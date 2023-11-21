package xyz.nietongxue.common.coordinate

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize


object XYCoordinate : Coordinate {
    override val dimensions: List<Dimension>
        get() = listOf(X, Y)
}

object X : Dimension
object Y : Dimension
object Point2D0 : Point {
    override val values: Map<Dimension, Value<Dimension>>
        get() = mapOf(X to XValue(0), Y to YValue(0))
}

object Point2D1 : Point {
    override val values: Map<Dimension, Value<*>>
        get() = mapOf(X to XValue(1), Y to YValue(1))
}

fun <D : Dimension> point(vararg values: Pair<D, Value<D>>): Point {
    return object : Point {
        override val values: Map<Dimension, Value<*>>
            get() = mapOf(*values)
    }
}

class XValue(val d: Int) : Value<X> {

}

class YValue(val d: Int) : Value<Y> {

}


object xNotZero : Predicate<X> {
    override fun test(value: Value<X>): Boolean {
        return when (value) {
            is XValue -> value.d != 0
            else -> error("not support")
        }

    }
}

class CoordinateTest : StringSpec({
    "xy" {
        val no = object : Selector {
            override val predicates: Map<Dimension, Predicate<Dimension>>
                //TODO 如何消除这个警告？
                get() = mapOf(X to xNotZero as Predicate<Dimension>)
        }
        no.select(listOf(Point2D0, Point2D1)).shouldHaveSize(1)
    }
    "xy more" {
        val no = object : Selector {
            override val predicates: Map<Dimension, Predicate<Dimension>>
                get() = mapOf(X to xNotZero as Predicate<Dimension>)
        }
        no.select(listOf(Point2D0, Point2D1, object : Point {
            override val values: Map<Dimension, Value<*>>
                get() = mapOf(X to XValue(1), Y to YValue(0))

        })).shouldHaveSize(2)
    }
})