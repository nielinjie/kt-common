package xyz.nietongxue.common.graph

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import xyz.nietongxue.common.base.v7
import xyz.nietongxue.common.graph.selectors.edgeByPortAndTarget
import xyz.nietongxue.common.graph.selectors.edgeBySourceAndPort
import xyz.nietongxue.common.graph.selectors.nodeSelector
import xyz.nietongxue.common.graph.selectors.nodeById

class SelectorsTest : StringSpec({

    val node1 = BaseNode(v7())
    val node2 = BaseNode(v7())
    val graph = BaseGraph().also {
        it.nodes_.addAll(listOf(node1, node2))
        it.edges_.add(BaseEdge(node1.id, "to2", node2.id))
    }
    "node selector" {
        val selector = nodeSelector<_,BaseGraph> { it :BaseNode -> it.id == node1.id }
        val nodes = selector.select(graph.nodes_,graph)
        nodes.size shouldBe 1
    }
    "node id selector"{
        val selector = nodeById<BaseGraph>(node1.id)
        val nodes = selector.select(graph.nodes_,graph)
        nodes.size shouldBe 1
    }
    "edge by source and port"{
        val selector = edgeBySourceAndPort(nodeById(node1.id),"to2")
        val edges = selector.select(graph.edges_,graph)
        edges.size shouldBe 1
    }
    "edge by port and target"{
        val selector = edgeByPortAndTarget("to2",nodeById(node2.id))
        val edges = selector.select(graph.edges_,graph)
        edges.size shouldBe 1
    }
    "no edge should be found with wrong port"{
        val selector = edgeBySourceAndPort(nodeById(node1.id),"to3")
        val edges = selector.select(graph.edges_,graph)
        edges.size shouldBe 0
    }
})