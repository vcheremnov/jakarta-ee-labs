package ru.nsu.ccfit.cheremnov.database.services

import ru.nsu.ccfit.cheremnov.database.dao.JdbcNodeDao
import ru.nsu.ccfit.cheremnov.database.dao.JdbcTagDao
import ru.nsu.ccfit.cheremnov.database.transactions.JdbcTransactionService
import java.sql.Connection

class JdbcNodeService(
    connection: Connection,
): AbstractNodeService() {

    override val tagDao = JdbcTagDao(connection)
    override val nodeDao = JdbcNodeDao(connection)
    override val transactionService = JdbcTransactionService(connection)

}