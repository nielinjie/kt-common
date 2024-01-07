package xyz.nietongxue.common.base


data class Path(val parts: List<Name>) {
    companion object {
        val zero = Path(emptyList())
        fun fromString(path: String) = Path(path.split("/").filter { it.isNotBlank() })
    }

    fun append(part: Name) = this.append(
        fromString(part)
    )

    fun append(path: Path) = Path(this.parts + path.parts.filter { it.isNotBlank() })
    fun asString() = parts.joinToString("/")
    fun isChildOf(b: Path): Boolean {
        return (this.parts.size == b.parts.size + 1) && this.parts.subList(0, b.parts.size) == b.parts
    }
    fun isDescendantOf(b:Path):Boolean{
        return (this.parts.size > b.parts.size) && this.parts.subList(0, b.parts.size) == b.parts
    }

    fun parent() = Path(this.parts.subList(0, this.parts.size - 1))
    fun shortName(): String = parts.last()
    fun packageAndName() = PackageAndName(this)
}
class PackageAndName(val path: Path){
    val name = path.shortName()
    val packageName = path.parent()
}
