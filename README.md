# Easy Connectivity

---
[![](https://jitpack.io/v/mahmoud947/easy_connectivity.svg)](https://jitpack.io/#mahmoud947/easy_connectivity)

## from an android developers who hate boilerplate code

## Step 1. Add the JitPack repository to your build file

### gradle

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
   ...
   maven { url 'https://jitpack.io' }
  }
 }
```

## Step 2. Add the dependency

```gradle
dependencies {
         implementation 'com.github.mahmoud947:easy_connectivity:1.0.0'
 }
```

## Basic Usage

- ### Basic check

---
  **NOTE**

## add ACCESS_NETWORK_STATE permission

```xml
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ```

  ```kotlin
  class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var easyConnectivity: EasyConnectivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

          easyConnectivity =
            EasyConnectivity.getInstance(systemService = applicationContext.getSystemService())

        if (easyConnectivity.isConnected()){
            Toast.makeText(this,"connected",Toast.LENGTH_SHORT).show()
        }

    }
}
```

- ### with Flow

---
**NOTE**

## add internet permission

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

## add ACCESS_NETWORK_STATE permission

```xml
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ```

---

```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var easyConnectivity: EasyConnectivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

         easyConnectivity =
            EasyConnectivity.getInstance(
                systemService = applicationContext.getSystemService(),
                url = "https://www.google.com/",
                acceptedHttpCodes = listOf(200),
                connectionTimeOut = 200)

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
```

- ### with callBack

**NOTE**

## add ACCESS_NETWORK_STATE permission

```xml
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ```

---

```kotlin
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
```
