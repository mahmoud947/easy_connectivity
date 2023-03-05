package com.example.easy_connectivity

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.net.HttpURLConnection
import java.net.URL


private const val TAG = "ConnectivityManagerNetwork"

class EasyConnectivity private constructor(
    private val connectivityManager: ConnectivityManager,
    private val connectionTimeOut: Int,
    private val url: String,
    private val acceptedHttpCodes: List<Int>
) : NetworkMonitor {

    override val networkState: Flow<NetworkState> = callbackFlow {


        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch(Dispatchers.IO) {
                    if (isOline()) {
                        channel.trySend(NetworkState.AvailableWithInternet(networkType()))
                    } else {
                        channel.trySend(NetworkState.AvailableWithOutInternet(networkType()))
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                channel.trySend(NetworkState.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                channel.trySend(NetworkState.UnAvailable)

            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                channel.trySend(NetworkState.Losing)
            }

        }


        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

    }.distinctUntilChanged()

    override fun callBack(callback: NetworkMonitorCallback) {

        val mCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback.onAvailable()
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
        else -> when {
            VERSION.SDK_INT >= VERSION_CODES.M ->
                activeNetwork
                    ?.let(::getNetworkCapabilities)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    ?: false
            else -> activeNetworkInfo?.isConnected ?: false
        }
    }

    private fun isOline(): Boolean {
        return try {
            val url: URL = URL(this.url)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = this.connectionTimeOut
            connection.connect()
            val code = connection.responseCode
            connection.disconnect()
            code in this.acceptedHttpCodes
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        private var easyConnectivity: EasyConnectivity? = null
        fun getInstance(
            context: ConnectivityManager?,
            connectionTimeOut: Int = 1000,
            url: String = "https://www.google.com/",
            acceptedHttpCodes: List<Int> = listOf(200)
        ): EasyConnectivity {
            if (easyConnectivity == null) {
                easyConnectivity =
                    context?.let { EasyConnectivity(it, connectionTimeOut, url, acceptedHttpCodes) }
            }
            return easyConnectivity!!
        }
    }
}
