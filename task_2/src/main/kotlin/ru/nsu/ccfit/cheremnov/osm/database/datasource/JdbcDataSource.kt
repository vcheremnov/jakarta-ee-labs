package ru.nsu.ccfit.cheremnov.osm.database.datasource

import java.sql.Connection
import java.sql.DriverManager

object JdbcDataSource {

    fun createConnection(): Connection =
        createConnection(
            DatasourceConfig.datasourceUrl,
            DatasourceConfig.datasourceUsername,
            DatasourceConfig.datasourcePassword
        )

    fun createConnection(
        datasourceUrl: String,
        datasourceUsername: String,
        datasourcePassword: String
    ): Connection =
        DriverManager.getConnection(
            datasourceUrl,
            datasourceUsername,
            datasourcePassword
        )

}
