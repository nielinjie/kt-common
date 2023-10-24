package xyz.nietongxue.common.base

import kotlinx.serialization.Serializable



@Serializable
data class JsonWrapper(val typeInfo:String,val jsonString:String) {

}
