package ru.nsu.ccfit.cheremnov.osm.api.errors

data class ValidationSubError(
    val field: String,
    val message: String? = null
): OsmSubError