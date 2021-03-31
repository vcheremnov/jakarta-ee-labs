package ru.nsu.ccfit.cheremnov.osm.benchmarks

import ru.nsu.ccfit.cheremnov.osm.database.initialization.DatabaseInitializer
import ru.nsu.ccfit.cheremnov.osm.database.services.NodeService
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.InputDataSource
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.OsmDataReader
import ru.nsu.ccfit.cheremnov.osm.model.Node
import kotlin.system.measureTimeMillis

class InsertPerformanceTester(
    private val databaseInitializer: DatabaseInitializer,
    private val nodeService: NodeService,
    private val dataReader: OsmDataReader,
    private val inputDataSource: InputDataSource
) {

    companion object {
        private const val batchSize = 50000
    }

    fun performInsertTests() {
        performInsertTest("Batch insert test:") {
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

        performInsertTest("Prepared insert test:") {
            dataReader.readAndProcessData(inputDataSource) {
                nodeService.insert(it)
            }
        }

        performInsertTest("Plain insert test:") {
            dataReader.readAndProcessData(inputDataSource) {
                nodeService.insert(it)
            }
        }
    }

    private fun performInsertTest(message: String, testBody: () -> Unit) {
        println(message)
        databaseInitializer.initializeDatabase()
        measureTimeMillis {
            testBody()
        }.let {
            println("Elapsed time: ${"%.3fs".format(it / 1000.0)}\n")
        }
    }
}