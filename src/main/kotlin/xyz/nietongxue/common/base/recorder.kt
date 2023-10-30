package xyz.nietongxue.common.base

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class Record<M>(
    val message: M,
    val level: LogLevel = LogLevel.INFO,
    val id: Id = v7(),
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
)
