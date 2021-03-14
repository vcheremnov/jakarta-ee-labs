package ru.nsu.ccfit.cheremnov.database.services

import ru.nsu.ccfit.cheremnov.model.Node
import java.io.Closeable

interface NodeService: Closeable {

    fun get(id: Long): Node?

    fun insert(node: Node)

    fun insertPrepared(node: Node)

    fun insertBatch(nodes: Collection<Node>)

}