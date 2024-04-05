package xyz.nietongxue.common.tree

import xyz.nietongxue.common.base.Path

fun toTree(paths: List<Path>): Tree {
    val records = toCompletedPathList(paths).map {
        TreeRecord(it.asString(), it.parent()?.asString())
    }
    return object : Tree {
        override val records: List<TreeRecord> = records
    }
}


fun toTreeOf(paths: List<Path>): TreeI<Path> {
    return object : TreeI<Path> {
        override val tree: Tree = toTree(paths)
        override fun getById(id: String): Path? {
            return paths.find { it.toString() == id }
        }
    }
}

fun toCompletedPathList(paths: List<Path>): List<Path> {
    val re = mutableSetOf<Path>()
    paths.forEach {
        it.ancestors().let { re.addAll(it) }
        re.add(it)
    }
    return re.toList().sortedBy { it.asString() }
}

