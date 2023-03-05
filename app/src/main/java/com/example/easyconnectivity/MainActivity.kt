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


        easyConnectivity = EasyConnectivity.getInstance(applicationContext.getSystemService())


        if (easyConnectivity.isConnected()) {
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launchWhenStarted {
            easyConnectivity.networkState.collect {
                when (it) {
                    is NetworkState.AvailableWithInternet -> {
                        Toast.makeText(this@MainActivity, it.networkType.name, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkState.AvailableWithOutInternet -> {
                        Toast.makeText(this@MainActivity, it.networkType.name, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkState.UnAvailable -> {
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkState.Lost -> {
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkState.Losing -> {
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

    }
}