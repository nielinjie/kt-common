package xyz.nietongxue.common.processing

import arrow.core.left
import arrow.core.nel
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ProcessingTest : StringSpec({
    "simple" {
        val one = bind<String, String, Double>(1.0)
        val added = one.flatMap {
            bind(it + 1.0)
        }
        added.shouldBe(bind(2.0))
    }
    "logs" {
        val one = bind<String, String, Double>(1.0)
        val added = one.flatMap {
            bind<String, String, Double>(it + 1.0).withLog("added 1")
        }
        added.let {
            it.first shouldBe listOf("added 1")
            it.second shouldBe 2.0.right()
        }
    }
    "errors" {
        val one = bind<String, String, Double>(1.0)
        val added = one.flatMap {
            bind<String, String, Double>(it + 1.0).withLog("added 1")
        }
        val re = added.flatMap {
            listOf("can not add negative") to ("can not add negative").nel().left()
        }
        re.let {
            it.first shouldBe listOf("added 1", "can not add negative")
            it.second shouldBe ("can not add negative").nel().left()
        }
    }
    "errors2" {
        val one = bind<String, String, Double>(1.0)

        val re = one.flatMap {
//            listOf("can not add negative") to listOf("can not add negative").nel().left()
            errorAndLog("can not add negative", "can not add negative")
        }
        val added = re.flatMap {
            resultAndLog(it + 1.0, "added 1")
        }
        added.let {
            it.first shouldBe listOf("can not add negative")
            it.second shouldBe ("can not add negative").nel().left()
        }
    }
    "variance"{
        open class Log(val msg:String)
        class LogUpper(msg:String):Log("upper - $msg")
        val one = bind<LogUpper, String, Double>(1.0)
        val added = one.flatMap {
            bind(it + 1.0)
        }
        val added2=  added.flatMap {
            bind<Log, String, Double>(it + 1.0).withLog(Log("added 1 again"))
        }
        added2.let {
            it.first .also {
                it.shouldHaveSize(1)
                it.first().msg shouldBe "added 1 again"
            }
            it.second shouldBe 3.0.right()
        }
    }
    "keep log"{
        data class Log(val msg:String)
        data class LogUpper(val log:Log)
        val one = bind<Log, String, Double>(1.0)
        val added = one.flatMap {
            bind(it + 1.0)
        }
        val added2=  added.keepLog{
            LogUpper(it)
        }.flatMap {
            bind<LogUpper, String, Double>(it + 1.0).withLog(LogUpper(Log("added 1 again")))
        }
        added2.let {
            it.first .shouldBe(listOf(LogUpper(Log("added 1 again"))))
            it.second shouldBe 3.0.right()
        }
    }
})