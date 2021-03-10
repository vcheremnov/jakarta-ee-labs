package ru.nsu.ccfit.cheremnov.processing

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import ru.nsu.ccfit.cheremnov.model.Node
import ru.nsu.ccfit.cheremnov.model.Tag
import ru.nsu.ccfit.cheremnov.model.generated.XmlNode
import ru.nsu.ccfit.cheremnov.model.generated.XmlTag
import java.io.InputStream
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory


class OsmXmlDataReader: AbstractOsmXmlDataReader() {

    override val logger: Logger = LogManager.getLogger()

    override fun readAndProcessData(inputDataStream: InputStream, nodeProcessor: (Node) -> Unit) {
        val xmlNodeClass = XmlNode::class.java
        val jc = JAXBContext.newInstance(xmlNodeClass)
        val unmarshaller = jc.createUnmarshaller()
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

private fun XmlNode.toNode() =
    Node(
        user = user,
        tags = tag.map(XmlTag::toTag)
    )

private fun XmlTag.toTag() =
    Tag(
        key = k
    )
