package xyz.nietongxue.common.coordinate

import xyz.nietongxue.common.base.Ordered
import xyz.nietongxue.common.base.Path
import xyz.nietongxue.common.base.Selector as BaseSelector


interface Coordinate {
    val dimensions: List<Dimension>
}

interface Location {
    val values: List<Value>
}

interface Selector : BaseSelector<Location, Any> {
    val predicates: List<Predicate>
    fun select(points: List<Location>): List<Location> {
        return points.filter { point ->
            predicates.all { predicate ->
                val thisDimensionValue = point.values.find { it.dimension == predicate.dimension }
                if (thisDimensionValue == null) {
                    error("dimension not found")
                } else {
                    predicate.test(thisDimensionValue)
                }
            }
        }
    }

    override fun select(input: List<Location>, context: Any): List<Location> {
        return select(input)
    }
}

fun Selector.and(b: Selector): Selector {
    return object : Selector {
        override val predicates: List<Predicate> = this@and.predicates + b.predicates
    }
}

interface Dimension
interface Value {
    val dimension: Dimension
}

interface Predicate {
    fun test(value: Value): Boolean
    val dimension: Dimension
}

fun Predicate.selector():Selector{
    return object : Selector {
        override val predicates: List<Predicate> = listOf(this@selector)
    }

}
fun List<Predicate>.selector(): Selector {
    return object : Selector {
        override val predicates: List<Predicate> = this@selector
    }
}

open class NumberDimension<T : Number>(val name: String) : Dimension
open class CategoryDimension<A : Any>(val name: String, val categories: List<A>) : Dimension
open class OrderedDimension<A : Any>(val name: String, val ordered: Ordered<A>) : Dimension
open class PathLikeDimension(val name: String) : Dimension

open class NumberValue<T : Number>(override val dimension: NumberDimension<T>, val d: T) : Value
open class CategoryValue<A : Any>(override val dimension: CategoryDimension<A>, val d: A) : Value
open class PathLikeValue(override val dimension: PathLikeDimension, val path: Path) : Value


fun <A : Any> CategoryValue<A>.match(f: (A, A) -> Boolean): Predicate {
    return object : Predicate {
        override fun test(value: Value): Boolean {
            value as CategoryValue<A>
            return f(this@match.d, value.d)
        }

        override val dimension: Dimension = this@match.dimension

    }
}

fun <A : Any> CategoryValue<A>.matchEq(): Predicate {
    return match { a1: A, a2: A -> a1 == a2 }
}