package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import xyz.nietongxue.common.tree.TreeNode
import xyz.nietongxue.common.tree.toTree
import xyz.nietongxue.common.tree.toTreeNode

class PathTreeTest : StringSpec({
    "simple" {
        val paths = listOf(Path.fromString(""))
        val tree = toTree(paths)
        tree.records.size shouldBe 1
    }
    "more" {
        val paths = listOf(
            Path.fromString(""),
            Path.fromString("a"),
            Path.fromString("a/b")
        )
        val tree = toTree(paths)
        tree.records.size shouldBe 3
        val rootNode = toTreeNode(tree, { it })
        rootNode.children.size shouldBe 1
        rootNode shouldBeEqual TreeNode(
            "", "",
            listOf(
                TreeNode(
                    "a", "a", listOf(
                        TreeNode("a/b", "a/b", emptyList(), "a")
                    ), ""
                )
            ), null
        )
    }
    "more2" {
        val paths = listOf(
            Path.fromString("a"),
            Path.fromString("a/b"),
            Path.fromString("a/c")
        )
        val tree = toTree(paths)
        val rootNode = toTreeNode(tree, { it })
        rootNode.children.size shouldBe 1
        rootNode shouldBeEqual TreeNode(
            "", "",
            listOf(
                TreeNode(
                    "a", "a", listOf(
                        TreeNode("a/b", "a/b", emptyList(), "a"),
                        TreeNode("a/c", "a/c", emptyList(), "a")
                    ), ""
                )
            ), null
        )
    }
    "more3" {
        val paths = listOf(
            Path.fromString("a"),
            Path.fromString("a/b"),
            Path.fromString("a/c"),
            Path.fromString("a/c/d")
        )
        val tree = toTree(paths)
        val rootNode = toTreeNode(tree, { it })
        rootNode.children.size shouldBe 1
        rootNode shouldBeEqual TreeNode(
            "", "",
            listOf(
                TreeNode(
                    "a", "a", listOf(
                        TreeNode("a/b", "a/b", emptyList(), "a"),
                        TreeNode(
                            "a/c", "a/c", listOf(
                                TreeNode("a/c/d", "a/c/d", emptyList(), "a/c")
                            ), "a"
                        )
                    ), ""
                )
            ), null
        )
    }
    "more4" {
        val paths = listOf(
            Path.fromString("a/b"),
            Path.fromString("a/c/d")
        )
        val tree = toTree(paths)
        val rootNode = toTreeNode(tree, { it })
        rootNode.children.size shouldBe 1
        rootNode shouldBeEqual TreeNode(
            "", "",
            listOf(
                TreeNode(
                    "a", "a", listOf(
                        TreeNode("a/b", "a/b", emptyList(), "a"),
                        TreeNode(
                            "a/c", "a/c", listOf(
                                TreeNode("a/c/d", "a/c/d", emptyList(), "a/c")
                            ), "a"
                        )
                    ), ""
                )
            ), null
        )
    }
    "placeHolder for not existed" {
        val paths = listOf(
            Path.fromString("a/b"),
            Path.fromString("a/c/d")
        )
        val tree = toTree(paths)
        val rootNode: TreeNode<Path?> = toTreeNode(tree, { id ->
            paths.find { it.asString() == id }
        })
        rootNode shouldBeEqual TreeNode(
            "", null,
            listOf(
                TreeNode(
                    "a", null, listOf(
                        TreeNode("a/b", Path.fromString("a/b"), emptyList(), "a"),
                        TreeNode(
                            "a/c", null, listOf(
                                TreeNode("a/c/d", Path.fromString("a/c/d"), emptyList(), "a/c")
                            ), "a"
                        )
                    ), ""
                )
            ), null
        )
    }
})