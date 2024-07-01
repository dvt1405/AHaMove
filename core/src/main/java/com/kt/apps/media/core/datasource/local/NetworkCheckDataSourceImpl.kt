package com.kt.apps.media.core.datasource.local

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import com.kt.apps.media.core.di.qualifiers.CoroutineDispatcherType
import com.kt.apps.media.core.di.qualifiers.CoroutineScopeQualifier
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.internal.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkCheckDataSourceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    @CoroutineScopeQualifier(CoroutineDispatcherType.IO)
    private val _scope: Provider<CoroutineScope>
) : INetworkCheckDataSource {
    private val scope by lazy {
        _scope.get()
    }

    private val _networkState by lazy {
        MutableStateFlow(false)
    }

    init {
        checkNetwork()
    }

    private fun checkNetwork() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerNetworkCallback(networkRequest, object : NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    scope.launch {
                        checkIsNetworkOnline()
                    }
                }
            })
        }
    }

    override fun checkIsNetworkOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        val isConnected = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        scope.launch {
            _networkState.emit(isConnected)
        }
        return isConnected
    }

    override fun networkState(): StateFlow<Boolean> {
        return _networkState.asStateFlow()
    }
}