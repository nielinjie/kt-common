package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import xyz.nietongxue.common.tree.toCompletedPathList

class PathTest : StringSpec({
    "simple" {
        val paths = listOf(Path.fromString(""))
        toCompletedPathList(paths).also {
            it shouldBe paths
        }
        val paths2 = listOf(Path.fromString("a"))
        toCompletedPathList(paths2).also {
            it shouldBe listOf(Path.fromString("")) + paths2
        }

    }
    "ancestors" {
        val path = Path.fromString("a/b/c")
        path.ancestors() shouldBe listOf(
            Path.fromString(""),
            Path.fromString("a"),
            Path.fromString("a/b")
        )
    }
    "more" {
        val paths = listOf(
            Path.fromString(""),
            Path.fromString("a/b")
        )
        toCompletedPathList(paths).also {
            it shouldBe listOf(
                Path.fromString(""),
                Path.fromString("a"), Path.fromString("a/b")
            )
        }
    }
    "more2" {
        val paths = listOf(
            Path.fromString("a/b"),
            Path.fromString("a/c")
        )
        toCompletedPathList(paths).also {
            it shouldBe listOf(
                Path.fromString(""),
                Path.fromString("a"),
                Path.fromString("a/b"),
                Path.fromString("a/c")
            )
        }
    }
})