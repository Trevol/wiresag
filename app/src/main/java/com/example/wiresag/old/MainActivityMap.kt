package com.example.wiresag.old

import android.Manifest
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import androidx.preference.PreferenceManager
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.ui.Main
import com.example.wiresag.utils.PermissionsRequest
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay

class MainActivityMap : ComponentActivity() {

    private val permissionsRequest = PermissionsRequest(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        val pt = GeoPoint(45.0, 35.5)
        val items = mutableListOf(OverlayItem("", "", pt))
        // map.controller.animateTo(pt)

        val locationOverlay = SimpleLocationOverlay(Bitmaps.location)

        locationOverlay.setLocation(GeoPoint(45.2, 35.1))

        val itemTapListener = object : OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(
                index: Int,
                item: OverlayItem?
            ): Boolean {
                return true
            }

            override fun onItemLongPress(
                index: Int,
                item: OverlayItem?
            ): Boolean {
                return true
            }

        }
        val objectsOverlay =
            ItemizedIconOverlay(items, Bitmaps.pylonDrawable, itemTapListener, baseContext)

        var newPt = pt
        var mapInstance: MapView? = null

        fun newMarker() {
            locationOverlay.setLocation(GeoPoint(45.7, 34.8))

            newPt = GeoPoint(newPt.latitude + .05, newPt.longitude + .05)
            objectsOverlay.addItem(OverlayItem("", "", newPt))
            mapInstance!!.postInvalidate()
        }

        fun clearMarkers() {
            objectsOverlay.removeAllItems()
            newPt = pt
            mapInstance!!.postInvalidate()
        }

        fun onMapInit(map: MapView) {
            mapInstance = map
            map.setMultiTouchControls(true)
            map.overlays.add(locationOverlay)

            /*map.enableScaleBar()
                .enableMinimap()*/

            map.overlays.add(objectsOverlay)

            map.controller.setZoom(9.0)
            map.controller.setCenter(pt)
        }

        permissionsRequest.launch { granted ->
            setContent {
                Main {
                    if (granted) {
                        Box() {
                            MapView(modifier = Modifier.fillMaxSize(), ::onMapInit)

                            Row(
                                modifier = Modifier.padding(start = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                Button(onClick = { newMarker() }) {
                                    Text("О")
                                }
                                Button(onClick = { clearMarkers() }) {
                                    Text("Ф")
                                }
                            }

                        }

                    } else {
                        Text("No device permissions")
                    }
                }
            }
        }
    }


    private object Bitmaps {
        val pylonDrawable: Drawable =
            circleBitmap(Color.RED).toDrawable(Resources.getSystem())
        val location = circleBitmap(Color.GREEN)
        val direction = circleBitmap(Color.BLACK)

        private fun circleBitmap(circleColor: Int): Bitmap {
            val bmp = Bitmap.createBitmap(31, 31, Bitmap.Config.ARGB_8888)
            val cnv = Canvas(bmp)
            cnv.drawCircle(
                15f,
                15f,
                10f,
                Paint().apply {
                    style = Paint.Style.STROKE
                    strokeWidth = 6f
                    this.color = circleColor
                })
            return bmp
        }
    }
}
