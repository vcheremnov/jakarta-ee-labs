package ru.nsu.ccfit.cheremnov.database.dao

import ru.nsu.ccfit.cheremnov.database.model.DbTag
import java.io.Closeable

interface TagDao: Closeable {

    fun getAllByNodeId(nodeId: Long): List<DbTag>

    fun insert(tag: DbTag)

    fun insertPrepared(tag: DbTag)

    fun insertBatch(tags: Collection<DbTag>)

}