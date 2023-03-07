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
            EasyConnectivity.getInstance(applicationContext.getSystemService())

        if (easyConnectivity.isConnected()) {
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show()
        }

    }
}
```

- ### with Flow

---
**NOTE**

## add internet permission

```xml

<uses-permission android:name="android.permission.INTERNET" />
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
            EasyConnectivity.getInstance(applicationContext.getSystemService())

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
