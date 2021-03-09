package ru.nsu.ccfit.cheremnov.processing

import org.apache.logging.log4j.LogManager
import ru.nsu.ccfit.cheremnov.model.Node
import ru.nsu.ccfit.cheremnov.model.Tag
import java.io.InputStream
import javax.xml.namespace.QName
import javax.xml.stream.XMLInputFactory


class OsmXmlDataReader: OsmDataReader {

    companion object {
        private const val nodeElementName = "node"
        private const val tagElementName = "tag"
        private const val nodeUserAttributeName = "user"
        private const val tagKeyAttributeName = "k"
    }

    private val logger = LogManager.getLogger()

    override fun readAndProcessData(dataSource: InputDataSource, nodeProcessor: (Node) -> Unit): Result<Unit> {
        return dataSource
            .also { logger.info("Opening input data stream") }
            .openInputDataStream()
            .mapCatching { inputDataStream ->
                logger.info("Started input data processing")
                inputDataStream.use { readAndProcessData(it, nodeProcessor) }
            }.recoverCatching {
                val message = when (it) {
                    is InputDataStreamOpeningFailed -> "Failed to open input data stream"
                    else -> "Failed to process input data"
                }
                throw DataProcessingFailed(message, it)
            }.onSuccess {
                logger.info("Successfully processed input data")
            }.onFailure {
                logger.error(it.stackTraceToString())
            }
    }

    private fun readAndProcessData(inputDataStream: InputStream, nodeProcessor: (Node) -> Unit) {
        var tag: Tag? = null
        var node: Node? = null
        var nodeTags: MutableList<Tag>? = null

        val xmlReader = XMLInputFactory.newInstance().createXMLEventReader(inputDataStream)
        while (xmlReader.hasNext()) {
            val event = xmlReader.nextEvent()
            if (event.isStartElement) {
                val startElement = event.asStartElement()
                when (startElement.name.localPart) {
                    nodeElementName -> {
                        if (node != null) {
                            throw DataProcessingFailed("Nested node has been encountered")
                        }

                        val user = startElement
                            .getAttributeByName(QName(nodeUserAttributeName))?.value
                            ?: throw DataProcessingFailed("User is not specified in the node")

                        nodeTags = mutableListOf()
                        node = Node(user, nodeTags)
                    }

                    tagElementName -> {
                        if (tag != null) {
                            throw DataProcessingFailed("Nested tag has been encountered")
                        }

                        if (node == null) {
                            continue
                        }

                        val key = startElement
                            .getAttributeByName(QName(tagKeyAttributeName))?.value
                            ?: throw DataProcessingFailed("Key is not specified in the tag")

                        tag = Tag(key)
                    }
                }
            } else if (event.isEndElement) {
                val endElement = event.asEndElement()
                when (endElement.name.localPart) {
                    nodeElementName -> {
                        nodeProcessor(node!!)
                        node = null
                    }

                    tagElementName -> {
                        if (tag != null) {
                            nodeTags!!.add(tag)
                            tag = null
                        }
                    }
                }
            }
        }
    }

}