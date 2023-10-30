package xyz.nietongxue.common.graph

import xyz.nietongxue.common.base.Name
import xyz.nietongxue.common.base.Selector
import xyz.nietongxue.common.processing.*


/*
1. node types
2. edge types
3. port types
4. connection validation
5. connection situation
 */



sealed interface Quantifier {
    fun match(number: Int): Boolean
    data class Many(val number: Int?) : Quantifier {
        override fun match(number: Int): Boolean {
            return this.number?.let { number == it } ?: (number > 1)
        }
    }

    data object One : Quantifier {
        override fun match(number: Int): Boolean {
            return number == 1
        }
    }

    data object ZeroOrOne : Quantifier {
        override fun match(number: Int): Boolean {
            return number in 0..1
        }
    }

    data object OneOrMore : Quantifier {
        override fun match(number: Int): Boolean {
            return number >= 1
        }
    }

    data object ZeroOrMany : Quantifier {
        override fun match(number: Int): Boolean {
            return true
        }
    }
}

interface NodeSelector<V : Graph> : Selector<Node, V>
interface EdgeSelector<V : Graph> : Selector<Edge, V>
open class EdgeByNodeAndPortSelector<V : Graph>(val nodeSelector: NodeSelector<V>, val portName: Name) :
    EdgeSelector<V> {
    override fun select(input: List<Edge>, context: V): List<Edge> {
        val nodes = nodeSelector.select(context.nodes(), context)
        return input.filter { edge ->
            nodes.any { it.id == edge.from } && edge.on == portName
        }
    }
}

open class NodeQuantifierRule<V : BaseGraph>(val nodes: NodeSelector<V>, val quantifier: Quantifier) :
    Rule<V, GraphLog> {
    override fun check(value: V, phase: AssemblePhase): ProcessingWithLog<V, GraphLog> {
        val number = nodes.select(value.nodes(), value).size
        return if (quantifier.match(number)) {
            bind(value)
        } else {
            justError(StopResult(StringLog("NodeQuantifierRule failed")))
        }
    }

}

interface GraphLog {

}

data class StringLog(val s: String) : GraphLog

class NodePortRule<V : BaseGraph>(val sourceSelector: NodeSelector<V>, val portNames: List<Name>) : Rule<V, GraphLog> {
    override fun check(value: V, phase: AssemblePhase): ProcessingWithLog<V, GraphLog> {
        val sourceNodes = sourceSelector.select(value.nodes(), value)
        sourceNodes.forEach {
            val connections = value.connections(it.id)
            if (!connections.all { connection ->
                    portNames.contains(connection.on)
                }) {
                return justError(StopResult(StringLog("NodePortRule failed")))
            }
        }
        return bind(value)
    }

}

class ConnectionTargetRule<V : BaseGraph>(
    val sourceSelector: NodeSelector<V>,
    val portName: Name,
    val targetSelector: NodeSelector<V>
) :
    Rule<V, GraphLog> {
    override fun check(value: V, phase: AssemblePhase): ProcessingWithLog<V, GraphLog> {
        val sourceNodes = sourceSelector.select(value.nodes(), value)
        sourceNodes.forEach {
            val connections = value.connections(it.id, portName)
            connections.forEach { connection ->
                val targetNodes = targetSelector.select(value.nodes(), value)
                if (!targetNodes.any { it.id == connection.to }) {
                    return@check justError(StopResult(StringLog("ConnectionTargetRule failed")))
                }
            }
        }
        return bind(value)
    }
}

open class ConnectionQualityRule<V : BaseGraph>(
    val sourceSelector: NodeSelector<V>,
    val portName: Name,
    val quantifier: Quantifier
) :
    Rule<V, GraphLog> {
    override fun check(value: V, phase: AssemblePhase): ProcessingWithLog<V, GraphLog> {
        val sourceNodes = sourceSelector.select(value.nodes(), value)
        sourceNodes.forEach {
            val connections = value.connections(it.id, portName)
            if (!quantifier.match(connections.size)) {
                return@check justError(StopResult(StringLog("ConnectionQualityRule failed")))
            }
        }
        return bind(value)
    }
}
