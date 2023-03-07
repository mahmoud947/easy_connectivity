package com.example.easy_connectivity

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.net.HttpURLConnection
import java.net.URL


private const val TAG = "ConnectivityManagerNetwork"

class EasyConnectivity private constructor(
    private val connectivityManager: ConnectivityManager,
) : NetworkMonitor {
    override fun getNetworkStateFlow(
        connectionTimeOut: Int,
        url: String,
        acceptedHttpCodes: List<Int>
    ): Flow<NetworkStateV2> = callbackFlow {

        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch(Dispatchers.IO) {
                    if (isOline(connectionTimeOut, url, acceptedHttpCodes)) {
                        channel.trySend(NetworkStateV2(true, networkType()))
                    } else {
                        channel.trySend(NetworkStateV2(false, networkType()))
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                channel.trySend(NetworkStateV2(false, networkType()))
            }

            override fun onUnavailable() {
                super.onUnavailable()
                channel.trySend(NetworkStateV2(false, networkType()))
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                channel.trySend(NetworkStateV2(false, networkType()))
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

    }


    override fun callBack(callback: NetworkMonitorCallback) {

        val mCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback.onAvailable(networkType())
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                callback.onLost()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                callback.onUnAvailable()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                callback.onLosing()
            }
        }

        connectivityManager.registerDefaultNetworkCallback(mCallback)
    }

    override fun isConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    override fun networkType(): NetworkType {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return NetworkType.CELLULAR
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return NetworkType.WIFI
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return NetworkType.ETHERNET
            }
        }
        return NetworkType.NULL
    }

    @Suppress("DEPRECATION")
    private fun ConnectivityManager?.isCurrentlyConnected() = when (this) {
        null -> false
        else -> activeNetwork
            ?.let(::getNetworkCapabilities)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    private fun isOline(connectionTimeOut: Int,
                        url: String,
                        acceptedHttpCodes: List<Int>): Boolean {
        return try {
            val url = URL(url)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = connectionTimeOut
            connection.connect()
            val code = connection.responseCode
            connection.disconnect()
            code in acceptedHttpCodes
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        private var easyConnectivity: EasyConnectivity? = null
        fun getInstance(
            systemService: ConnectivityManager?,
        ): EasyConnectivity {
            if (easyConnectivity == null) {
                easyConnectivity =
                    systemService?.let { EasyConnectivity(it) }
            }
            return easyConnectivity!!
        }
    }
}
