package ru.nsu.ccfit.cheremnov.stats

data class OsmDataStats(
    val userEditsNumber: Map<String, Long>,
    val tagUsagesNumber: Map<String, Long>
)