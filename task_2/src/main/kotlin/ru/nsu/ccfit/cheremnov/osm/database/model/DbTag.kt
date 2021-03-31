package ru.nsu.ccfit.cheremnov.osm.database.model

data class DbTag(
    val nodeId: Long,
    val key: String,
    val value: String
)