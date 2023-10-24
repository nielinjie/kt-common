package xyz.nietongxue.common.base

import kotlinx.datetime.Clock
import kotlinx.uuid.UUID
import kotlinx.uuid.UUIDExperimentalAPI
import kotlinx.uuid.UUIDv7


typealias Id = String //使用string作为对外类型，uuid只在新建时有意义。

typealias IdGetter<I> = (I) -> Id?
typealias ParentGetter<I> = (I) -> Id?

val defaultIdNewer: () -> Id = { id() }

fun id(): Id = UUID().toString()

@OptIn(UUIDExperimentalAPI::class)
fun v7(): Id = UUIDv7(Clock.System.now().toEpochMilliseconds()).toString() // apply v7 uuid
