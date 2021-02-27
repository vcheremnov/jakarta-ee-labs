package ru.nsu.ccfit.cheremnov.osmdata.stats

class OsmDataStatsPrinter {

    fun printDataStats(dataStats: OsmDataStats) {
        println("*** User edits stats ***")
        dataStats.userEditsNumber.printSortedByValues()
        println("\n*** Tags usage stats ***")
        dataStats.tagUsagesNumber.printSortedByValues()
    }

    private fun <K, V: Comparable<V>> Map<K, V>.printSortedByValues() {
        toList()
            .sortedByDescending { it.second }
            .forEach{ println("${it.first} -> ${it.second}") }
    }

}