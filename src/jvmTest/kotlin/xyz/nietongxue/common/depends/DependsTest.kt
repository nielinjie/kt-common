package xyz.nietongxue.common.depends

import io.kotest.assertions.throwables.shouldThrowWithMessage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DependsTest {
    @Test
    fun testDirectDependency() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))

        val result = listDependents(nodeB, listOf(nodeA, nodeB))

        Assertions.assertEquals(listOf(nodeA), result)
    }

    @Test
    fun testIndirectDependency() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))
        val nodeC = NodeWithDepends("C", listOf("B"))

        val result = listDependents(nodeC, listOf(nodeA, nodeB, nodeC))

        Assertions.assertEquals(listOf(nodeB, nodeA).sortedBy { it.name }, result.sortedBy { it.name })
    }


    @Test
    fun testMultiDependency() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeE = NodeWithDepends("E", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))
        val nodeC = NodeWithDepends("C", listOf("B","E"))
        val result = listDependents(nodeC, listOf(nodeA, nodeB, nodeC,nodeE))

        Assertions.assertEquals(listOf(nodeB, nodeA,  nodeE).sortedBy { it.name }, result.sortedBy { it.name })
    }
    @Test
    fun testMultiDependencyNotInAll() {
        val nodeA = NodeWithDepends("A", emptyList())
        val nodeE = NodeWithDepends("E", emptyList())
        val nodeB = NodeWithDepends("B", listOf("A"))
        val nodeC = NodeWithDepends("C", listOf("B","E"))
        shouldThrowWithMessage<IllegalStateException>("depends not in all - E") {
            val result = listDependents(nodeC, listOf(nodeA, nodeB, nodeC,))

            Assertions.assertEquals(listOf(nodeB, nodeA, nodeE).sortedBy { it.name }, result.sortedBy { it.name })
        }
    }

    @Test
    fun testCircularDependency() {
        val nodeA = NodeWithDepends("A", listOf("B"))
        val nodeB = NodeWithDepends("B", listOf("A"))

        val result = listDependents(nodeA, listOf(nodeA, nodeB))

        Assertions.assertEquals(listOf(nodeB), result)
    }

    @Test
    fun testNoDependencies() {
        val nodeA = NodeWithDepends("A", emptyList())

        val result = listDependents(nodeA, listOf(nodeA))

        Assertions.assertEquals(listOf<NodeWithDepends>(), result)
    }
}