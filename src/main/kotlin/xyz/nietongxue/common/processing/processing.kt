package xyz.nietongxue.common.processing

import arrow.core.*




typealias Processing<L, E, A> = Pair<List<L>, EitherNel<E, A>>
typealias ProcessingSuc<L,A> = Pair<List<L>,Either.Right<A>>

fun <L, E, A> bind(a: A): Processing<L, E, A> = emptyList<L>() to a.right()

fun <L, E, A> Processing<L, E, A>.flatMap(fn: (a: A) -> Processing<out L, E, A>): Processing<L, E, A> {
    val (logs, either) = this
    return when (either) {
        is Either.Left -> logs to either
        is Either.Right -> {
            val (logs2, either2) = fn(either.value)
            logs + logs2 to either2
        }
    }
}

fun <L, E, A, L1> Processing<L, E, A>.keepLog(fn: (l: L) -> L1): Processing<L1, E, A> {
    val (logs, either) = this
    return logs.map(fn) to either
}

fun <L, E, A, E2> Processing<L, E, A>.mapError(fn: (E) -> E2): Processing<L, E2, A> {
    val (logs, either) = this
    return logs to either.mapLeft {
        it.map(fn)
    }
}
fun <L,E,A,L2,E2> Processing<L,E,A> .toNextStep(fl:(L)->L2,fe:(E)->E2):Processing<L2,E2,A>{
    return this.keepLog(fl).mapError(fe)
}

fun <L, E, A> Processing<L, E, A>.withLog(log: L): Processing<L, E, A> {
    val (logs, either) = this
    return logs + log to either
}

fun <L, E, A> resultAndLog(a: A, log: L): Processing<L, E, A> = listOf(log) to a.right()
fun <L, E, A> errorAndLog(e: E, log: L): Processing<L, E, A> = listOf(log) to (e).nel().left()

class ProcessingScope<L, E, A>() {
    var current = bind<L, E, A>(null as A)
    fun log(log: L): Unit {
        this.current = current.withLog(log)
    }

    fun error(e: E): Unit {
        this.current = current.flatMap { emptyList<L>() to e.nel().left() }
    }

    fun re(a: A): Unit {
        this.current = current.flatMap {
            emptyList<L>() to a.right()
        }
    }

    fun value(onError: () -> E, onValue: (A) -> Unit): Unit {
        when (this.current.second) {
            is Either.Left -> onError().raise()
            is Either.Right -> onValue((this.current.second as Either.Right<A>).value)
        }
    }

    fun E.raise(): Unit {
        this@ProcessingScope.error(this)
    }

    fun A.done(): Unit {
        this@ProcessingScope.re(this)
    }

    fun Processing<L, E, A>.bind(): Unit {
        this@ProcessingScope.current = current.flatMap { this }
    }

    fun done(): Processing<L, E, A> {
        return this.current
    }
}

fun <L, E, A> processing(fn: ProcessingScope<L, E, A>.() -> Unit): ProcessingScope<L, E, A> {
    val re = ProcessingScope<L, E, A>()
    re.also(fn)
    return re
}