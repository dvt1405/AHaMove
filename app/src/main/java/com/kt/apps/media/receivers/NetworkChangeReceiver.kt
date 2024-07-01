package com.kt.apps.media.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class DaggerBroadcastReceiver : BroadcastReceiver() {

    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
    }
}

@AndroidEntryPoint
class NetworkChangeReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var networkDataSource: INetworkCheckDataSource

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        networkDataSource.checkIsNetworkOnline()
    }
}