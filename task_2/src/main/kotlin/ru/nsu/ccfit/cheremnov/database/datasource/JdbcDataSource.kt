package ru.nsu.ccfit.cheremnov.database.datasource

import java.sql.Connection
import java.sql.DriverManager

object JdbcDataSource {

    fun createConnection(autoCommit: Boolean = false): Connection =
        DriverManager.getConnection(
            DatasourceConfig.datasourceUrl,
            DatasourceConfig.datasourceUsername,
            DatasourceConfig.datasourcePassword
        ).apply { this.autoCommit = autoCommit }

}