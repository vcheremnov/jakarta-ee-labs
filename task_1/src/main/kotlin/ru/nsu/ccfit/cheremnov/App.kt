package ru.nsu.ccfit.cheremnov

import ru.nsu.ccfit.cheremnov.osmdata.stats.OsmDataAnalyzer
import ru.nsu.ccfit.cheremnov.osmdata.stats.OsmDataStatsPrinter

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
        .onFailure { println(it.localizedMessage) }
}

private fun printUsage() {
    println("Usage: ./gradlew run --args=\"<xml archive filename>\"")
}