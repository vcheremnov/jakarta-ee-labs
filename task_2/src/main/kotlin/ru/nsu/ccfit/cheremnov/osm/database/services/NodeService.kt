package ru.nsu.ccfit.cheremnov.osm.database.services

import ru.nsu.ccfit.cheremnov.osm.model.Node
import java.io.Closeable

interface NodeService: Closeable {

    fun get(id: Long): Node?

    fun insert(nodes: Collection<Node>)

    fun insertPrepared(nodes: Collection<Node>)

    fun insertBatch(nodes: Collection<Node>)

}