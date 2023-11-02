package xyz.nietongxue.common.graph

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import xyz.nietongxue.common.base.v7

class SelectorsTest : StringSpec({

    val node1 = BaseNode(v7())
    val node2 = BaseNode(v7())
    val graph = BaseGraph().also {
        it.nodes.addAll(listOf(node1, node2))
        it.edges.add(BaseEdge(node1.id, "to2", node2.id))
    }
    "node selector" {
        val selector = nodeSelector<_,BaseGraph> { it :BaseNode -> it.id == node1.id }
        val nodes = selector.select(graph.nodes,graph)
        nodes.size shouldBe 1
    }
    "node id selector"{
        val selector = nodeById(node1.id)
        val nodes = selector.select(graph.nodes,graph)
        nodes.size shouldBe 1
    }
    "edge by source and port"{
        val selector = edgeBySourceAndPort(nodeById(node1.id),"to2")
        val edges = selector.select(graph.edges,graph)
        edges.size shouldBe 1
    }
    "edge by port and target"{
        val selector = edgeByPortAndTarget("to2",nodeById(node2.id))
        val edges = selector.select(graph.edges,graph)
        edges.size shouldBe 1
    }
    "no edge should be found with wrong port"{
        val selector = edgeBySourceAndPort(nodeById(node1.id),"to3")
        val edges = selector.select(graph.edges,graph)
        edges.size shouldBe 0
    }
})