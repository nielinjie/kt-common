package xyz.nietongxue.common.base

import net.pearx.kasechange.CaseFormat
import net.pearx.kasechange.toCase


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
        val half = (n - 3) / 2
        this.take(half) + "..." + this.takeLast(half)
    } else this
}

fun String.atMostStart(n: Int): String {
    return if (this.length > n) {
        "..." + this.takeLast(n)
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

fun String.wrapBy(s: String, s2: String = s.paired() ?: error("can not auto paired")): String {
    return s + this + s2
}

fun String.paired(): String? {
    return when (this) {
        "<" -> ">"
        "\"" -> "\""
        "{" -> "}"
        "[" -> "]"
        "(" -> ")"
        "<<" -> ">>"
        "'" -> "'"
        "（" -> "）"
        "【" -> "】"
        "《" -> "》"
        else -> null
    }
}

fun String.ensureEnd(end: String = "\n"): String {
    return if (this.endsWith(end)) this else this + end
}

infix fun String.n(b: String): String {
    return this + "\n" + b
}

infix fun String.nn(b: String): String {
    return this + "\n\n" + b
}

infix fun String.t(b: String): String {
    return this + "\t" + b
}

infix fun String.d(b: String): String {
    return "$this - $b"
}

infix fun String.d(any: Any): String {
    return this d any.toString()
}

val List<String>.n: String
    get() {
        return this.joinToString("\n")
    }
val List<String>.nn: String
    get() {
        return this.joinToString("\n\n")
    }

fun String.cList(): List<String> {
//    val delimiters = listOf("、", "，", " ", ",") //TODO 考虑混合情况
    val delimiters = Regex("[、，,\\s]+")
    val results = this.split(delimiters).map { it.trim() }
    return results.filter { it.isNotEmpty() }
}
