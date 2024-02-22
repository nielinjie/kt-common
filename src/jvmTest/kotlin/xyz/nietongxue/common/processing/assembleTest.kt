package xyz.nietongxue.common.processing

import io.kotest.core.spec.style.StringSpec

class AssembleTest : StringSpec({
    "assemble" {
        val assemble = Assemble<String, String>("1", emptyList())
        val re = assemble.apply { it ->
            bind((it + "2"))
        }
        re.finish().shouldSuccess()
    }
    "assemble in scope" {
        val assemble = Assemble<String, String>("1", emptyList())
        val re = assemble.applyInScope {
            ((it + "2")).done()
        }
        re.finish().shouldSuccess()
    }

    "assemble in scope with error" {
        val assemble = Assemble<String, String>("1", emptyList())
        val re = assemble.applyInScope {
            tryRun({StopResult(it.message!!)}) {
                error("error")
            }
        }
        re.finish().shouldErr(("error"))
    }
})