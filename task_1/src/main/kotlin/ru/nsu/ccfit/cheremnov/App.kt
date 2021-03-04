package ru.nsu.ccfit.cheremnov

import ru.nsu.ccfit.cheremnov.stats.OsmDataAnalyzer
import ru.nsu.ccfit.cheremnov.stats.OsmDataStatsPrinter


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
        .onFailure {
            it.printCauseMessageTrace()
        }
}

private fun printUsage() {
    System.err.println("Usage: ./gradlew run --args=\"<xml archive filename>\"")
}

private fun Throwable.printCauseMessageTrace(indent: String = "") {
    if (cause == null) {
        System.err.println("${indent}${localizedMessage}")
    } else {
        System.err.println("${indent}${localizedMessage}. Cause:")
        cause!!.printCauseMessageTrace("${indent}\t")
    }
}

