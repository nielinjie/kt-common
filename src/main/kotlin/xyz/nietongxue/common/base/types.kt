package xyz.nietongxue.common.base

interface Type<V> {
    val name: Path
    fun instanceOf(value:V):Boolean
}
interface Types<V>{
    val types:List<Type<V>>
}
