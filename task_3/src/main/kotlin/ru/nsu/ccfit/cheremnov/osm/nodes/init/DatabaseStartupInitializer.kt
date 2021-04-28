package ru.nsu.ccfit.cheremnov.osm.nodes.init

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.nsu.ccfit.cheremnov.osm.database.initialization.JdbcDatabaseDataInitializer
import ru.nsu.ccfit.cheremnov.osm.nodes.entities.NodeRepository

@Component
class DatabaseStartupInitializer(
    private val nodeRepository: NodeRepository,

    @Value("\${osm.data.filepath}")
    private val osmDataFilepath: String,

    @Value("\${spring.datasource.url}")
    private val datasourceUrl: String,

    @Value("\${spring.datasource.username}")
    private val datasourceUsername: String,

    @Value("\${spring.datasource.password}")
    private val datasourcePassword: String
): CommandLineRunner {

    private val databaseInitializer = JdbcDatabaseDataInitializer(
        dataFilepath = osmDataFilepath,
        datasourceUrl = datasourceUrl,
        datasourceUsername = datasourceUsername,
        datasourcePassword = datasourcePassword
    )

    override fun run(vararg args: String) {
        val nodesCount = nodeRepository.count()
        if (nodesCount == 0L) {
            databaseInitializer.run()
        }
    }

}