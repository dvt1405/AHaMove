package com.kt.apps.media.core.storage.keyvalue

interface IKeyValueStorage {

    fun setString(key: String, value: String)

    fun getString(key: String, defValue: String): String

    fun getFloat(key: String, defValue: Float): Float

    fun setFloat(key: String, value: Float)

    fun getInt(key: String, defValue: Int): Int

    fun setInt(key: String, value: Int)

    fun getBoolean(key: String): Boolean

    fun setBoolean(key: String, value: Boolean)

    fun getLong(key: String, defValue: Long): Long

    fun setLong(key: String, value: Long)

    fun deleteKey(key: String)

    fun <T : Any> get(key: String, defValue: T? = null, type: Class<T>): T?

    fun <T : Any> save(key: String, value: T, type: Class<T>)

}