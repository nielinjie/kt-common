package xyz.nietongxue.common.persistence

import com.eygraber.uri.Uri
import com.eygraber.uri.toURI
import java.nio.file.Files
import java.util.concurrent.locks.ReentrantLock
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

actual fun writeToPath(path: Uri, content: String) {
    val file = Path(path.toURI().path)
    Files.createDirectories(file.parent)
    file.writeText(content)
}

actual fun readFromPath(path: Uri): String? {
    val file = (Path(path.toURI().path))
    if (!Files.exists(file)) {
        return null
    }
    return file.readText()
}


actual fun createLock(): Lock {
    return object : Lock {
        val l = ReentrantLock()
        override fun lock() {
            l.lock()
        }

        override fun unlock() {
            l.unlock()
        }
    }
}