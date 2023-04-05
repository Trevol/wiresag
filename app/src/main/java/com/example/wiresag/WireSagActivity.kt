package com.example.wiresag

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import com.example.wiresag.activity.FullScreenActivity
import com.example.wiresag.camera.CameraPhotoRequest
import com.example.wiresag.osmdroid.StubLocationProvider
import com.example.wiresag.storage.image.FileImageStorage
import com.example.wiresag.ui.Main
import com.example.wiresag.ui.NoPermissions
import com.example.wiresag.utils.PermissionsRequest
import com.example.wiresag.viewModel.WireSagViewModel
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import java.io.File

class WireSagActivity : FullScreenActivity(keepScreenOn = true) {

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

    private lateinit var services: Services

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        services = Services(this)

        setContent { Text("Проверка разрешений...") }

        permissionsRequest().launch { granted ->
            setContent {
                if (granted) {
                    val viewModel = remember { services.viewModel() }
                    Main { viewModel.View() }
                } else {
                    NoPermissions()
                }
            }
        }

    }

    private class Services(val context: ComponentActivity) {
        val imagesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: File(context.filesDir, "Pictures").also { it.mkdirs() }

        val photoRequest = CameraPhotoRequest(
            context,
            File(imagesDirectory, "photoRequestTmp").apply { mkdirs() },
            "wiresag_authority"
        )

        private fun locationProvider(): IMyLocationProvider {
            /*val locationProvider = GpsMyLocationProvider(context)
                .apply {
                    locationUpdateMinDistance = 0.001f
                    locationUpdateMinTime = 500
                }*/

            /*val locationProvider = DebugLocationProvider(
                //initialLocation = null,
                //latDelta = 0.0,
                initialDelay = 1000,
                locationUpdateTime = 1000
            )*/

            val locationProvider = StubLocationProvider()

            return locationProvider
        }


        fun viewModel() = WireSagViewModel(
            context,
            locationProvider(),
            photoRequest,
            FileImageStorage(
                storageDirectory = File(imagesDirectory, "spans").apply { mkdirs() }
            )
        )
    }
}
