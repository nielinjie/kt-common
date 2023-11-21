package xyz.nietongxue.common.coordinate


interface Coordinate {
    val dimensions: List<Dimension>
}

interface Location<D : Dimension> {
    val values: List<Value<D>>
}

interface Selector<D : Dimension> {
    val predicates: List<Predicate<D>>
    fun select(points: List<Location<D>>): List<Location<D>> {
        return points.filter { point ->
            predicates.all { predicate ->
                point.values.any { value ->
                    //TODO 这里好像是错的，没有考虑维度的限制。
                    predicate.test(value)
                }
            }
        }
    }
}

interface Dimension
interface Value<out D : Dimension> {
    val dimension: D
}

interface Predicate<D : Dimension> {
    fun test(value: Value<D>): Boolean
}

