package xyz.nietongxue.common.processing

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import xyz.nietongxue.common.log.Log
import xyz.nietongxue.common.validate.ValidateResult

fun <V,M> Processing<Log<M>, ValidateResult<M>, V>.shouldSuccess(): V {
    return this.second.shouldBeRight()
}

fun <V,M> Processing<Log<M>, ValidateResult<M>, V>.shouldErr(message:M): ValidateResult<M> {
    return this.second.shouldBeLeft().first().also {
        it.message shouldBe message
    }
}