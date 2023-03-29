package com.example.wiresag.mapView

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import android.view.MotionEvent
import androidx.core.graphics.toPointF
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

typealias OverlayMotionEvent = (OverlayMotionEventScope) -> Boolean

class CanvasOverlay(
    val onDraw: DrawScope.() -> Unit,
    private val onSingleTapConfirmed: OverlayMotionEvent? = null
) : Overlay() {

    class DrawScope(val canvas: Canvas, val projection: Projection) {
        fun IGeoPoint.toPixel(): Point = projection.toPixels(this, Point())
        fun IGeoPoint.toPixelF(): PointF = toPixel().run { PointF(x, y) }

        fun Iterable<IGeoPoint>.toPixels() = map { it.toPixel() }
        fun Iterable<IGeoPoint>.toPixelsF() = map { it.toPixelF() }

        companion object {
            private inline fun PointF(x: Int, y: Int) = PointF(x.toFloat(), y.toFloat())
        }
    }

    override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView) =
        if (onSingleTapConfirmed != null) {
            onSingleTapConfirmed.invoke(OverlayMotionEventScope(e, mapView.projection))
        } else {
            super.onSingleTapConfirmed(e, mapView)
        }

    override fun draw(canvas: Canvas, pj: Projection) {
        onDraw(DrawScope(canvas, pj))
    }

    fun evaluateDrawDependencies() = draw(Canvas(), mockProjection())

    companion object {
        private fun mockProjection() =
            Projection(0.0, 1, 1, GeoPoint(0.0, 0.0), 0f, true, true, 0, 0)
    }
}

class OverlayMotionEventScope(val motionEvent: MotionEvent, private val projection: Projection) {
    val geoPoint = projection.fromPixels(motionEvent.x.toInt(), motionEvent.y.toInt())
    val eventPixel = PointF(motionEvent.x, motionEvent.y)

    fun toPixelF(point: IGeoPoint) = projection.toPixels(point, Point()).toPointF()
}
