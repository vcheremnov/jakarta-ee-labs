package ru.nsu.ccfit.cheremnov.osm.api.nodes.tags

import javax.validation.constraints.NotBlank

data class TagPatchRequest(
    @field:NotBlank
    val value: String?
)