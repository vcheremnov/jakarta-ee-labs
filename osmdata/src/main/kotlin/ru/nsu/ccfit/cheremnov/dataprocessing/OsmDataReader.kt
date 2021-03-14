package ru.nsu.ccfit.cheremnov.dataprocessing

import ru.nsu.ccfit.cheremnov.model.Node


interface OsmDataReader {
    fun readAndProcessData(dataSource: InputDataSource, nodeProcessor: (Node) -> Unit): Result<Unit>
}

class DataProcessingFailed(
    message: String,
    cause: Throwable? = null
): Exception(message, cause)