package ru.nsu.ccfit.cheremnov.osm.api.nodes

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class NodePatchRequest(
    @field:[
    Min(-90)
    Max(90)
    ]
    val latitude: Double?,

    @field:[
    Min(-180)
    Max(180)
    ]
    val longitude: Double?
)