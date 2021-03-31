package ru.nsu.ccfit.cheremnov.osm.database.datasource

import java.util.*

object DatasourceConfig {

    val datasourceUrl: String
    val datasourceUsername: String
    val datasourcePassword: String

    private const val propertiesFilename = "application.properties"

    init {
        val properties = Properties()

        DatasourceConfig::class.java
            .classLoader
            .getResourceAsStream(propertiesFilename)
            ?.use {
                properties.load(it)
            } ?: throw RuntimeException("Failed to load \"$propertiesFilename\"")

        datasourceUrl = properties.getProperty("datasource.url")
        datasourceUsername = properties.getProperty("datasource.username")
        datasourcePassword = properties.getProperty("datasource.password")
    }

}