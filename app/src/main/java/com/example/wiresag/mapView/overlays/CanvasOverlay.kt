package com.example.wiresag.mapView.overlays

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import android.view.MotionEvent
import com.example.wiresag.math.PointF
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class CanvasOverlay(
    val onDraw: DrawScope.() -> Unit,
    private val onSingleTapConfirmed: MapViewMotionEvent? = null,
    private val onLongPress: MapViewMotionEvent?
) : Overlay() {

    override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView) =
        onSingleTapConfirmed?.invoke(MapViewMotionEventScope(e, mapView.projection))
            ?: super.onSingleTapConfirmed(e, mapView)

    override fun onLongPress(e: MotionEvent, mapView: MapView) =
        onLongPress?.invoke(MapViewMotionEventScope(e, mapView.projection))
            ?: super.onLongPress(e, mapView)

    override fun draw(canvas: Canvas, pj: Projection) {
        onDraw(DrawScope.ScopeImpl(canvas, pj))
    }

    fun evaluateDependencies() {
        onDraw(DrawScope.NoOpScope())
    }

    sealed class DrawScope(val canvas: Canvas) {
        fun IGeoPoint.toPixelF(): PointF = toPixel().run { PointF(x, y) }
        fun Iterable<IGeoPoint>.toPixels() = map { it.toPixel() }
        fun Iterable<IGeoPoint>.toPixelsF() = map { it.toPixelF() }

        abstract fun IGeoPoint.toPixel(): Point

        internal class ScopeImpl(canvas: Canvas, private val projection: Projection) :
            DrawScope(canvas) {
            override fun IGeoPoint.toPixel(): Point = projection.toPixels(this, Point())
        }

        internal class NoOpScope : DrawScope(NoOpCanvas()) {
            override fun IGeoPoint.toPixel() = Point(0, 0)
        }
    }
}

