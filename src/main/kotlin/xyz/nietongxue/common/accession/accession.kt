package xyz.nietongxue.common.accession

import xyz.nietongxue.common.base.HasId
import xyz.nietongxue.common.base.Id


interface Zero<V> {
    val zero: V
}

open class Accession(
    override val id: Id,
    val base: Id?,
    val reason: List<Id>,
) : HasId

fun <A : Accession> List<A>.append(accession: A): List<A> {
    if (this.isEmpty())
        return listOf(accession)
    if (accession.base == this.first().id) {
        return listOf(accession) + this
    } else {
        error("base is not match")
    }
}

abstract class AccessionBranch<V, A : Accession> {
    abstract val zero: Zero<V>
    var accessions = listOf<A>()
    fun append(accession: A): Id {
        this.accessions = accessions.append(accession)
        return accession.id
    }

    fun newest(): Id? {
        return accessions.firstOrNull()?.id
    }

    fun oldToNew(): List<A> {
        return accessions.reversed()
    }

    fun oldToNew(beforeOrEqualTo: Id?): List<A> {
        return beforeOrEqualTo?.let { oldToNew().takeWhile { it.id != beforeOrEqualTo } } ?: oldToNew()
    }

    abstract fun snapshot(beforeOrEqualTo: Id?): V
}