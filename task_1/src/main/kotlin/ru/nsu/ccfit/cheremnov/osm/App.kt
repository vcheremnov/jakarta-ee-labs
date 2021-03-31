package ru.nsu.ccfit.cheremnov.osm

import ru.nsu.ccfit.cheremnov.osm.dataprocessing.CompressedFileInputDataSource
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.OsmXmlDataReader
import ru.nsu.ccfit.cheremnov.osm.stats.OsmDataAnalyzer
import ru.nsu.ccfit.cheremnov.osm.stats.OsmDataStatsPrinter
import ru.nsu.ccfit.cheremnov.osm.utils.printMessageTrace


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printUsage()
        return
    }

    val dataFilepath = args.first()
    val inputDataSource = CompressedFileInputDataSource(dataFilepath)
    val dataReader = OsmXmlDataReader()
    val dataAnalyzer = OsmDataAnalyzer(dataReader)
    val dataStatsPrinter = OsmDataStatsPrinter()

    dataAnalyzer
        .collectDataStats(inputDataSource)
        .onSuccess { dataStatsPrinter.printDataStats(it) }
        .onFailure { it.printMessageTrace() }
}

private fun printUsage() {
    System.err.println("Usage: ./gradlew task_1:run --args=\"<xml archive filepath>\"")
}

