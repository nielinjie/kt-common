package xyz.nietongxue.common.base

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import xyz.nietongxue.common.base.Serializing.j

@Serializable
data class Hash(val hex: String)

interface WithHash {
    fun getHash(): Hash
}


fun hashProperties(vararg property: Pair<String, JsonElement>): Hash {
    return hashString(property.toMap().let {
        j().encodeToJsonElement(it).let { sortJsonElementKeysDeep(it) }.toString()
    })
}

fun sortJsonElementKeysDeep(jsonElement: JsonElement): JsonElement {
    return when (jsonElement) {
        is JsonObject -> {
            val sortedKeys = jsonElement.keys.sorted()
            val sortedMap = sortedKeys.associateWith { key -> sortJsonElementKeysDeep(jsonElement[key]!!) }
            JsonObject(sortedMap)
        }

        is JsonArray -> {
            val sortedArray = jsonElement.map { sortJsonElementKeysDeep(it) }
            JsonArray(sortedArray)
        }

        else -> jsonElement
    }
}

expect fun hashString(s: String): Hash