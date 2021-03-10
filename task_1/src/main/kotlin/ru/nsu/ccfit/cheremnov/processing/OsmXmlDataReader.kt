package ru.nsu.ccfit.cheremnov.processing

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import ru.nsu.ccfit.cheremnov.model.Node
import ru.nsu.ccfit.cheremnov.model.Tag
import java.io.InputStream
import javax.xml.namespace.QName
import javax.xml.stream.XMLInputFactory


class OsmXmlDataReader: AbstractOsmXmlDataReader() {

    override val logger: Logger = LogManager.getLogger()

    override fun readAndProcessData(inputDataStream: InputStream, nodeProcessor: (Node) -> Unit) {
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