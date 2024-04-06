package xyz.nietongxue.common.base

import com.appmattus.crypto.Algorithm

actual fun hashString(s: String): Hash {
    return hashBytes(s.encodeToByteArray())
}

@OptIn(ExperimentalStdlibApi::class)
fun hashBytes(bytes: ByteArray): Hash {
    val digest = Algorithm.MD5.createDigest()
    return digest.digest(bytes).toHexString().let {
        Hash(it)
    }
}