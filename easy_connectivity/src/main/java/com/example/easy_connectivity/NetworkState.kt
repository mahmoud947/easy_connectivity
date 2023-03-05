package com.example.easy_connectivity

//enum class NetworkState {
//    AvailableWithOutInternet, UnAvailable, Losing, Lost,AvailableWithInternet
//}
sealed class NetworkState {
    data class AvailableWithInternet(
        val networkType: NetworkType
    ) : NetworkState()

    data class AvailableWithOutInternet(
        val networkType: NetworkType
    ) : NetworkState()

    object UnAvailable : NetworkState()
    object Losing : NetworkState()

    object Lost : NetworkState()
}