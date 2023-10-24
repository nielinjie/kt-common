package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RecordTest: StringSpec({
    "serializable string"{
        val record = Record("message",data = "data")
        println(record)
        val string = Json.encodeToString(record)
        println(string)
        val re2 = Json.decodeFromString<Record<String>>(string)
        record shouldBe re2
    }
    "serializable jsonWrapper"{
        val record = Record("message",data = JsonWrapper("typeInfo","jsonString"))
        println(record)
        val string = Json.encodeToString(record)
        val re2 = Json.decodeFromString<Record<JsonWrapper>>(string)

        println(string)
        record shouldBe re2
    }
})