package ru.nsu.ccfit.cheremnov.osm.database.services

import ru.nsu.ccfit.cheremnov.osm.database.dao.NodeDao
import ru.nsu.ccfit.cheremnov.osm.database.dao.TagDao
import ru.nsu.ccfit.cheremnov.osm.database.model.DbNode
import ru.nsu.ccfit.cheremnov.osm.database.model.DbTag
import ru.nsu.ccfit.cheremnov.osm.database.transactions.TransactionService
import ru.nsu.ccfit.cheremnov.osm.model.Node
import ru.nsu.ccfit.cheremnov.osm.model.Tag


abstract class AbstractNodeService: NodeService {

    protected abstract val transactionService: TransactionService
    protected abstract val nodeDao: NodeDao
    protected abstract val tagDao: TagDao

    override fun get(id: Long): Node? {
        return transactionService.performTransaction {
            val dbNode = nodeDao.get(id)
            val dbTags = tagDao.getAllByNodeId(id)
            dbNode?.toNode(dbTags)
        }
    }

    override fun insert(nodes: Collection<Node>) {
        transactionService.performTransaction {
            nodes
                .map(Node::toDbNode)
                .forEach { nodeDao.insert(it) }

            nodes
                .flatMap(Node::getDbTags)
                .forEach { tagDao.insert(it) }
        }
    }

    override fun insertPrepared(nodes: Collection<Node>) {
        transactionService.performTransaction {
            nodes
                .map(Node::toDbNode)
                .forEach { nodeDao.insertPrepared(it) }

            nodes
                .flatMap(Node::getDbTags)
                .forEach { tagDao.insertPrepared(it) }
        }
    }

    override fun insertBatch(nodes: Collection<Node>) {
        transactionService.performTransaction {
            nodes
                .map(Node::toDbNode)
                .also { nodeDao.insertBatch(it) }

            nodes
                .flatMap(Node::getDbTags)
                .also { tagDao.insertBatch(it) }
        }
    }

    override fun close() {
        nodeDao.close()
        tagDao.close()
    }

}

private fun DbNode.toNode(dbTags: List<DbTag>): Node =
    Node(
        id = id,
        username = username,
        latitude = latitude,
        longitude = longitude,
        tags = dbTags.map(DbTag::toTag)
    )

private fun Node.toDbNode(): DbNode =
    DbNode(
        id = id,
        username = username,
        latitude = latitude,
        longitude = longitude
    )

private fun Node.getDbTags(): List<DbTag> =
    tags.map { it.toDbTag(id) }

private fun DbTag.toTag(): Tag =
    Tag(
        key = key,
        value = value
    )

private fun Tag.toDbTag(nodeId: Long): DbTag =
    DbTag(
        nodeId = nodeId,
        key = key,
        value = value
    )
