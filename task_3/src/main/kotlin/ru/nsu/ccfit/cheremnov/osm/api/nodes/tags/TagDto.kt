package ru.nsu.ccfit.cheremnov.osm.api.nodes.tags

import javax.validation.constraints.NotBlank

data class TagDto(
    @field:NotBlank
    val key: String,

    @field:NotBlank
    val value: String
)