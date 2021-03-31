package ru.nsu.ccfit.cheremnov.osm.database.datasource

import java.sql.Connection
import java.sql.DriverManager

object JdbcDataSource {

    fun createConnection(): Connection =
        DriverManager.getConnection(
            DatasourceConfig.datasourceUrl,
            DatasourceConfig.datasourceUsername,
            DatasourceConfig.datasourcePassword
        )

}
