package com.example.easyconnectivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.easy_connectivity.EasyConnectivity
import com.example.easy_connectivity.NetworkState
import com.example.easyconnectivity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var easyConnectivity: EasyConnectivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        easyConnectivity =
            EasyConnectivity.getInstance(
                systemService = applicationContext.getSystemService()
            )

        if (easyConnectivity.isConnected()) {
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launchWhenStarted {
            easyConnectivity.networkState.collect {
                when (it) {
                    is NetworkState.AvailableWithInternet -> {
                        // connected without internet
                        val networkType = it.networkType.name
                        /*
                        WIFI,
                        MOBILE,
                        NON
                         */
                    }
                    is NetworkState.AvailableWithOutInternet -> {
                        // connected with internet
                    }
                    is NetworkState.UnAvailable -> {
                        // not connected
                    }
                    is NetworkState.Losing -> {
                        // Losing connect
                    }
                    is NetworkState.Lost -> {
                        // lost connect
                    }
                }
            }
        }
    }
}