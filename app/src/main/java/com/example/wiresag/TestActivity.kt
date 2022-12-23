package com.example.wiresag

import android.Manifest
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.preference.PreferenceManager
import com.example.wiresag.mapView.CanvasOverlay
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.osmdroid.enableRotationGesture
import com.example.wiresag.osmdroid.enableScaleBar
import com.example.wiresag.ui.Main
import com.example.wiresag.ui.NoPermissions
import com.example.wiresag.utils.PermissionsRequest
import com.example.wiresag.utils.addTo
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class TestActivity : ComponentActivity() {
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

    val state = State()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent { Text("Проверка разрешений...") }

        permissionsRequest().launch { granted ->
            setContent {
                if (granted) {
                    Main {
                        Box(Modifier.fillMaxSize()) {
                            Button(onClick = { state.addPoint() }, modifier = Modifier.zIndex(1f)) {
                                Text("Add ${state.points.size}")
                            }
                            MapView(
                                modifier = Modifier.fillMaxSize(),
                                onInitMapView = ::initMapView,
                                onUpdateMapView = ::updateMapView
                            )
                        }
                    }
                } else {
                    NoPermissions()
                }
            }
        }
    }

    private fun initMapView(mapView: MapView) {
        mapView.apply {
            setMultiTouchControls(true)
            enableScaleBar()
            enableRotationGesture()

            CanvasOverlay {
                val paint = Paint().apply { color = Color.MAGENTA }
                state.points.forEachIndexed { i, geoPt ->
                    val pt = geoPt.toPixel()
                    canvas.drawCircle(pt.x.toFloat(), pt.y.toFloat(), 5f, paint)
                    if (i > 0) {
                        val prevPt = state.points[i - 1].toPixel()
                        canvas.drawLine(
                            pt.x.toFloat(),
                            pt.y.toFloat(),
                            prevPt.x.toFloat(),
                            prevPt.y.toFloat(),
                            paint
                        )
                    }
                }
            }.addTo(overlays)

            controller.animateTo(state.points.first(), 19.5, null)
        }
    }

    private fun updateMapView(mapView: MapView) {
        mapView.overlays.filterIsInstance<CanvasOverlay>()
            .first()
            .evaluateDrawDependencies()
        mapView.postInvalidate()
    }


    class State {
        val points = mutableStateListOf(GeoPoint(45.0, 35.0))
        fun addPoint() {
            val last = points.last()
            points.add(GeoPoint(last.latitude + .00002, last.longitude + .00003))
        }
    }
}