package ru.nsu.ccfit.cheremnov.osm.database.model

data class DbNode(
    val id: Long,
    val username: String,
    val latitude: Double,
    val longitude: Double
)