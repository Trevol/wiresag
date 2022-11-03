package com.example.wiresag

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wiresag.ui.Main
import com.example.wiresag.ui.NoPermissions
import com.example.wiresag.ui.theme.WireSagTheme
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.PermissionsRequest
import com.example.wiresag.utils.prettyFormat

class WireSagActivity : ComponentActivity() {
    private fun permissionsRequest() = PermissionsRequest(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    private lateinit var locationManager: LocationManager

    var gpsEnabled by mutableStateOf(false)
    var location by mutableStateOf(null as Location?)

    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(newLocation: Location) {
            location = newLocation
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.d("INFO-", "onStatusChanged: $provider $status $extras")
        }

        override fun onProviderEnabled(provider: String) {
            gpsEnabled = true
        }

        override fun onProviderDisabled(provider: String) {
            gpsEnabled = false
        }
    }

    private var started by mutableStateOf(false)

    private fun stopProcessLocation() {
        if (!started) {
            return
        }
        locationManager.removeUpdates(gpsLocationListener)
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        started = false
    }

    @SuppressLint("MissingPermission")
    private fun startProcessLocation() {
        if (started) {
            return
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            0F,
            gpsLocationListener
        )
        started = true
    }

    private fun createWireSagViewModel(): WireSagViewModel {
        return WireSagViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setContent { Text("Проверка разрешений...") }

        permissionsRequest().launch { granted ->
            setContent {
                if (granted) {
                    Main { createWireSagViewModel().View() }
                } else {
                    NoPermissions()
                }
            }
        }

        /*setContent {
            Main {
                if (permissionsGranted) {
                    if (gpsEnabled) {
                        if (started) {
                            val location = location
                            Column {
                                Button(onClick = ::stopProcessLocation) {
                                    Text("Stop")
                                }
                                if (location != null) {

                                    Row {
                                        Text("Lat: ${DMS(location.latitude).prettyFormat()}")
                                        Text(" (${location.latitude})")
                                    }
                                    Row {
                                        Text("Lon: ${DMS(location.longitude).prettyFormat()}")
                                        Text(" (${location.longitude})")
                                    }

                                    Text("Alt: ${location.altitude}")
                                    Text("Accuracy: ${location.accuracy}")
                                } else {
                                    Text("Finding location...")
                                }
                            }

                        } else {
                            Button(onClick = ::startProcessLocation) {
                                Text("Start")
                            }
                        }

                    } else {
                        Text("Gps is disabled")
                    }


                }

            }

        }*/
    }


    @SuppressLint("MissingPermission")
    private fun onAppReady() {
        //start consume location events
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
