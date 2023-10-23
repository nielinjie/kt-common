package xyz.nietongxue.common.validate

import kotlinx.serialization.Serializable
import xyz.nietongxue.common.base.JsonWrapper
import xyz.nietongxue.common.base.id

@Serializable
data class ResultRecord(
    val message: String,
    val level: Level = Level.INFO,
    val id: String = id().toString(),
    val data: JsonWrapper? = null
)

enum class Level {
    INFO, WARN, ERROR
}
