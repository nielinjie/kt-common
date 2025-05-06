package xyz.nietongxue.common.depends

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WhoDependOnMeTest() {
    @Test
    fun testDirectDependents() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))

        val result = whoDependOnMe(nodeA, listOf(nodeA, nodeB))

        assertEquals(listOf(nodeB), result)
    }

    @Test
    fun testIndirectDependents() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))
        val nodeC = NodeWithDepends("C", listOf("B"))

        val result = whoDependOnMe(nodeA, listOf(nodeA, nodeB, nodeC))

        assertEquals(listOf(nodeB, nodeC).sortedBy { it.name }, result.sortedBy { it.name })
    }


    @Test
    fun testMultiDependents() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))
        val nodeC = NodeWithDepends("C", listOf("A", "B"))
        val nodeD = NodeWithDepends("D", listOf("C"))

        val result = whoDependOnMe(nodeA, listOf(nodeA, nodeB, nodeC, nodeD))
        assertEquals(listOf(nodeB, nodeC, nodeD).sortedBy { it.name }, result.sortedBy { it.name })
    }

    @Test
    fun testCircularDependents() {
        val nodeA = NodeWithDepends("A", listOf("B"))
        val nodeB = NodeWithDepends("B", listOf("A"))

        val result = whoDependOnMe(nodeA, listOf(nodeA, nodeB))

        assertEquals(listOf(nodeB), result)
    }

    @Test
    fun testNoDependents() {
        val nodeA = NodeWithDepends("A", emptyList())

        val result = whoDependOnMe(nodeA, listOf(nodeA))

        assertEquals(emptyList<NodeWithDepends>(), result)
    }
}