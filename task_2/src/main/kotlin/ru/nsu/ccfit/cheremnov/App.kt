package ru.nsu.ccfit.cheremnov

import ru.nsu.ccfit.cheremnov.processing.CompressedFileInputDataSource
import ru.nsu.ccfit.cheremnov.processing.OsmXmlDataReader


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printUsage()
        return
    }

    val dataFilepath = args.first()
    val inputDataSource = CompressedFileInputDataSource(dataFilepath)
    val dataReader = OsmXmlDataReader()
}

private fun printUsage() {
    System.err.println("Usage: ./gradlew task_2:run --args=\"<xml archive filepath>\"")
}

