package xyz.nietongxue.common.accession

import xyz.nietongxue.common.base.HasId
import xyz.nietongxue.common.base.Id


interface Zero<V> {
    val zero: V
}

open class Accession(
    override val id: Id,
    val base: Id,
    val reason: List<Id>,
) : HasId

fun <A:Accession> List<A>.append(accession: A): List<A> {
    if (this.isEmpty())
        return listOf(accession)
    if (accession.base == this.first().base) {
        return listOf(accession) + this
    } else {
        error("base is not match")
    }
}

abstract class AccessionBranch<V,A:Accession> {
    abstract val zero: Zero<V>
    var accessions = listOf<A>()
    fun append(accession: A) {
        this.accessions = accessions.append(accession)
    }

    abstract fun snapshot(computeTo: Id): V
}