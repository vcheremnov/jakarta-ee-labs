package ru.nsu.ccfit.cheremnov.osm.stats

data class OsmDataStats(
    val userEditsNumber: Map<String, Long>,
    val tagUsagesNumber: Map<String, Long>
)