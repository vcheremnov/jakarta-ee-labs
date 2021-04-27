package ru.nsu.ccfit.cheremnov.osm.database.initialization

import ru.nsu.ccfit.cheremnov.osm.database.datasource.DatasourceConfig
import ru.nsu.ccfit.cheremnov.osm.database.datasource.JdbcDataSource
import ru.nsu.ccfit.cheremnov.osm.database.services.JdbcNodeService
import ru.nsu.ccfit.cheremnov.osm.database.services.NodeService
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.CompressedFileInputDataSource
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.OsmXmlDataReader
import ru.nsu.ccfit.cheremnov.osm.model.Node

class JdbcDatabaseDataInitializer(
    dataFilepath: String,
    private val datasourceUrl: String = DatasourceConfig.datasourceUrl,
    private val datasourceUsername: String = DatasourceConfig.datasourceUsername,
    private val datasourcePassword: String = DatasourceConfig.datasourcePassword
) {

    companion object {
        private const val batchSize = 50000
    }

    private val inputDataSource = CompressedFileInputDataSource(dataFilepath)
    private val dataReader = OsmXmlDataReader()

    fun run() {
        JdbcDataSource.createConnection(
            datasourceUrl,
            datasourceUsername,
            datasourcePassword
        ).use { connection ->
            JdbcNodeService(connection).use { nodeService ->
                insertData(nodeService)
            }
        }
    }

    private fun insertData(nodeService: NodeService) {
        val batch = arrayListOf<Node>()
        dataReader.readAndProcessData(inputDataSource) {
            batch.add(it)
            if (batch.size == batchSize) {
                nodeService.insertBatch(batch)
                batch.clear()
            }
        }

        if (batch.isNotEmpty()) {
            nodeService.insertBatch(batch)
        }
    }
}