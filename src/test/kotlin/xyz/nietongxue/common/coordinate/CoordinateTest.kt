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
    override val values: Map<Dimension, Value>
        get() = mapOf(X to XValue(0), Y to YValue(0))
}

object Point2D1 : Point {
    override val values: Map<Dimension, Value>
        get() = mapOf(X to XValue(1), Y to YValue(1))
}

class XValue(val d: Int) : Value {

}

class YValue(val d: Int) : Value {

}

//TODO 如何实现只匹配X
// object notZeroX: Predicate<X, XValue> {
object notZero : Predicate<Value> {
    override fun test(value: Value): Boolean {
        return when (value) {
            is XValue -> value.d != 0
            else -> error("not support")
        }

    }
}

class CoordinateTest : StringSpec({
    "xy" {
        val no = object : Selector {
            override val predicates: Map<Dimension, Predicate<Value>>
                get() = mapOf(X to notZero)
        }
        no.select(listOf(Point2D0, Point2D1)).shouldHaveSize(1)
    }
    "xy more" {
        val no = object : Selector {
            override val predicates: Map<Dimension, Predicate<Value>>
                get() = mapOf(X to notZero)
        }
        no.select(listOf(Point2D0, Point2D1, object : Point {
            override val values: Map<Dimension, Value>
                get() = mapOf(X to XValue(1), Y to YValue(0))

        })).shouldHaveSize(2)
    }
})