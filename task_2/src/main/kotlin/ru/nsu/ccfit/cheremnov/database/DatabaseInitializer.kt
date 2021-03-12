package ru.nsu.ccfit.cheremnov.database

import java.io.Closeable
import java.sql.Connection
import java.sql.Statement

class DatabaseInitializer(
    connection: Connection
): Closeable {

    companion object {
        private const val schemaSqlFilename = "schema.sql"
    }

    private val initSchemaStatement: Statement = connection.createStatement()

    private val schemaSql: String =
        DatabaseInitializer::class.java
            .classLoader
            .getResourceAsStream(schemaSqlFilename)
            ?.use {
                it.reader().readText()
            } ?: throw RuntimeException("Failed to load \"$schemaSqlFilename\"")

    fun initializeDatabase() {
        initSchemaStatement.executeUpdate(schemaSql)
    }

    override fun close() {
        initSchemaStatement.close()
    }

}