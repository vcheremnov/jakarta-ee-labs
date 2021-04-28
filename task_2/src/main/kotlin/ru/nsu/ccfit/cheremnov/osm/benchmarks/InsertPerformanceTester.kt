package ru.nsu.ccfit.cheremnov.osm.benchmarks

import ru.nsu.ccfit.cheremnov.osm.database.initialization.DatabaseSchemaInitializer
import ru.nsu.ccfit.cheremnov.osm.database.services.NodeService
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.InputDataSource
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.OsmDataReader
import ru.nsu.ccfit.cheremnov.osm.model.Node

class InsertPerformanceTester(
    private val databaseSchemaInitializer: DatabaseSchemaInitializer,
    private val nodeService: NodeService,
    private val dataReader: OsmDataReader,
    private val inputDataSource: InputDataSource
) {

    companion object {
        private const val batchSize = 50000
    }

    fun performInsertTests() {
        performInsertTest("Batch insert test:", nodeService::insertBatch)
        performInsertTest("Prepared insert test:", nodeService::insertPrepared)
        performInsertTest("Plain insert test:", nodeService::insert)
    }

    private fun performInsertTest(message: String, insertMethod: (Collection<Node>) -> Unit) {
        println(message)
        databaseSchemaInitializer.initializeDatabase()

        var nodesInserted = 0L
        val beginTime = System.currentTimeMillis()
        val batch = arrayListOf<Node>()
        dataReader.readAndProcessData(inputDataSource) {
            batch.add(it)
            if (batch.size == batchSize) {
                insertMethod(batch)
                nodesInserted += batch.size
                batch.clear()
                showProgress(nodesInserted, beginTime)
            }
        }.onSuccess {
            if (batch.isNotEmpty()) {
                nodeService.insertBatch(batch)
                nodesInserted += batch.size
                showProgress(nodesInserted, beginTime)
            }
            println("Test has been finished\n")
        }.getOrThrow()
    }

    private fun showProgress(nodesInserted: Long, beginTime: Long) {
        val elapsedTime = (System.currentTimeMillis() - beginTime) / 1000.0
        println("Nodes inserted: $nodesInserted, elapsed time: ${"%.3fs".format(elapsedTime)}")
    }
}