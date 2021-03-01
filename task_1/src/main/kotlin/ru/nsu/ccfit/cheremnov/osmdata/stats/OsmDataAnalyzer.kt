package ru.nsu.ccfit.cheremnov.osmdata.stats

import ru.nsu.ccfit.cheremnov.osmdata.processing.InputDataSource
import ru.nsu.ccfit.cheremnov.osmdata.processing.OsmDataReader


class OsmDataAnalyzer(
    private val dataReader: OsmDataReader,
) {

    fun collectDataStats(inputDataSource: InputDataSource): Result<OsmDataStats> {
        val userEditsNumber = mutableMapOf<String, Long>()
        val tagUsagesNumber = mutableMapOf<String, Long>()

        return dataReader.readAndProcessData(inputDataSource) {
            userEditsNumber.incrementValue(it.user)
            for (tag in it.tags) {
                tagUsagesNumber.incrementValue(tag.key)
            }
        }.map { OsmDataStats(userEditsNumber, tagUsagesNumber) }
    }

    private fun <K> MutableMap<K, Long>.incrementValue(key: K) {
        val oldValue = getOrDefault(key, 0)
        put(key, oldValue + 1)
    }

}