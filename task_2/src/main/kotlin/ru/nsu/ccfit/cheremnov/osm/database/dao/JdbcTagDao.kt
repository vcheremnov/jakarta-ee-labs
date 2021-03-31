package ru.nsu.ccfit.cheremnov.osm.database.dao

import ru.nsu.ccfit.cheremnov.osm.database.model.DbTag
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

class JdbcTagDao(
    connection: Connection
): TagDao {

    private val getTagsStatement: PreparedStatement = connection
        .prepareStatement("select * from tags where node_id = ?")

    private val insertPreparedStatement: PreparedStatement = connection
        .prepareStatement("""
            insert into tags (node_id, key, value) 
            values (?, ?, ?)
        """.trimIndent())

    private val insertStatement: Statement = connection.createStatement()

    override fun getAllByNodeId(nodeId: Long): List<DbTag> {
        val resultSet = getTagsStatement.apply {
            setLong(1, nodeId)
        }.executeQuery()

        resultSet.use {
            val resultList = mutableListOf<DbTag>()
            while (it.next()) {
                val tag = DbTag(
                    nodeId = it.getLong("node_id"),
                    key = it.getString("key"),
                    value = it.getString("value")
                )
                resultList.add(tag)
            }
            return resultList
        }
    }

    override fun insert(tag: DbTag) {
        insertStatement.executeUpdate("""
            insert into tags(node_id, key, value)
            values (${tag.nodeId}, '${tag.key}', '${tag.value}')
        """.trimIndent())
    }

    override fun insertPrepared(tag: DbTag) {
        insertPreparedStatement.apply {
            setLong(1, tag.nodeId)
            setString(2, tag.key)
            setString(3, tag.value)
        }.execute()
    }

    override fun insertBatch(tags: Collection<DbTag>) {
        insertPreparedStatement.apply {
            tags.forEach {
                setLong(1, it.nodeId)
                setString(2, it.key)
                setString(3, it.value)
                addBatch()
            }
        }.executeBatch()
    }

    override fun close() {
        insertStatement.close()
        insertPreparedStatement.close()
        getTagsStatement.close()
    }
}