package ru.nsu.ccfit.cheremnov.database.initialization

import java.io.Closeable

interface DatabaseInitializer: Closeable {

    fun initializeDatabase()

}