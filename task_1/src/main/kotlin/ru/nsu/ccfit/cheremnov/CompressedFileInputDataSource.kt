package ru.nsu.ccfit.cheremnov

import org.apache.commons.compress.compressors.CompressorStreamFactory
import ru.nsu.ccfit.cheremnov.osmdata.processing.InputDataSource
import java.io.File
import java.io.InputStream

class CompressedFileInputDataSource(
    private val inputFilepath: String
): InputDataSource {

    override fun getInputDataStream(): InputStream {
        return File(inputFilepath).inputStream().buffered()
            .let { CompressorStreamFactory().createCompressorInputStream(it) }
    }

}