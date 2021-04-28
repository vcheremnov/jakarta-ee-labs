package ru.nsu.ccfit.cheremnov.osm.database

fun String.escapeQuotes() =
    replace("'", "''")