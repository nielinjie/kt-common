package xyz.nietongxue.common.base

import io.github.encryptorcode.pluralize.Pluralize
import net.pearx.kasechange.CaseFormat
import net.pearx.kasechange.toCase


fun String.pluralize(count: Int = 5): String {
    return Pluralize.pluralize(this, count)
}

fun String.singular(): String {
    return Pluralize.pluralize(this, 1)
}

fun String.capitalizedCamel(): String {
    return this.toCase(CaseFormat.CAPITALIZED_CAMEL)
}

fun String.camel(): String {
    return this.toCase(CaseFormat.CAMEL)
}
fun String.lowerUnderscore(): String {
    return this.toCase(CaseFormat.LOWER_UNDERSCORE)
}

fun String.minusAtEnd(s: String): String {
    return this.substring(0, this.length - s.length)
}
fun String.byMinus(): String? {
    return if (this.endsWith("-")) {
        this.substring(0, this.length - 1)
    } else null
}
fun String.minusAtStart(s: String): String {
    return this.substring(s.length, this.length)
}

fun String.endBy(s: String): String? {
    return if (this.endsWith(s))
        this.minusAtEnd(s)
    else null
}

fun String.atMost(n: Int): String {
    return if (this.length > n) {
        this.take(n) + "..."
    } else this
}
fun String.atMostMiddle(n: Int): String {
    return if (this.length > n) {
        val half = (n-3) / 2
        this.take(half) + "..." + this.takeLast(half)
    } else this
}

fun String.startBy(s: String): String? {
    return if (this.startsWith(s))
        this.minusAtStart(s)
    else null
}

fun String.aroundBy(s: String, s2: String = s.paired() ?: error("can not auto paired")): String? {
    return if (this.startsWith(s) && this.endsWith(s2))
        this.minusAtStart(s).minusAtEnd(s2)
    else null
}

fun String.paired(): String? {
    return when (this) {
        "<" -> ">"
        "\"" -> "\""
        "{" -> "}"
        "[" -> "]"
        "(" -> ")"
        "<<"-> ">>"
        else -> null
    }
}
fun String.ensureEnd(end:String ="\n"):String{
    return if(this.endsWith(end)) this else this+end
}