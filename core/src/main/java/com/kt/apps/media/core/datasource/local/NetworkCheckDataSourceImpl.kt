package com.kt.apps.media.core.datasource.local

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import com.kt.apps.media.core.di.qualifiers.CoroutineDispatcherType
import com.kt.apps.media.core.di.qualifiers.CoroutineScopeQualifier
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.internal.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.stateIn
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
        MutableSharedFlow<Boolean>(1)
    }

    init {
        checkNetwork()
        checkIsNetworkOnline()
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

                override fun onLost(network: Network) {
                    super.onLost(network)
                    scope.launch {
                        checkIsNetworkOnline()
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    scope.launch {
                        checkIsNetworkOnline()
                    }
                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties
                ) {
                    super.onLinkPropertiesChanged(network, linkProperties)
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
        var isConnected = false
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork)
        if (networkCapabilities != null) {
            isConnected = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        scope.launch {
            _networkState.emit(isConnected)
        }
        return isConnected
    }

    override suspend fun networkState(): StateFlow<Boolean> {
        return _networkState.stateIn(scope)
    }
}