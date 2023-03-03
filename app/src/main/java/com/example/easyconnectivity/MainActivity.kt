package com.example.easyconnectivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.easy_connectivity.EasyConnectivity
import com.example.easy_connectivity.NetworkMonitorCallback
import com.example.easyconnectivity.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var networkMonitor: EasyConnectivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        networkMonitor = EasyConnectivity.Builder(context = this)
            .setAcceptedHttpCodes(listOf(200))
            .setConnectionTimeOut(1000)
            .build()

//        lifecycleScope.launchWhenStarted {
//
//            networkMonitor.networkState.collect{
//                Snackbar.make(this@MainActivity,binding.root,"Network status is ${it.name}",Snackbar.LENGTH_SHORT).show()
//            }
//        }
//
        networkMonitor.callBack(object :NetworkMonitorCallback{
            override fun onAvailable() {
                Snackbar.make(this@MainActivity,binding.root,"Network status is onAvailable",Snackbar.LENGTH_SHORT).show()
            }

            override fun onUnAvailable() {
                Snackbar.make(this@MainActivity,binding.root,"Network status is onUnAvailable",Snackbar.LENGTH_SHORT).show()
            }

            override fun onLosing() {
                Snackbar.make(this@MainActivity,binding.root,"Network status is onLosing",Snackbar.LENGTH_SHORT).show()
            }

            override fun onLost() {
                Snackbar.make(this@MainActivity,binding.root,"Network status is onLost}",Snackbar.LENGTH_SHORT).show()
            }


        })

    }
}