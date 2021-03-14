package ru.nsu.ccfit.cheremnov.dataprocessing

import org.apache.commons.compress.compressors.CompressorStreamFactory
import java.io.File
import java.io.InputStream

class CompressedFileInputDataSource(
    private val inputFilepath: String
): InputDataSource {

    override fun openInputDataStream(): Result<InputStream> =
        runCatching {
            File(inputFilepath)
                .inputStream()
                .buffered()
                .let { CompressorStreamFactory().createCompressorInputStream(it) }
        }.recoverCatching {
            throw InputDataStreamOpeningFailed("Failed to open input data file", it)
        }

}