package xyz.nietongxue.common.persistence

import com.eygraber.uri.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import xyz.nietongxue.common.base.IdGetter

expect fun writeToPath(path: Uri, content: String)
expect fun readFromPath(path: Uri): String?
interface Lock {
    fun lock()
    fun unlock()
}

expect fun createLock(): Lock

open class ListPersistence<T>(val file: Uri, val serializer: KSerializer<T>) {
    val list = mutableListOf<T>()
    private val lock = createLock()
    private fun save() {
        val jsonArray = JsonArray(list.map { Json.encodeToJsonElement(serializer, it) })
        writeToPath(file, jsonArray.toString())
    }

    fun load() {
        readFromPath(file)?.let {
            val jsonArray = Json.parseToJsonElement(it).jsonArray
            jsonArray.map {
                Json.decodeFromJsonElement(serializer, it)
            }.let {
                list.addAll(it)
            }
        }
    }

    fun set(list: List<T>) {
        withList {
            it.clear()
            it.addAll(list)
        }
    }

    fun update(list: List<T>, idGetter: IdGetter<T>) {
        withList {
            list.forEach { item ->
                val index = it.indexOfFirst { idGetter(it) == idGetter(item) }
                if (index == -1) {
                    it.add(item)
                } else {
                    it[index] = item
                }
            }
        }
    }

    fun remove(item: T) {
        withList { it.remove(item) }
    }

    fun add(item: T) {
        withList { it.add(item) }
    }

    fun withList(block: (MutableList<T>) -> Unit) {
        lock.lock()
        block(list)
        save()
        lock.unlock()
    }

}

