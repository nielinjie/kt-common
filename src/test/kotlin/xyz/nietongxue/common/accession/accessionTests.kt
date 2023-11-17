package xyz.nietongxue.common.accession

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import xyz.nietongxue.common.base.id
import xyz.nietongxue.common.processing.Action
import xyz.nietongxue.common.processing.Reducer
import xyz.nietongxue.common.processing.bind

private val zero = object : Zero<String> {
    override val zero: String = ""
}

class Simple : StringSpec({
    class AppendStringAction(val adding: String) : Action<String, String> {
        override fun action(): Reducer<String, String> {
            return {
                bind(it + adding)
            }
        }

    }
    "string" {
        val branch = ActionAccessionBranch<String, String>(zero)
        branch.append(ActionAccession(listOf(AppendStringAction("hello")), id(), null, emptyList()))
        val s = branch.snapshot(null)
        s shouldBe "hello"
    }
    "more action" {
        val branch = ActionAccessionBranch<String, String>(zero)
        branch.append(
            ActionAccession(
                listOf(AppendStringAction("hello"), AppendStringAction(" foo")),
                id(),
                null,
                emptyList()
            )
        )
        val s = branch.snapshot(null)
        s shouldBe "hello foo"
    }
    "more accession" {
        val branch = ActionAccessionBranch<String, String>(zero)
        val id = branch.append(ActionAccession(listOf(AppendStringAction("hello")), id(), null, emptyList()))
        branch.append(ActionAccession(listOf(AppendStringAction(" foo")), id(), id, emptyList()))
        val s = branch.snapshot(null)
        s shouldBe "hello foo"
    }
})