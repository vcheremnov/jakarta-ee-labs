package ru.nsu.ccfit.cheremnov.osmdata.model

data class Node(
    val user: String,
    val tags: Set<Tag>
)