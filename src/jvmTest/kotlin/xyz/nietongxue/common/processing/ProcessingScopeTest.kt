package xyz.nietongxue.common.processing

import arrow.core.left
import arrow.core.nel
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProcessingScopeTest : StringSpec({

    "scope form" {
        val one = bind<String, String, Double>(1.0)
        val added = processing<String, String, Double> {
            log("added 1")
            re((one.second.getOrNull() ?: 1.0) + 1.0)
        }
        added.current.let {
            it.first shouldBe listOf("added 1")
            it.second shouldBe 2.0.right()
        }
    }
    "scope form2" {
        val one = bind<String, String, Double>(1.0)
        val added = processing<String, String, Double> {
            log("add 1")
            error("can not add negative")
        }
        added.current.let {
            it.first shouldBe listOf("add 1")
            it.second shouldBe ("can not add negative").nel().left()
        }
    }
    "scope form 3" {
        val one = bind<String, String, Double>(1.0)
        val added = processing<String, String, Double> {
            log("add 1")
            error("can not add negative")
        }
        added.current.let {
            it.first shouldBe listOf("add 1")
            it.second shouldBe ("can not add negative").nel().left()
        }
    }
    "4" {
        val one = bind<String, String, Double>(1.0)
        val added = processing<String, String, Double> {
            one.bind()
            log("added 1")
            value(
                { "can not add negative" }, { (it + 1.0).done() })
        }
        added.current.let {
            it.first shouldBe listOf("added 1")
            it.second shouldBe 2.0.right()
        }
    }
    "with throwing"{
        val one = bind<String, String, Double>(1.0)
        fun stupidAdd(a:Double,b:Double):Double{
            error("no clear enough")
        }
        val added = processing<String, String, Double> {
            tryRun({it.message }) {
                one.bind()
                log("added 1")
                stupidAdd(1.0,1.0).done()
            }
        }
        added.current.let {
            it.first shouldBe listOf("added 1")
            it.second shouldBe (("no clear enough").nel()).left()
        }
    }
})