package ru.nsu.ccfit.cheremnov.osmdata.processing

import java.io.InputStream


interface InputDataSource {
    fun getInputDataStream(): InputStream
}
