package com.example.easy_connectivity

interface NetworkMonitorCallback {
    fun onAvailable()
    fun onUnAvailable()
    fun onLosing()
    fun onLost()
}