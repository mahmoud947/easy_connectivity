
package com.example.easy_connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface NetworkMonitor {
//    val isOnline: Flow<Boolean>
    val networkState:Flow<NetworkState>
    fun callBack(callback: NetworkMonitorCallback)

    fun isConnected():Boolean

}
