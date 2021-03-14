package ru.nsu.ccfit.cheremnov.database.transactions

import java.sql.Connection

open class JdbcTransactionService(
    private val connection: Connection
): TransactionService {

    override fun <R> performTransaction(
        isolation: TransactionIsolation,
        transactionBody: () -> R
    ): R = with(connection) {
        try {
            autoCommit = false
            transactionIsolation = isolation.toConnectionTransactionIsolation()
            val result = transactionBody()
            commit()
            return result
        } catch (ex: Exception) {
            rollback()
            throw ex
        }
    }

}

private fun TransactionIsolation.toConnectionTransactionIsolation() =
    when (this) {
        TransactionIsolation.NONE -> Connection.TRANSACTION_NONE
        TransactionIsolation.READ_UNCOMMITTED -> Connection.TRANSACTION_READ_UNCOMMITTED
        TransactionIsolation.READ_COMMITTED -> Connection.TRANSACTION_READ_COMMITTED
        TransactionIsolation.REPEATABLE_READ -> Connection.TRANSACTION_REPEATABLE_READ
        TransactionIsolation.SERIALIZABLE -> Connection.TRANSACTION_SERIALIZABLE
    }