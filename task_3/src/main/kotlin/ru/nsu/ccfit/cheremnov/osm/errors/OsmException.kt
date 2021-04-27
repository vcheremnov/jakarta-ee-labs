package ru.nsu.ccfit.cheremnov.osm.errors

open class OsmException(
    val errorCode: String,
    message: String,
    cause: Throwable? = null
): Exception(message, cause)

open class OsmNotFoundException(
    errorCode: String,
    message: String,
    cause: Throwable? = null
): OsmException(errorCode, message, cause)

open class OsmAlreadyExistsException(
    errorCode: String,
    message: String,
    cause: Throwable? = null
): OsmException(errorCode, message, cause)