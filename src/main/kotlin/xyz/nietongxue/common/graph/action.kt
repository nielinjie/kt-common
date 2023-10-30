package xyz.nietongxue.common.graph

import xyz.nietongxue.common.processing.Action
import xyz.nietongxue.common.processing.Reducer
import xyz.nietongxue.common.processing.bind

data class AddNode<V : BaseGraph, L : GraphLog>(val node: Node) : Action<V, L> {
    override fun action(): Reducer<V, L> {
        return { graph ->
            graph.nodes.add(node as BaseNode)
            bind(graph)
        }
    }
}

data class Connect<V : BaseGraph, L : GraphLog>(val edge: Edge) : Action<V, L> {
    override fun action(): Reducer<V, L> {
        return { graph ->
            graph.edges.add(edge)
            bind(graph)
        }
    }
}