package xyz.nietongxue.common.graph

import xyz.nietongxue.common.base.Name


/*
1. node types
2. edge types
3. port types
4. connection validation
5. connection situation
 */



sealed interface Quantifier {
    data class Many(val number: Int) : Quantifier
    data object One : Quantifier
    data object ZeroOrOne : Quantifier
    data object OneOrMore : Quantifier
    data object ZeroOrMany : Quantifier
}

interface NodeSelector {
    fun select(graph: Graph): List<Node>
}

interface NodePredicate {
    fun test(node: Node): Boolean
}

data class WrongConnectionException(val from: Node, val to: Node, val on: Name) : Exception(){

}
data class NoSuchPortException(val node: Node, val port: Port) : Exception(){

}