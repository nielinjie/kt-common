package xyz.nietongxue.common.base

import kotlin.math.max

interface Matcher<T, R> {
    fun match(value: T): List<MatchResult<R>>
}


fun <T> List<MatchResult<T>>.discussed(): List<MatchResult<T>> {
    val groups = this.groupBy { it.value }
    return groups.map { it.value.merged() }.filterNotNull().sortedByDescending { it.score }
}

fun <T> List<MatchResult<T>>.merged(): MatchResult<T>? {
    if (this.map { it.value }.distinct().size > 1) error("values are not the same")
    if (this.any { it.score.value < 0.0 }) return null
    return this.maxByOrNull { it.score }
}


fun <T> MatchResult<T>?.add(b: MatchResult<T>?): MatchResult<T>? {
    return this?.let { a ->
        b?.let { b ->
            require(a.value == b.value)
            if (a.score.value > 0 && b.score.value > 0) {
                MatchResult(a.value, Score(max(a.score.value, b.score.value)), a.reason + b.reason)
            } else null
        }
    }
}

data class MatchResult<R>(val value: R, val score: Score, val reason: String, val additional: Any? = null)
data class Score(val value: Float) : Comparable<Score> {
    constructor(value: Double) : this(value.toFloat())
    constructor(value: Int) : this(value.toFloat())

    init {
        require(value in 0.0..1.0)
    }

    override fun compareTo(other: Score): Int {
        return value.compareTo(other.value)
    }
}