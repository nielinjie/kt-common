package xyz.nietongxue.common.base

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import kotlinx.serialization.Serializable

val modules = listOf<Module>(JavaTimeModule())
val jsonMapper = jsonMapper { modules.forEach { addModule(it) } }


@Serializable
data class JsonWrapper(val typeInfo:String,val jsonString:String) {

}

fun JsonWrapper.decodeValue(): Any {
    return when (this.typeInfo) {
        "String" -> this.jsonString
        "Number" -> this.jsonString.toDouble()
        "Boolean" -> this.jsonString.toBoolean()
        else -> jsonMapper.readValue(this.jsonString, Class.forName(this.typeInfo))
    }
}

fun JsonWrapper.Companion.from(value: Any): JsonWrapper {
    return when (value) {
        is String -> JsonWrapper("String", value)
        is Number -> JsonWrapper("Number", value.toString())
        is Boolean -> JsonWrapper("Boolean", value.toString())
        else -> JsonWrapper(value::class.java.name, jsonMapper.writeValueAsString(value))
    }
}