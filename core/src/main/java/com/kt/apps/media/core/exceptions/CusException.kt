package com.kt.apps.media.core.exceptions

class CusException(
    val errorCode: Int,
    val errorMessage: String? = null
) : Exception(errorMessage) {
}