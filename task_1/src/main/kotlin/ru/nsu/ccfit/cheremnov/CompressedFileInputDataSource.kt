package ru.nsu.ccfit.cheremnov

import org.apache.commons.compress.compressors.CompressorStreamFactory
import ru.nsu.ccfit.cheremnov.osmdata.processing.InputDataStreamOpeningFailed
import ru.nsu.ccfit.cheremnov.osmdata.processing.InputDataSource
import java.io.File
import java.io.FileNotFoundException
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
            if (it is FileNotFoundException) {
                throw InputDataStreamOpeningFailed(
                    "Failed to open file $inputFilepath: ${it.localizedMessage}"
                )
            } else throw it
        }

}