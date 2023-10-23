package xyz.nietongxue.common.graph

import java.util.*


typealias Id = UUID
typealias Name = String


interface HasId {
    val id: Id
}

interface Graph
interface Node
interface Port
interface Edge


open class BaseGraph : Graph {
    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()
    fun node(node: Node) {
        nodes.add(node)
    }

    fun edge(edge: Edge) {
        edges.add(edge)
    }
}

open class BaseNode(val id: Id) : Node {
    val ports = mutableListOf<Port>()
}

open class BaseEdge(
    val from: Id,
    val to: Id,
    val on: Name
) : Edge

class BasePort(val name: Name) : Port {

}