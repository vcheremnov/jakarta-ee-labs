package ru.nsu.ccfit.cheremnov.osm.database.services

import ru.nsu.ccfit.cheremnov.osm.database.dao.JdbcNodeDao
import ru.nsu.ccfit.cheremnov.osm.database.dao.JdbcTagDao
import ru.nsu.ccfit.cheremnov.osm.database.transactions.JdbcTransactionService
import java.sql.Connection

class JdbcNodeService(
    connection: Connection,
): AbstractNodeService() {

    override val tagDao = JdbcTagDao(connection)
    override val nodeDao = JdbcNodeDao(connection)
    override val transactionService = JdbcTransactionService(connection)

}