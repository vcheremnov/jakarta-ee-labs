package ru.nsu.ccfit.cheremnov.osm.nodes

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.nsu.ccfit.cheremnov.osm.nodes.entities.Node
import ru.nsu.ccfit.cheremnov.osm.nodes.entities.NodeRepository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class JpaNodeRepository(
    @PersistenceContext
    val entityManager: EntityManager
): NodeRepository {

    @Transactional
    override fun create(node: Node): Boolean {
        val nodeExists = entityManager.find(Node::class.java, node.id) != null
        if (nodeExists) {
            return false
        }

        entityManager.persist(node)
        return true
    }

    override fun find(nodeId: Long): Node? =
        entityManager.find(Node::class.java, nodeId)

    @Transactional
    override fun delete(nodeId: Long): Boolean {
        val query = "delete from Node n where n.id = :nodeId"
        val deletesNumber = entityManager.createQuery(query)
            .setParameter("nodeId", nodeId)
            .executeUpdate()

        return deletesNumber != 0
    }

    @Suppress("UNCHECKED_CAST")
    override fun findNearest(latitude: Double, longitude: Double, radius: Double): List<Node> {
        val query = "select * from find_nearest_nodes(?1, ?2, ?3)"
        return entityManager.createNativeQuery(query, Node::class.java)
            .setParameter(1, latitude)
            .setParameter(2, longitude)
            .setParameter(3, radius)
            .resultList as List<Node>
    }

    override fun count(): Long {
        val query = "select count(n) from Node n"
        return entityManager
            .createQuery(query, java.lang.Long::class.java)
            .singleResult as Long
    }

}