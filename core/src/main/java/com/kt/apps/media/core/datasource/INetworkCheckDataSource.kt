package com.kt.apps.media.core.datasource

import kotlinx.coroutines.flow.StateFlow

interface INetworkCheckDataSource {
    fun checkIsNetworkOnline(): Boolean

    fun networkState(): StateFlow<Boolean>
}