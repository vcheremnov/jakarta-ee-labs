package ru.nsu.ccfit.cheremnov.database.dao

import ru.nsu.ccfit.cheremnov.database.model.DbNode
import java.io.Closeable

interface NodeDao: Closeable {

    fun get(id: Long): DbNode?

    fun insert(node: DbNode)

    fun insertPrepared(node: DbNode)

    fun insertBatch(nodes: Collection<DbNode>)

}