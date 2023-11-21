package xyz.nietongxue.common.coordinate


interface Coordinate {
    val dimensions: List<Dimension>
}

interface Point {
    val values: Map<Dimension, Value>
}

interface Selector {
    val predicates: Map<Dimension, Predicate<Value>>
    fun select(points: List<Point>): List<Point> {
        return points.filter { point ->
            predicates.all { (dimension, predicate) ->
                predicate.test(point.values[dimension]!!)
            }
        }
    }
}

interface Dimension
interface Value
interface Predicate< V : Value> {
    fun test(value: V): Boolean
}

