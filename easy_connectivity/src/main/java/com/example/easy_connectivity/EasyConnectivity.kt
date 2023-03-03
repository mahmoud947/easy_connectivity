package com.example.easy_connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import android.os.AsyncTask
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.distinctUntilChanged
import java.net.HttpURLConnection
import java.net.URL


private const val TAG = "ConnectivityManagerNetw"

class EasyConnectivity private constructor(
    private val context: Context,
    private val connectionTimeOut: Int,
    private val url: String,
    private val acceptedHttpCodes: List<Int>
) : NetworkMonitor {

    override val networkState: Flow<NetworkState> = callbackFlow<NetworkState> {

        val connectivityManager = context.getSystemService<ConnectivityManager>()

        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch(Dispatchers.IO) {
                    if (isOline()) {
                        channel.trySend(NetworkState.AvailableWithInternet)
                    } else {
                        channel.trySend(NetworkState.AvailableWithOutInternet)
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


        connectivityManager?.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }

    }.distinctUntilChanged()

    override fun callBack(callback: NetworkMonitorCallback) {
        val connectivityManager = context.getSystemService<ConnectivityManager>()

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


        connectivityManager?.registerDefaultNetworkCallback(mCallback)
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
            code in this.acceptedHttpCodes

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    class Builder(private val context: Context) {

        private var connectionTimeOut: Int = 1000
        private var url: String = "https://www.google.com/"
        private var acceptedHttpCodes: List<Int> = listOf(200)

        fun setConnectionTimeOut(connectionTimeOut: Int) =
            apply { this.connectionTimeOut = connectionTimeOut }

        fun setUrl(url: String) = apply { this.url = url }

        fun setAcceptedHttpCodes(acceptedHttpCodes: List<Int>) =
            apply { this.acceptedHttpCodes = acceptedHttpCodes }

        fun build() =
            EasyConnectivity(
                context = context,
                connectionTimeOut = connectionTimeOut,
                url = url,
                acceptedHttpCodes = acceptedHttpCodes
            )
    }


}
