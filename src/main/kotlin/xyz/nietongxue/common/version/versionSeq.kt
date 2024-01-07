package xyz.nietongxue.common.version

import xyz.nietongxue.common.base.HasId
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.common.base.SingleStream


interface SingleBaseVersion : HasId {
    val base: Id
}


class VersionSingleStream(val versions: Collection<SingleBaseVersion>) :
    SingleStream<SingleBaseVersion> {
    override fun base(a: SingleBaseVersion): SingleBaseVersion {
        return versions.find { it.id == a.base }!!
    }

    override fun end(): SingleBaseVersion {
        val bases = versions.map { it.base }
        return versions.find { it.id !in bases }!!
    }

}


