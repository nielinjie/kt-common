package xyz.nietongxue.common.base

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

object Serializing {
    private var serializersModule: SerializersModule? = null
    private var j: Json? = null
    fun plus(module: SerializersModule) {
        serializersModule = serializersModule?.let {
            it.plus(module)
            it
        } ?: module
        this.j = this.serializersModule?.let {
            Json {
                this@Json.serializersModule = it
            }
        } ?: Json
    }

    fun j(): Json = j ?: Json
}