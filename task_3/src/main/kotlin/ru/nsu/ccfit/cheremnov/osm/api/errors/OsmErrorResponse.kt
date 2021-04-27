package ru.nsu.ccfit.cheremnov.osm.api.errors

import java.time.LocalDateTime

data class OsmErrorResponse(
    val path: String,
    val errorCode: String,
    val timestamp: LocalDateTime,
    val message: String? = null,
    val subErrors: List<OsmSubError>? = null
)

interface OsmSubError