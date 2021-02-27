package ru.nsu.ccfit.cheremnov.osmdata.stats

data class OsmDataStats(
    val userEditsNumber: Map<String, Long>,
    val tagUsagesNumber: Map<String, Long>
)