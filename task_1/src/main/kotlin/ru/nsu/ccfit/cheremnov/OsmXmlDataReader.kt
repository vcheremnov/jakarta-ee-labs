package ru.nsu.ccfit.cheremnov

import org.apache.logging.log4j.LogManager
import ru.nsu.ccfit.cheremnov.osmdata.processing.InputDataSource
import ru.nsu.ccfit.cheremnov.osmdata.processing.OsmDataReader
import ru.nsu.ccfit.cheremnov.osmdata.model.Node
import ru.nsu.ccfit.cheremnov.osmdata.model.Tag
import java.lang.RuntimeException
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

    override fun readAndProcessData(inputDataSource: InputDataSource, nodeProcessor: (Node) -> Unit) {
        inputDataSource.getInputDataStream().use {
            var tag: Tag? = null
            var node: Node? = null
            var nodeTags: MutableSet<Tag>? = null

            val xmlReader = XMLInputFactory.newInstance().createXMLEventReader(it)
            while (xmlReader.hasNext()) {
                val event = xmlReader.nextEvent()
                if (event.isStartElement) {
                    val startElement = event.asStartElement()
                    when (startElement.name.localPart) {
                        nodeElementName -> {
                            if (node != null) {
                                throw RuntimeException("Nested node has been encountered")
                            }

                            val user = startElement
                                .getAttributeByName(QName(nodeUserAttributeName))?.value
                                ?: throw RuntimeException("User was not specified in the node")

                            nodeTags = mutableSetOf()
                            node = Node(user, nodeTags)
                        }

                        tagElementName -> {
                            if (tag != null) {
                                throw RuntimeException("Nested tag has been encountered")
                            }

                            if (node == null) {
                                continue
                            }

                            val key = startElement
                                .getAttributeByName(QName(tagKeyAttributeName))?.value
                                ?: throw RuntimeException("Key was not specified in the tag")

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

}