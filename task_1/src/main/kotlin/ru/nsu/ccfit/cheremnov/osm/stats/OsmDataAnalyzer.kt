package ru.nsu.ccfit.cheremnov.osm.stats

import ru.nsu.ccfit.cheremnov.osm.dataprocessing.InputDataSource
import ru.nsu.ccfit.cheremnov.osm.dataprocessing.OsmDataReader


class OsmDataAnalyzer(
    private val dataReader: OsmDataReader,
) {

    fun collectDataStats(inputDataSource: InputDataSource): Result<OsmDataStats> {
        val userEditsNumber = mutableMapOf<String, Long>()
        val tagUsagesNumber = mutableMapOf<String, Long>()

        return dataReader.readAndProcessData(inputDataSource) {
            userEditsNumber.incrementValue(it.username)
            for (tag in it.tags) {
                tagUsagesNumber.incrementValue(tag.key)
            }
        }.map {
            OsmDataStats(userEditsNumber, tagUsagesNumber)
        }.recoverCatching {
            throw DataAnalysisFailed("Failed to collect data stats", it)
        }
    }

    private fun <K> MutableMap<K, Long>.incrementValue(key: K) {
        val oldValue = getOrDefault(key, 0)
        put(key, oldValue + 1)
    }

}

class DataAnalysisFailed(
    message: String,
    cause: Throwable? = null
): Exception(message, cause)

