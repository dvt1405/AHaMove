package com.kt.apps.media.core.models

sealed class DataState<T> {
    class Loading<T> : DataState<T>()

    data class Success<T>(val data: T) : DataState<T>()

    data class Error<T>(val throwable: Throwable) : DataState<T>()
}