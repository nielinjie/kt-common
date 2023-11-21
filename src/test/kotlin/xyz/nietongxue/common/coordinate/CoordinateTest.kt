package xyz.nietongxue.common.coordinate

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize


object XYCoordinate : Coordinate {
    override val dimensions: List<Dimension>
        get() = listOf(TwoD.X, TwoD.Y)
}

sealed interface TwoD  {
    object X : NumberDimension<Int>("x")
    object Y : NumberDimension<Int>("y")
}

object Location2D0 : Location {
    override val values = listOf(XValue(0), YValue(0))
}

object Location2D1 : Location {
    override val values = listOf(XValue(1), YValue(1))
}


class XValue(n: Int) : NumberValue<Int>(TwoD.X, n)
class YValue(n: Int) : NumberValue<Int>(TwoD.Y, n)

fun notZero(dimension: Dimension): Predicate {
    return object : Predicate {
        override fun test(value: Value): Boolean {
            return when (value.dimension) {
                is NumberDimension<*> -> (value as NumberValue<*>).d.toInt() != 0
                else -> false
            }
        }

        override val dimension: Dimension
            get() = dimension
    }
}

class CoordinateTest : StringSpec({
    "xy" {
        val no = object : Selector {
            override val predicates: List<Predicate> = listOf(notZero(TwoD.X))
        }
        no.select(listOf(Location2D0, Location2D1)).shouldHaveSize(1)
    }
    "xy more" {
        val no = object : Selector {
            override val predicates: List<Predicate> = listOf(notZero(TwoD.X))
        }
        no.select(listOf(Location2D0, Location2D1, object : Location {
            override val values = listOf(XValue(1), YValue(0))

        })).shouldHaveSize(2)
    }
})