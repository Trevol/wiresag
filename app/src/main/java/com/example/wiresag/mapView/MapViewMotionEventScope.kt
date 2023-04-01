package com.example.wiresag.mapView

import android.graphics.Point
import android.graphics.PointF
import android.view.MotionEvent
import androidx.core.graphics.toPointF
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.Projection

typealias MapViewMotionEvent = (MapViewMotionEventScope) -> Boolean

class MapViewMotionEventScope(val motionEvent: MotionEvent, private val projection: Projection) {
    val geoPoint = projection.fromPixels(motionEvent.x.toInt(), motionEvent.y.toInt())
    val eventPixel = PointF(motionEvent.x, motionEvent.y)
    fun toPixelF(point: IGeoPoint) = projection.toPixels(point, Point()).toPointF()
}