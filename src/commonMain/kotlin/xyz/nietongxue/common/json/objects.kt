package xyz.nietongxue.common.json

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

operator fun JsonElement.div(path: String): JsonElement {
    return this.jsonObject[path]!!
}

