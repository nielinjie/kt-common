package xyz.nietongxue.common.base

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4


typealias Id = Uuid

typealias IdGetter<I> = (I) -> Id?
typealias ParentGetter<I> = (I) -> Id?

val defaultIdNewer: () -> Id = { uuid4() }

fun id(): Id {
    return uuid4()
}

fun v7(): Id = uuid4() //TODO apply v7 uuid
