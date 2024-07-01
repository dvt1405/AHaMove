package com.kt.apps.media.core.storage.keyvalue

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class KeyValueStorageImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : IKeyValueStorage {
    private val _sharePref by lazy {
        context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    override fun setString(key: String, value: String) {
        _sharePref.edit()
            .putString(key, value)
            .apply()
    }

    override fun getString(key: String, defValue: String): String {
        return _sharePref.getString(key, defValue) ?: defValue
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return _sharePref.getFloat(key, defValue)
    }

    override fun setFloat(key: String, value: Float) {
        _sharePref.edit()
            .putFloat(key, value)
            .apply()
    }

    override fun getInt(key: String, defValue: Int): Int {
        return _sharePref.getInt(key, defValue)
    }

    override fun setInt(key: String, value: Int) {
        _sharePref.edit()
            .putInt(key, value)
            .apply()
    }

    override fun getBoolean(key: String): Boolean {
        return _sharePref.getBoolean(key, false)
    }

    override fun setBoolean(key: String, value: Boolean) {
        _sharePref.edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getLong(key: String, defValue: Long): Long {
        if (!_sharePref.contains(key)) {
            return defValue
        }
        return _sharePref.getLong(key, defValue)
    }

    override fun setLong(key: String, value: Long) {
        _sharePref.edit()
            .putLong(key, value)
            .apply()
    }

    override fun deleteKey(key: String) {
        _sharePref.edit()
            .remove(key)
            .apply()
    }

    override fun <T : Any> save(key: String, value: T, type: Class<T>) {
        val jsonStr = Gson().toJson(value, type)
        _sharePref.edit()
            .putString(key, jsonStr)
            .apply()
    }

    override fun <T : Any> get(key: String, defValue: T?, type: Class<T>): T? {
        val result = kotlin.runCatching {
            val jsonStr = _sharePref.getString(key, null)
            Gson().fromJson(jsonStr, type)
        }
        return result.getOrNull() ?: defValue
    }

    companion object {
        const val SHARED_PREF_NAME = "default_shared_pref"
    }
}