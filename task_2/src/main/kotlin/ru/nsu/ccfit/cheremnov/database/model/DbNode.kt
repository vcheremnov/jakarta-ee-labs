package ru.nsu.ccfit.cheremnov.database.model

data class DbNode(
    val id: Long,
    val username: String,
    val latitude: Double,
    val longitude: Double
)