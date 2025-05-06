package xyz.nietongxue.common.depends

import org.junit.jupiter.api.Test

class TestDepends() {
    @Test
    fun test() {
        val objects = listOf(
            NodeWithDepends("A", listOf("B", "C")),
            NodeWithDepends("B", listOf("D", "E")),
            NodeWithDepends("C", listOf("E")),
            NodeWithDepends("D", listOf("F")),
            NodeWithDepends("E", emptyList()),
            NodeWithDepends("F", emptyList())
        )

        val sortedObjects = topologicalSortDFS(objects)
        println("拓扑排序结果: $sortedObjects")
        println("依赖关系: ${sortedObjects.reversed().joinToString(" -> ")}")
    }
    @Test
    fun test2() {
        val objects = listOf(
            NodeWithDepends("A", listOf("B", "C")),
            NodeWithDepends("B", listOf("D", "E")),
            NodeWithDepends("C", listOf("E")),
            NodeWithDepends("D", listOf("F")),
            NodeWithDepends("E", listOf("A")), // 添加循环依赖
            NodeWithDepends("F", listOf())
        )

        val sortedObjects = topologicalSortDFS(objects)
        println("拓扑排序结果: $sortedObjects")
        println("依赖关系: ${sortedObjects.reversed().joinToString(" -> ")}")
    }
}