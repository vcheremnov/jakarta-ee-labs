package ru.nsu.ccfit.cheremnov.osm.nodes.entities

import javax.persistence.*

@Entity
@Table(name = "nodes")
class Node(
    @Id
    @Column(name = "id")
    val id: Long,

    @Column(name = "username")
    val username: String,

    @Column(name = "latitude")
    var latitude: Double,

    @Column(name = "longitude")
    var longitude: Double,

    @OneToMany(
        mappedBy = "node",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    val tags: MutableSet<Tag> = hashSetOf()
) {

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as Node

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

interface NodeRepository {

    fun count(): Long

    fun create(node: Node): Boolean

    fun find(nodeId: Long): Node?

    fun delete(nodeId: Long): Boolean

    fun findNearest(latitude: Double, longitude: Double, radius: Double): List<Node>

}

