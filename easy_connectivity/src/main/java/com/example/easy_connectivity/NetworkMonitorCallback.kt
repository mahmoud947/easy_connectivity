package com.example.easy_connectivity

interface NetworkMonitorCallback {
    fun onAvailable(networkType: NetworkType)
    fun onUnAvailable()
    fun onLosing()
    fun onLost()
}