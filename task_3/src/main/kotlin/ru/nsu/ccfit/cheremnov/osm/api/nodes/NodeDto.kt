package ru.nsu.ccfit.cheremnov.osm.api.nodes

import ru.nsu.ccfit.cheremnov.osm.api.nodes.tags.TagDto
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

data class NodeDto(
    @field:PositiveOrZero
    val id: Long,

    @field:NotBlank
    val username: String,

    @field:[
    Min(-90)
    Max(90)
    ]
    val latitude: Double,

    @field:[
    Min(-180)
    Max(180)
    ]
    val longitude: Double,

    @field:Valid
    val tags: Collection<TagDto>
)