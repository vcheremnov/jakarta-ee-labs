package ru.nsu.ccfit.cheremnov.osmdata.processing

import ru.nsu.ccfit.cheremnov.osmdata.model.Node


interface OsmDataReader {
    fun readAndProcessData(inputDataSource: InputDataSource, nodeProcessor: (Node) -> Unit)
}