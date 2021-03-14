package ru.nsu.ccfit.cheremnov

import ru.nsu.ccfit.cheremnov.benchmarks.InsertPerformanceTester
import ru.nsu.ccfit.cheremnov.database.datasource.JdbcDataSource
import ru.nsu.ccfit.cheremnov.database.initialization.JdbcDatabaseInitializer
import ru.nsu.ccfit.cheremnov.database.services.JdbcNodeService
import ru.nsu.ccfit.cheremnov.dataprocessing.CompressedFileInputDataSource
import ru.nsu.ccfit.cheremnov.dataprocessing.OsmXmlDataReader
import ru.nsu.ccfit.cheremnov.utils.printMessageTrace


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
            JdbcDatabaseInitializer(connection).use { databaseInitializer ->
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

