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
         implementation 'com.github.mahmoud947:easy_connectivity:1.0.0-beta4'
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


        easyConnectivity = EasyConnectivity.Builder(context = this)
            .build()


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

        easyConnectivity = EasyConnectivity.Builder(context = this)
            .setUrl("https://www.google.com/")
            .setAcceptedHttpCodes(listOf(200))
            .setConnectionTimeOut(2000)
            .build()

        lifecycleScope.launchWhenStarted {

            easyConnectivity.networkState.collect {
                when (it) {
                    NetworkState.AvailableWithInternet -> {
                        // connected without internet
                    }

                    NetworkState.AvailableWithOutInternet -> {
                        // connected with internet
                    }
                    NetworkState.UnAvailable -> {
                        // not connected
                    }
                    NetworkState.Losing -> {
                        // Losing connect
                    }

                    NetworkState.Lost -> {
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
```
