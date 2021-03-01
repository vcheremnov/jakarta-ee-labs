package ru.nsu.ccfit.cheremnov.osmdata.processing

import java.io.InputStream


interface InputDataSource {
    fun openInputDataStream(): Result<InputStream>
}

class InputDataStreamOpeningFailed(message: String): Exception(message)
