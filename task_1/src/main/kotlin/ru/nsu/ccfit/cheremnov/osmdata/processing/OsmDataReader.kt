package ru.nsu.ccfit.cheremnov.osmdata.processing

import ru.nsu.ccfit.cheremnov.osmdata.model.Node


interface OsmDataReader {
    fun readAndProcessData(dataSource: InputDataSource, nodeProcessor: (Node) -> Unit): Result<Unit>
}

class DataProcessingFailed(message: String): Exception(message)