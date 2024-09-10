package xyz.nietongxue.common.persistence

import com.eygraber.uri.toUri
import jakarta.annotation.PostConstruct
import kotlinx.serialization.KSerializer
import xyz.nietongxue.common.base.Path
import java.nio.file.Path as FPath


open class PersistentList<T>(file: FPath, serializer: KSerializer<T>) :
    ListPersistence<T>(file.toUri().toUri(), serializer) {
    @PostConstruct
    fun onLoad() {
        load()
    }
}


class FileTreePersistentList(val mapping: PathMapToFileTree) {
    private val persistentMap = mutableMapOf<Path, PersistentList<*>>()


    fun <T> getOrCreate(path: Path, serializer: KSerializer<T>): PersistentList<T> {
        @Suppress("UNCHECKED_CAST")
        return persistentMap.getOrPut(path) {
            PersistentList(mapping.getFPath(path), serializer).also {
                it.load()
            }
        } as PersistentList<T>

    }

    fun mappingPath(renterName: String) = Path(listOf(renterName))


}



