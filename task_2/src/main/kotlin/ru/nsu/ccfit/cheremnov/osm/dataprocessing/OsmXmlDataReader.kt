package ru.nsu.ccfit.cheremnov.osm.dataprocessing

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import ru.nsu.ccfit.cheremnov.osm.model.Node
import ru.nsu.ccfit.cheremnov.osm.model.Tag
import ru.nsu.ccfit.cheremnov.osm.model.generated.XmlNode
import ru.nsu.ccfit.cheremnov.osm.model.generated.XmlTag
import java.io.InputStream
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory


class OsmXmlDataReader: ru.nsu.ccfit.cheremnov.osm.dataprocessing.AbstractOsmXmlDataReader() {

    override val logger: Logger = LogManager.getLogger()

    override fun readAndProcessData(inputDataStream: InputStream, nodeProcessor: (Node) -> Unit) {
        val xmlNodeClass = ru.nsu.ccfit.cheremnov.osm.model.generated.XmlNode::class.java
        val unmarshaller = JAXBContext.newInstance(xmlNodeClass).createUnmarshaller()
        val xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(inputDataStream)

        while (xmlReader.hasNext()) {
            if (xmlReader.isStartElement && xmlReader.name.localPart == nodeElementName) {
                val xmlNode = unmarshaller.unmarshal(xmlReader, xmlNodeClass)
                val node = xmlNode.value.toNode()
                nodeProcessor(node)
            } else {
                xmlReader.next()
            }
        }
    }

}

private fun XmlNode.toNode(): Node =
    Node(
        id = id,
        username = user,
        latitude = lat,
        longitude = lon,
        tags = tag.map(ru.nsu.ccfit.cheremnov.osm.model.generated.XmlTag::toTag)
    )

private fun XmlTag.toTag(): Tag =
    Tag(
        key = k,
        value = v
    )
