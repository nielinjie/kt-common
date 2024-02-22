package xyz.nietongxue.common.tree

import xyz.nietongxue.common.base.Id


class TreeRecord(val id: Id, val parent: Id?)


interface TreeI<I> {
    val tree: Tree
    fun getById(id: Id): I?
    fun root(): I? = getById(tree.root().id)
    fun getChild(id: Id): List<I> {
        return tree.getChildren(id).map { getById(it.id)!! }
    }

    fun getDescendants(id: Id): List<I> {
        return tree.getDescendants(id).map { getById(it.id)!! }
    }

    fun getAncestors(id: Id): List<I> {
        return tree.getAncestors(id).map { getById(it.id)!! }
    }

    fun getLeaves(): List<I> {
        return tree.getLeaves().map { getById(it.id)!! }
    }
}

interface Tree {
    val records: List<TreeRecord>
    fun root(): TreeRecord = records.first { it.parent == null }

    //TODO 可以性能优化。不用每次计算全部
    fun children(): Map<Id?, List<TreeRecord>> = records.groupBy { it.parent }
    fun getChildren(id: Id): List<TreeRecord> {
        return children().get(id) ?: emptyList()
    }

    fun getDescendants(id: Id): List<TreeRecord> {
        return getChildren(id).flatMap { listOf(it) + getDescendants(it.id) }
    }

    fun getAncestors(id: Id): List<TreeRecord> {
        val parent = records.first { it.id == id }.parent
        return if (parent == null) emptyList() else listOf(records.first { it.id == parent }) + getAncestors(parent)
    }


    fun getLeaves(): List<TreeRecord> {
        return records.filter { getChildren(it.id).isEmpty() }
    }

    fun getDepth(id: Id): Int {
        return getAncestors(id).size
    }

    fun getDepth(): Int {
        return records.map { getDepth(it.id) }.max()
    }


}
