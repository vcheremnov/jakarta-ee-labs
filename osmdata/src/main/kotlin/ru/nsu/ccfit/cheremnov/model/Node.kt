package ru.nsu.ccfit.cheremnov.model

data class Node(
    val id: Long,
    val username: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<Tag>
)