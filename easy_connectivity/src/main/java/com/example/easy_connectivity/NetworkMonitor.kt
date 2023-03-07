package com.example.easy_connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface NetworkMonitor {
    fun getNetworkStateFlow(connectionTimeOut: Int = 1000,
                            url: String = "https://www.google.com/",
                            acceptedHttpCodes: List<Int> = listOf(200)): Flow<NetworkState>


    fun callBack(callback: NetworkMonitorCallback)

    fun isConnected(): Boolean

    fun networkType(): NetworkType
}
