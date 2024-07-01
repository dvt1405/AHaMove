package com.kt.apps.media.core.datasource

import kotlinx.coroutines.flow.StateFlow

interface INetworkCheckDataSource {
    fun checkIsNetworkOnline(): Boolean

    suspend fun networkState(): StateFlow<Boolean>
}