package com.example.easyconnectivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.easy_connectivity.EasyConnectivity
import com.example.easyconnectivity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var easyConnectivity: EasyConnectivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        easyConnectivity = EasyConnectivity.getInstance(applicationContext.getSystemService())

        if (easyConnectivity.isConnected()) {
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launchWhenStarted {
            easyConnectivity.getNetworkStateFlow().collect {
                val networkType = it.networkType.name
                /* WIFI,
                 CELLULAR,
                 ETHERNET,
                 NULL */

                val connectionState = it.connectionState.name
                /* Available,
                   AvailableWithOutInternet,
                   Lost,
                   Unavailable,
                   Losing
                 */
            }
        }
    }
}