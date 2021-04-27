package ru.nsu.ccfit.cheremnov.osm.database.initialization

import ru.nsu.ccfit.cheremnov.osm.database.transactions.JdbcTransactionService
import java.sql.Connection
import java.sql.Statement

class JdbcDatabaseSchemaInitializer(
    connection: Connection
): DatabaseSchemaInitializer {

    companion object {
        private const val schemaSqlFilename = "db_schema.sql"
    }

    private val transactionService = JdbcTransactionService(connection)

    private val initSchemaStatement: Statement = connection.createStatement()

    private val schemaSql: String =
        JdbcDatabaseSchemaInitializer::class.java
            .classLoader
            .getResourceAsStream(schemaSqlFilename)
            ?.use {
                it.reader().readText()
            } ?: throw RuntimeException("Failed to load \"$schemaSqlFilename\"")

    override fun initializeDatabase() {
        transactionService.performTransaction {
            initSchemaStatement.executeUpdate(schemaSql)
        }
    }

    override fun close() {
        initSchemaStatement.close()
    }

}