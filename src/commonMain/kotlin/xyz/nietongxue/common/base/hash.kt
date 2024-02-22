package xyz.nietongxue.common.base

import kotlinx.serialization.Serializable

@Serializable
data class Hash(val hex: String)

interface WithHash {
    fun getHash(): Hash
}

