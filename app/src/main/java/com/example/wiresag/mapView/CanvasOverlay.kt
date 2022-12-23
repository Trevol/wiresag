package com.example.wiresag.mapView

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class CanvasOverlay(val onDraw: DrawScope.() -> Unit) : Overlay() {

    class DrawScope(val canvas: Canvas, val projection: Projection) {
        fun IGeoPoint.toPixel(): Point = projection.toPixels(this, Point())
        fun IGeoPoint.toPixelF(): PointF = toPixel().run { PointF(x, y) }

        fun Iterable<IGeoPoint>.toPixels() = map { it.toPixel() }
        fun Iterable<IGeoPoint>.toPixelsF() = map { it.toPixelF() }

        companion object {
            private inline fun PointF(x: Int, y: Int) = PointF(x.toFloat(), y.toFloat())
        }
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
