package xyz.nietongxue.common.coordinate


interface Coordinate {
    val dimensions: List<Dimension>
}

interface Point {
    val values: Map<Dimension, Value<Dimension>>
}

interface Selector {
    val predicates: Map<Dimension, Predicate<Dimension>>
    fun select(points: List<Point>): List<Point> {
        return points.filter { point ->
            predicates.all { (dimension, predicate) ->
                predicate.test(point.values[dimension] as Value<Dimension>)
            }
        }
    }
}

interface Dimension
interface Value<out D:Dimension>
interface Predicate<in  D : Dimension> {
    fun test(value: Value<D>): Boolean
}

