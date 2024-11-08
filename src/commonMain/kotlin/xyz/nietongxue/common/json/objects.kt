package xyz.nietongxue.common.json

import kotlinx.serialization.json.*

operator fun JsonElement.div(path: String): JsonElement {
    return this.jsonObject[path]!!
}

inline fun <reified T : Any> T.pick(properties: List<String>): JsonElement {
    val json = Json.encodeToJsonElement(this).jsonObject
    return (buildJsonObject {
        properties.forEach {
            put(it, json[it]!!)
        }
    })
}

inline fun <reified T : Any> T.patch(
    properties: List<String>,
    jo: JsonElement,
): T {
    val json = jo.jsonObject
    val oldJson = Json.encodeToJsonElement(this).jsonObject
    return Json.decodeFromJsonElement<T>(
        buildJsonObject {
            oldJson.forEach {
                put(it.key, it.value)
            }
            properties.forEach {
                put(it, json[it]!!)
            }
        }
    )
}