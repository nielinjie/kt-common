package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import kotlinx.datetime.Clock
import kotlinx.uuid.UUIDExperimentalAPI
import kotlinx.uuid.UUIDv7

@OptIn(UUIDExperimentalAPI::class)
class IdTest : StringSpec({
    "id bug" {
        repeat(10) {
            Thread.sleep(2)
            val long = Clock.System.now().toEpochMilliseconds()
            val id = UUIDv7(long)
            println(id)

        }
    }
})