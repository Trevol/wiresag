package com.example.sensors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sensors.ui.theme.SensorsTheme
import kotlin.random.Random


class MainActivityOldPermissions : ComponentActivity() {

    private val permissions =
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private val permissionsRequestCode = Random.nextInt(0, 10000)
    private fun hasPermissions(context: Context) = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private var permissionsGranted by mutableStateOf(false)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionsRequestCode && hasPermissions(this)) {
            permissionsGranted = true
            onAppReady()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions(this)) {
            permissionsGranted = true
            onAppReady()
        } else {
            ActivityCompat.requestPermissions(
                this, permissions, permissionsRequestCode
            )
        }

        setContent {
            SensorsTheme {
                if (permissionsGranted) {
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        Column {
                            Text("Hello!")
                        }
                    }
                } else {
                    NoPermissions()
                }
            }
        }
    }

    private fun onAppReady() {
        //start consume location events
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}

@Composable
private fun NoPermissions() = Text(text = "No permissions!")

@Preview(showBackground = true)
@Composable
fun MainActivityDefaultPreview() {
    SensorsTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            Column {
                Text(text = "Hello!")
            }
        }
    }
}