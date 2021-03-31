package ru.nsu.ccfit.cheremnov.osm.dataprocessing

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import ru.nsu.ccfit.cheremnov.osm.model.Node
import ru.nsu.ccfit.cheremnov.osm.model.Tag
import java.io.InputStream
import javax.xml.namespace.QName
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement


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

                        nodeTags = mutableListOf()
                        node = startElement.retrieveNode(nodeTags)
                    }

                    tagElementName -> {
                        if (tag != null) {
                            throw DataProcessingFailed("Nested tag has been encountered")
                        }

                        if (node == null) {
                            continue
                        }

                        tag = startElement.retrieveTag()
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

    private fun StartElement.retrieveNode(emptyTagsList: List<Tag>): Node =
        retrieveAttributes(
            nodeIdAttributeName,
            nodeUserAttributeName,
            nodeLatitudeAttributeName,
            nodeLongitudeAttributeName
        ).let {
            Node(
                id = it[nodeIdAttributeName]!!.toLong(),
                username = it[nodeUserAttributeName]!!,
                latitude = it[nodeLatitudeAttributeName]!!.toDouble(),
                longitude = it[nodeLongitudeAttributeName]!!.toDouble(),
                tags = emptyTagsList
            )
        }

    private fun StartElement.retrieveTag(): Tag =
        retrieveAttributes(
            tagKeyAttributeName,
            tagValueAttributeName
        ).let {
            Tag(
                key = it[tagKeyAttributeName]!!,
                value = it[tagValueAttributeName]!!
            )
        }

    private fun StartElement.retrieveAttributes(vararg attributeNames: String): Map<String, String> =
        attributeNames.map {
            val attributeValue = getAttributeByName(QName(it))?.value
                ?: throw DataProcessingFailed("Attribute \"$it\" is not specified in the ${name.localPart}")
            it to attributeValue
        }.toMap()

}