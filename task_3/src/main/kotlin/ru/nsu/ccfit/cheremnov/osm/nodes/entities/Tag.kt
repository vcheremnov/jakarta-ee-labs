package ru.nsu.ccfit.cheremnov.osm.nodes.entities

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tags")
@IdClass(TagId::class)
class Tag(
    @Id
    @JoinColumn(name = "node_id")
    @ManyToOne(fetch = FetchType.EAGER)
    val node: Node,

    @Id
    @Column(name = "key")
    var key: String,

    @Column(name = "value")
    var value: String
) {

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as Tag

        return this.node == other.node
                && this.key == other.key
    }

    override fun hashCode(): Int {
        return Objects.hash(node, key)
    }

}

class TagId: Serializable {
    lateinit var node: Node
    lateinit var key: String
}