package xyz.nietongxue.common.graph

import xyz.nietongxue.common.base.HasId
import xyz.nietongxue.common.base.HasName
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.common.base.Name




interface Graph {
    fun nodes(): List<Node>
    fun edges(): List<Edge>
}

interface Node : HasId {
}

interface Port : HasName


interface Edge {
    val from: Id
    val to: Id
    val on: Name
}


open class BaseGraph : Graph {
    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()


    override fun nodes(): List<Node> {
        return this.nodes
    }

    override fun edges(): List<Edge> {
        return this.edges
    }

    fun connections(nodeId: Id): List<Edge> {
        return nodes.find { it.id == nodeId }?.let { node ->
            edges.filter { it.from == node.id }
        } ?: emptyList()
    }

    fun connections(nodeId: Id, portName: Name): List<Edge> {
        return connections(nodeId).filter {
            it.on == portName
        }
    }
}

open class BaseNode(override val id: Id) : Node {
}

open class BaseEdge(
    override val from: Id,
    override val on: Name,
    override val to: Id
) : Edge

class BasePort(override val name: Name) : Port {

}

