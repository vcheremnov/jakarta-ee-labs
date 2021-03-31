package ru.nsu.ccfit.cheremnov.osm.dataprocessing

import java.io.InputStream


interface InputDataSource {
    fun openInputDataStream(): Result<InputStream>
}

class InputDataStreamOpeningFailed(
    message: String,
    cause: Throwable? = null
): Exception(message, cause)
