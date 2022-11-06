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
import androidx.compose.runtime.*
import com.example.wiresag.osmdroid.DummyLocationProvider
import com.example.wiresag.ui.Main
import com.example.wiresag.ui.NoPermissions
import com.example.wiresag.ui.theme.WireSagTheme
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.PermissionsRequest
import com.example.wiresag.utils.prettyFormat
import com.example.wiresag.viewModel.WireSagViewModel
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

class WireSagActivity : ComponentActivity() {
    private fun permissionsRequest() = PermissionsRequest(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    private fun createWireSagViewModel(): WireSagViewModel {
        /*val locationProvider = GpsMyLocationProvider(applicationContext)
            .apply {
                locationUpdateMinDistance = 0f
                locationUpdateMinTime = 1000
            }*/
        val locationProvider = DummyLocationProvider(initialLocation = null)

        return WireSagViewModel(applicationContext, locationProvider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent { Text("Проверка разрешений...") }

        permissionsRequest().launch { granted ->
            setContent {
                if (granted) {
                    val viewModel = remember { createWireSagViewModel() }
                    Main { viewModel.View() }
                } else {
                    NoPermissions()
                }
            }
        }

    }
}
