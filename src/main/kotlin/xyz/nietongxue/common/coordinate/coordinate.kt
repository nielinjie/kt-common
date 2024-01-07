package xyz.nietongxue.common.coordinate

import xyz.nietongxue.common.base.Ordered
import xyz.nietongxue.common.base.Path


interface Coordinate {
    val dimensions: List<Dimension>
}

interface Location {
    val values: List<Value>
}

interface Selector {
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
}

interface Dimension
interface Value {
    val dimension: Dimension
}

interface Predicate {
    fun test(value: Value): Boolean
    val dimension: Dimension
}


open class NumberDimension<T : Number>(val name: String) : Dimension
open class CategoryDimension<A : Any>(val name: String, val categories: List<A>) : Dimension
open class OrderedDimension<A : Any>(val name: String, val ordered: Ordered<A>) : Dimension
open class PathLikeDimension(val name: String) : Dimension

open class NumberValue<T : Number>(override val dimension: NumberDimension<T>, val d: T) : Value
open class CategoryValue<A : Any>(override val dimension: CategoryDimension<A>, val d: String) : Value
open class PathLikeValue(override val dimension: PathLikeDimension, val path: Path) : Value