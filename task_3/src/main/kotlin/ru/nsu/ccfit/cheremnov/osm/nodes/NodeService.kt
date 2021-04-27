package ru.nsu.ccfit.cheremnov.osm.nodes

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NearestNodesResponse
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NodeDto
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NodeErrorCode
import ru.nsu.ccfit.cheremnov.osm.api.nodes.NodePatchRequest
import ru.nsu.ccfit.cheremnov.osm.api.nodes.tags.TagDto
import ru.nsu.ccfit.cheremnov.osm.api.nodes.tags.TagErrorCode
import ru.nsu.ccfit.cheremnov.osm.api.nodes.tags.TagPatchRequest
import ru.nsu.ccfit.cheremnov.osm.errors.OsmAlreadyExistsException
import ru.nsu.ccfit.cheremnov.osm.errors.OsmNotFoundException
import ru.nsu.ccfit.cheremnov.osm.nodes.entities.Node
import ru.nsu.ccfit.cheremnov.osm.nodes.entities.NodeRepository
import ru.nsu.ccfit.cheremnov.osm.nodes.entities.Tag

@Service
class NodeService(
    private val nodeRepository: NodeRepository
) {

    @Transactional
    fun createNode(nodeDto: NodeDto) {
        val node = nodeDto.toEntity()
        val hasBeenCreated = nodeRepository.create(node)
        if (!hasBeenCreated) {
            throw NodeAlreadyExists(nodeDto.id)
        }
    }

    fun findNode(nodeId: Long): NodeDto =
        findNodeOrThrow(nodeId).toDto()

    @Transactional
    fun updateNode(nodeId: Long, nodePatch: NodePatchRequest): NodeDto =
        findNodeOrThrow(nodeId).apply {
            latitude = nodePatch.latitude ?: latitude
            longitude = nodePatch.longitude ?: longitude
        }.toDto()

    @Transactional
    fun deleteNode(nodeId: Long) {
        val hasBeenDeleted = nodeRepository.delete(nodeId)
        if (!hasBeenDeleted) {
            throw NodeNotFound(nodeId)
        }
    }

    fun findNearestNodes(latitude: Double, longitude: Double, radius: Double): NearestNodesResponse =
        NearestNodesResponse(
            nodes = nodeRepository.findNearest(latitude, longitude, radius).map(Node::toDto)
        )

    @Transactional
    fun addNodeTag(nodeId: Long, tagDto: TagDto): NodeDto {
        val node = findNodeOrThrow(nodeId)
        val newTag = tagDto.toEntity(node)
        val hasBeenAdded = node.tags.add(newTag)
        if (!hasBeenAdded) {
            throw TagAlreadyExists(nodeId, tagDto.key)
        }

        return node.toDto()
    }

    @Transactional
    fun updateNodeTag(nodeId: Long, tagKey: String, tagPatch: TagPatchRequest): NodeDto {
        val node = findNodeOrThrow(nodeId)
        val tag = node.findTagOrThrow(tagKey)
        tag.apply { value = tagPatch.value ?: value }

        return node.toDto()
    }

    @Transactional
    fun deleteNodeTag(nodeId: Long, tagKey: String): NodeDto {
        val node = findNodeOrThrow(nodeId)
        val tag = node.findTagOrThrow(tagKey)
        node.tags.remove(tag)

        return node.toDto()
    }

    private fun findNodeOrThrow(nodeId: Long): Node =
        nodeRepository.find(nodeId) ?: throw NodeNotFound(nodeId)

    private fun Node.findTagOrThrow(tagKey: String): Tag =
        tags.find { it.key == tagKey } ?: throw TagNotFound(id, tagKey)

}

fun Node.toDto() =
    NodeDto(
        id = id,
        username = username,
        latitude = latitude,
        longitude = longitude,
        tags = tags.map(Tag::toDto)
    )

fun Tag.toDto() =
    TagDto(
        key = key,
        value = value
    )

fun NodeDto.toEntity() =
    Node(
        id = id,
        username = username,
        latitude = latitude,
        longitude = longitude,
        tags = hashSetOf()
    ).also { node ->
        node.tags.addAll(
            this.tags.map { it.toEntity(node) }
        )
    }

fun TagDto.toEntity(node: Node) =
    Tag(
        key = key,
        value = value,
        node = node
    )

class NodeNotFound(
    nodeId: Long
): OsmNotFoundException(
    errorCode = NodeErrorCode.NODE_NOT_FOUND.name,
    message = "Node with id '$nodeId' does not exist"
)

class NodeAlreadyExists(
    nodeId: Long
): OsmAlreadyExistsException(
    errorCode = NodeErrorCode.NODE_ALREADY_EXISTS.name,
    message = "Node with id '$nodeId' already exists"
)

class TagNotFound(
    nodeId: Long,
    tagKey: String
): OsmNotFoundException(
    errorCode = TagErrorCode.TAG_NOT_FOUND.name,
    message = "Node with id '$nodeId' does not have a tag with key '$tagKey'"
)

class TagAlreadyExists(
    nodeId: Long,
    tagKey: String
): OsmAlreadyExistsException(
    errorCode = TagErrorCode.TAG_ALREADY_EXISTS.name,
    message = "Node with id '$nodeId' already has a tag with key '$tagKey'"
)

