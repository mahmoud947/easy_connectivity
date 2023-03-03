package com.example.easyconnectivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.easy_connectivity.EasyConnectivity
import com.example.easy_connectivity.NetworkMonitorCallback
import com.example.easyconnectivity.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var easyConnectivity: EasyConnectivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        easyConnectivity = EasyConnectivity.Builder(context = this)
            .setAcceptedHttpCodes(listOf(200))
            .build()

        easyConnectivity.callBack(object : NetworkMonitorCallback {
            override fun onAvailable() {
                // connected
            }

            override fun onUnAvailable() {
                // not connected
            }

            override fun onLosing() {
                // Losing connect
            }

            override fun onLost() {
                // lost connect
            }
        })
    }
}