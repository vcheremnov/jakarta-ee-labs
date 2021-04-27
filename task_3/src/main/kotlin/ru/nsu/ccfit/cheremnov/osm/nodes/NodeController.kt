package ru.nsu.ccfit.cheremnov.osm.nodes

import org.springframework.web.bind.annotation.*
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NearestNodesResponse
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NodeDto
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NodePatchRequest
import ru.nsu.ccfit.cheremnov.osm.api.nodes.tags.TagDto
import ru.nsu.ccfit.cheremnov.osm.api.nodes.tags.TagPatchRequest
import javax.validation.Valid

@RestController
@RequestMapping("/nodes")
class NodeController(
    private val nodeService: NodeService
) {

    @PostMapping
    fun createNode(@Valid @RequestBody node: NodeDto) {
        nodeService.createNode(node)
    }

    @GetMapping("/{nodeId}")
    fun findNode(@PathVariable nodeId: Long): NodeDto {
        return nodeService.findNode(nodeId)
    }

    @PutMapping("/{nodeId}")
    fun updateNode(
        @PathVariable nodeId: Long,
        @Valid @RequestBody nodePatch: NodePatchRequest
    ): NodeDto {
        return nodeService.updateNode(nodeId, nodePatch)
    }

    @DeleteMapping("/{nodeId}")
    fun deleteNode(@PathVariable nodeId: Long) {
        nodeService.deleteNode(nodeId)
    }

    @PostMapping("/{nodeId}/tags")
    fun addNodeTag(
        @PathVariable nodeId: Long,
        @Valid @RequestBody tag: TagDto
    ): NodeDto {
        return nodeService.addNodeTag(nodeId, tag)
    }

    @PutMapping("/{nodeId}/tags/{tagKey}")
    fun updateNodeTag(
        @PathVariable nodeId: Long,
        @PathVariable tagKey: String,
        @Valid @RequestBody tagPatch: TagPatchRequest
    ): NodeDto {
        return nodeService.updateNodeTag(nodeId, tagKey, tagPatch)
    }

    @DeleteMapping("/{nodeId}/tags/{tagKey}")
    fun deleteNodeTag(
        @PathVariable nodeId: Long,
        @PathVariable tagKey: String
    ): NodeDto {
        return nodeService.deleteNodeTag(nodeId, tagKey)
    }

    @GetMapping("/nearest")
    fun findNearestNodes(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam radius: Double
    ): NearestNodesResponse {
        return nodeService.findNearestNodes(latitude, longitude, radius)
    }
}