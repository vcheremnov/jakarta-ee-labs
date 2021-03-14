package ru.nsu.ccfit.cheremnov.database.transactions

interface TransactionService {

    fun <R> performTransaction(
        isolation: TransactionIsolation = TransactionIsolation.READ_COMMITTED,
        transactionBody: () -> R
    ): R

}

enum class TransactionIsolation {
    NONE,
    READ_UNCOMMITTED,
    READ_COMMITTED,
    REPEATABLE_READ,
    SERIALIZABLE
}