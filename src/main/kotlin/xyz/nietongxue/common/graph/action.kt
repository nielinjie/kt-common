package xyz.nietongxue.common.graph

import xyz.nietongxue.common.graph.selectors.NodeSelector
import xyz.nietongxue.common.processing.*

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

data class UpdateNode<V : BaseGraph>(val selector: NodeSelector<V>, val nodeUpdater: (Node?) -> Node) :
    Action<V, GraphLog> {
    override fun action(): Reducer<V, GraphLog> {
        return { graph ->
            val nodes = selector.select(graph.nodes(), graph)
            if (nodes.size > 1) {
                justError(StopResult(StringLog("UpdateNode failed: selector ${selector} matched ${nodes.size} nodes")))
            } else {
                val node = nodes.firstOrNull()
                val newNode = nodeUpdater(node)
                if (node != null)
                    graph.nodes.remove(node)
                graph.nodes.add(newNode)
                bind(graph)
            }
        }
    }
}