package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RecordTest: StringSpec({
    //FIXME, error context is in message objects, test serialization of that
    "serializable string"{
        val record = Record("message")
        println(record)
        val string = Json.encodeToString(record)
        println(string)
        val re2 = Json.decodeFromString<Record<String>>(string)
        record shouldBe re2
    }
    "serializable jsonWrapper"{
        val record = Record("message")
        println(record)
        val string = Json.encodeToString(record)
        val re2 = Json.decodeFromString<Record<String>>(string)

        println(string)
        record shouldBe re2
    }
})