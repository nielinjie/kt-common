package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FPathTest : StringSpec({
    "pathAndExtension" {
        val path = "a/b/c.txt"
        val fpath = java.nio.file.Path.of(path)
        val (pathString, extension) = fpath.pathAndExtension()
        pathString shouldBe "a/b/c"
        extension shouldBe "txt"
    }
    "fileNam and Extension" {
        val path = "a/b/c.txt"
        val fpath = java.nio.file.Path.of(path)
        val (fileName, extension) = fpath.fileNameAndExtension()
        fileName shouldBe "c"
        extension shouldBe "txt"
    }
    "splitTo3" {
        val path = "a/b/c.txt"
        val fpath = java.nio.file.Path.of(path)
        val (first, middle, last) = fpath.splitTo3()
        first shouldBe "a/b"
        middle shouldBe "c"
        last shouldBe ".txt"
    }
}
)