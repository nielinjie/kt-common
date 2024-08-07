package xyz.nietongxue.common.coordinate

import kotlinx.serialization.Serializable
import xyz.nietongxue.common.base.Name
import xyz.nietongxue.common.base.Path
import xyz.nietongxue.common.base.Selector as BaseSelector


interface Coordinate {
    val dimensions: List<Dimension>
}

interface Location {
    val values: List<Value>
    val coordinate: Coordinate
    fun sortedByDimensionsValues(): List<Value> {
        return coordinate.dimensions.map { dimension ->
            values.find { it.dimension == dimension } ?: error("dimension not found")
        }
    }

    fun validateAt(): Boolean {
        return coordinate.dimensions.all { dimension ->
            values.any { it.dimension == dimension }
        }
    }

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

@Serializable
open class PredicatesSelector(override val predicates: List<ValueBasedPredicate>) : Selector

fun Selector.and(b: Selector): Selector {
    return object : Selector {
        override val predicates: List<Predicate> = this@and.predicates + b.predicates
    }
}

interface Dimension {
    val name: Name
}

interface Value {
    val dimension: Dimension

    companion object {
        fun fromString(coordinate: Coordinate, string: String) {
            val dimensionName = string.substringBefore("_")
            val value = string.substringAfter("_")
            val dimension = coordinate.dimensions.find { it.name == dimensionName } ?: error("dimension not found")
            when (dimension) {
                is CategoryDimension -> CategoryValue(dimension, value)
                is OrderedDimension -> OrderedValue(dimension, value)
                is PathLikeDimension -> PathLikeValue(dimension, Path.fromString(value))
                is NumberDimension<*> -> error("not implemented")
            }
        }
    }
}

fun Value.valueString(): String {
    return when (this) {
        is CategoryValue -> this.d
        is OrderedValue -> this.d
        is PathLikeValue -> this.path.asString()
        is NumberValue<*> -> this.d.toString()
        else -> error("unknown value type")
    }
}


interface Predicate {
    fun test(value: Value): Boolean
    val dimension: Dimension
}

fun Predicate.selector(): Selector {
    return object : Selector {
        override val predicates: List<Predicate> = listOf(this@selector)
    }

}

fun List<Predicate>.selector(): Selector {
    return object : Selector {
        override val predicates: List<Predicate> = this@selector
    }
}

@Serializable
open class NumberDimension<T : Number>(override val name: String) : Dimension


@Serializable
open class CategoryDimension(override val name: String, val categories: List<String>) : Dimension

@Serializable
open class OrderedDimension(override val name: String, val ordered: List<String>) : Dimension

@Serializable
open class PathLikeDimension(override val name: String) : Dimension

@Serializable
open class NumberValue<T : Number>(override val dimension: NumberDimension<T>, val d: T) : Value

@Serializable
open class CategoryValue(override val dimension: CategoryDimension, val d: String) : Value

@Serializable
open class OrderedValue(override val dimension: OrderedDimension, val d: String) : Value

@Serializable
open class PathLikeValue(override val dimension: PathLikeDimension, val path: Path) : Value

@Serializable
abstract class ValueBasedPredicate : Predicate {
}

@Serializable
class CategoryEqPredicate(override val dimension: CategoryDimension, val value: String) : ValueBasedPredicate() {
    override fun test(value: Value): Boolean {
        value as CategoryValue
        return value.d == this.value
    }
}

@Serializable
class OrderedEqPredicate(override val dimension: OrderedDimension, val value: String) : ValueBasedPredicate() {
    override fun test(value: Value): Boolean {
        value as OrderedValue
        return value.d == this.value
    }
}

@Serializable
class OrderedLEPredicate(override val dimension: OrderedDimension, val value: String) : ValueBasedPredicate() {
    override fun test(value: Value): Boolean {
        value as OrderedValue
        return dimension.ordered.indexOf(value.d) <= dimension.ordered.indexOf(this.value)
    }
}


fun CategoryValue.match(f: (String, String) -> Boolean): Predicate {
    return object : Predicate {
        override fun test(value: Value): Boolean {
            value as CategoryValue
            return f(this@match.d, value.d)
        }

        override val dimension: Dimension = this@match.dimension

    }
}

fun CategoryValue.matchEq(): Predicate {
    return match { a1: String, a2: String -> a1 == a2 }
}




