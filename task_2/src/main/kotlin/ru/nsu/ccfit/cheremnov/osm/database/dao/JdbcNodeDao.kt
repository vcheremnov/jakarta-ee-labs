package ru.nsu.ccfit.cheremnov.osm.database.dao

import ru.nsu.ccfit.cheremnov.osm.database.escapeQuotes
import ru.nsu.ccfit.cheremnov.osm.database.model.DbNode
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

class JdbcNodeDao(
    connection: Connection
) : NodeDao {

    private val getNodeStatement: PreparedStatement = connection
        .prepareStatement("select * from nodes where id = ?")

    private val insertPreparedStatement: PreparedStatement = connection
        .prepareStatement("""
            insert into nodes (id, username, latitude, longitude) 
            values (?, ?, ?, ?)
        """.trimIndent())

    private val insertStatement: Statement = connection.createStatement()

    override fun get(id: Long): DbNode? {
        val resultSet = getNodeStatement.apply {
            setLong(1, id)
        }.executeQuery()

        resultSet.use {
            return if (it.next()) DbNode(
                id = it.getLong("id"),
                username = it.getString("username"),
                latitude = it.getDouble("latitude"),
                longitude = it.getDouble("longitude")
            ) else null
        }
    }

    override fun insert(node: DbNode) {
        insertStatement.executeUpdate("""
            insert into nodes(id, username, latitude, longitude)
            values (${node.id}, '${node.username.escapeQuotes()}', ${node.latitude}, ${node.longitude})
        """.trimIndent())
    }

    override fun insertPrepared(node: DbNode) {
        insertPreparedStatement.apply {
            setLong(1, node.id)
            setString(2, node.username)
            setDouble(3, node.latitude)
            setDouble(4, node.longitude)
        }.execute()
    }

    override fun insertBatch(nodes: Collection<DbNode>) {
        insertPreparedStatement.apply {
            nodes.forEach {
                setLong(1, it.id)
                setString(2, it.username)
                setDouble(3, it.latitude)
                setDouble(4, it.longitude)
                addBatch()
            }
        }.executeBatch()
    }

    override fun close() {
        insertStatement.close()
        insertPreparedStatement.close()
        getNodeStatement.close()
    }

}