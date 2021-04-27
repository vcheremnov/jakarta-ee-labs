package ru.nsu.ccfit.cheremnov.osm

import ru.nsu.ccfit.cheremnov.osm.benchmarks.InsertPerformanceTester
import ru.nsu.ccfit.cheremnov.osm.database.datasource.JdbcDataSource
import ru.nsu.ccfit.cheremnov.osm.database.initialization.JdbcDatabaseSchemaInitializer
import ru.nsu.ccfit.cheremnov.osm.database.services.JdbcNodeService
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.CompressedFileInputDataSource
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.OsmXmlDataReader
import ru.nsu.ccfit.cheremnov.osm.utils.printMessageTrace


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printUsage()
        return
    }

    val dataFilepath = args.first()
    val inputDataSource = CompressedFileInputDataSource(dataFilepath)
    val dataReader = OsmXmlDataReader()

    runCatching {
        JdbcDataSource.createConnection().use { connection ->
            JdbcDatabaseSchemaInitializer(connection).use { databaseInitializer ->
                JdbcNodeService(connection).use { nodeService ->
                    InsertPerformanceTester(
                        databaseInitializer,
                        nodeService,
                        dataReader,
                        inputDataSource
                    ).performInsertTests()
                }
            }
        }
    }.onFailure {
        it.printMessageTrace()
    }
}

private fun printUsage() {
    System.err.println("Usage: ./gradlew task_2:run --args=\"<xml archive filepath>\"")
}

