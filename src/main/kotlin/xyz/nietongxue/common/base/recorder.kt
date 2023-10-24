package xyz.nietongxue.common.base

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable




@Serializable
data class Record<D>(
    val message: String,
    val level: LogLevel = LogLevel.INFO,
    val id: Id = v7(),
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val data: D? = null

)
