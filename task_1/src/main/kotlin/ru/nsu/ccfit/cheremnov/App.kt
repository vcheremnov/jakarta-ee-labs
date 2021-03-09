package ru.nsu.ccfit.cheremnov

import ru.nsu.ccfit.cheremnov.processing.CompressedFileInputDataSource
import ru.nsu.ccfit.cheremnov.processing.OsmXmlDataReader
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
        .onFailure { it.printMessageTrace() }
}

private fun printUsage() {
    System.err.println("Usage: ./gradlew task_1:run --args=\"<xml archive filepath>\"")
}

private fun Throwable.printMessageTrace(indent: String = "") {
    if (cause == null) {
        System.err.println("${indent}${localizedMessage}")
    } else {
        System.err.println("${indent}${localizedMessage}. Cause:")
        cause!!.printMessageTrace("${indent}\t")
    }
}

