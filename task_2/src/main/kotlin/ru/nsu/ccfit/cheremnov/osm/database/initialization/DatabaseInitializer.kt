package ru.nsu.ccfit.cheremnov.osm.database.initialization

import java.io.Closeable

interface DatabaseInitializer: Closeable {

    fun initializeDatabase()

}