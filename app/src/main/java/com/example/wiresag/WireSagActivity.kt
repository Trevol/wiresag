package com.example.wiresag

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.wiresag.camera.CameraPhotoRequest
import com.example.wiresag.osmdroid.DummyLocationProvider
import com.example.wiresag.ui.Main
import com.example.wiresag.ui.NoPermissions
import com.example.wiresag.utils.PermissionsRequest
import com.example.wiresag.viewModel.WireSagViewModel
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import java.io.File

class WireSagActivity : ComponentActivity() {
    private fun permissionsRequest() = PermissionsRequest(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        // Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    )

    private fun createWireSagViewModel(): WireSagViewModel {
        /*val locationProvider = GpsMyLocationProvider(applicationContext)
            .apply {
                locationUpdateMinDistance = 1f
                locationUpdateMinTime = 500
            }*/

        val locationProvider = DummyLocationProvider(
            //initialLocation = null,
            //latDelta = 0.0,
            initialDelay = 1000,
            locationUpdateTime = 1000
        )

        return WireSagViewModel(applicationContext, locationProvider)
    }

    var picture by mutableStateOf<Bitmap?>(null)
    lateinit var photoRequest: CameraPhotoRequest

    private fun takePhoto() {
        photoRequest.takePhoto {
            if (it != null) {
                picture = it
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoRequest = CameraPhotoRequest(
            this,
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: File(filesDir, "Pictures")
                .also { it.mkdirs() },
            "sample_authority"
        )
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
