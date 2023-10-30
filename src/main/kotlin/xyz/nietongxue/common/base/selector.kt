package xyz.nietongxue.common.base

interface Selector<I, C> {
    fun select(input: List<I>, context: C): List<I>
}

fun <I, C> Selector<I, C>.pipe(selector: Selector<I, C>): Selector<I, C> {
    return object : Selector<I, C> {
        override fun select(input: List<I>, context: C): List<I> {
            return selector.select(this@pipe.select(input, context), context)
        }
    }
}