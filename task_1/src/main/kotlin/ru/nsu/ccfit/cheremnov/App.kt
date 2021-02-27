package ru.nsu.ccfit.cheremnov

import ru.nsu.ccfit.cheremnov.osmdata.stats.OsmDataAnalyzer
import ru.nsu.ccfit.cheremnov.osmdata.stats.OsmDataStatsPrinter

fun main(args: Array<String>) {
    val dataFilepath = args.first()
    val inputDataSource = CompressedFileInputDataSource(dataFilepath)
    val dataReader = OsmXmlDataReader()
    val dataAnalyzer = OsmDataAnalyzer(dataReader)
    val dataStatsPrinter = OsmDataStatsPrinter()

    val dataStats = dataAnalyzer.collectDataStats(inputDataSource)
    dataStatsPrinter.printDataStats(dataStats)
}