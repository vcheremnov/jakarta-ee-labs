package ru.nsu.ccfit.cheremnov.osm.dataprocessing

import org.apache.logging.log4j.Logger
import ru.nsu.ccfit.cheremnov.osm.model.Node
import java.io.InputStream

abstract class AbstractOsmXmlDataReader: OsmDataReader {

    protected companion object {
        const val nodeElementName = "node"
        const val tagElementName = "tag"

        const val nodeIdAttributeName = "id"
        const val nodeUserAttributeName = "user"
        const val nodeLatitudeAttributeName = "lat"
        const val nodeLongitudeAttributeName = "lon"

        const val tagKeyAttributeName = "k"
        const val tagValueAttributeName = "v"
    }

    protected abstract val logger: Logger

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

    protected abstract fun readAndProcessData(inputDataStream: InputStream, nodeProcessor: (Node) -> Unit)

}
