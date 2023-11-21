package xyz.nietongxue.common.coordinate

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize


object XYCoordinate : Coordinate {
    override val dimensions: List<Dimension>
        get() = listOf(TwoD.X, TwoD.Y)
}

sealed interface TwoD : Dimension {
    data object X : TwoD
    data object Y : TwoD
}

object Location2D0 : Location<TwoD> {
    override val values = listOf(XValue(0), YValue(0))
}

object Location2D1 : Location<TwoD> {
    override val values = listOf(XValue(1), YValue(1))
}


open class NumberValue<T : Number, D : Dimension>(override val dimension: D, val d: T) : Value<D>
class XValue<T : Number>(n: T) : NumberValue<T, TwoD.X>(TwoD.X, n)
class YValue<T : Number>(n: T) : NumberValue<T, TwoD.Y>(TwoD.Y, n)

fun <D : Dimension> notZero(): Predicate<D> {
    return object : Predicate<D> {
        override fun test(value: Value<D>): Boolean {
            return when (value.dimension) {
                is TwoD -> (value as NumberValue<*, D>).d.toInt() != 0
                else -> false
            }
        }
    }
}

class CoordinateTest : StringSpec({
    "xy" {
        val no = object : Selector<TwoD> {
            override val predicates: List<Predicate<TwoD>> = listOf(notZero<TwoD.X>() as Predicate<TwoD>)
        }
        no.select(listOf(Location2D0, Location2D1)).shouldHaveSize(1)
    }
    "xy more" {
        val no = object : Selector<TwoD> {
            override val predicates: List<Predicate<TwoD>> = listOf(notZero<TwoD.X>() as Predicate<TwoD>)
        }
        no.select(listOf(Location2D0, Location2D1, object : Location<TwoD> {
            override val values = listOf(XValue(1), YValue(0))

        })).shouldHaveSize(2)
    }
})