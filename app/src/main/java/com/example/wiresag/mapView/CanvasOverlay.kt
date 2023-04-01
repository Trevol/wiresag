package com.example.wiresag.mapView

import android.graphics.*
import android.graphics.fonts.Font
import android.graphics.text.MeasuredText
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

            private class NoOpCanvas : Canvas() {
                override fun isHardwareAccelerated() = false
                override fun setBitmap(bitmap: Bitmap?) = Unit
                override fun enableZ() = Unit
                override fun disableZ() = Unit
                override fun isOpaque() = false
                override fun getWidth() = 0
                override fun getHeight() = 0
                override fun getDensity() = 0
                override fun setDensity(density: Int) = Unit
                override fun getMaximumBitmapWidth() = 0
                override fun getMaximumBitmapHeight() = 0
                override fun save() = 0

                @Deprecated("")
                override fun saveLayer(bounds: RectF?, paint: Paint?, saveFlags: Int) = 0
                override fun saveLayer(bounds: RectF?, paint: Paint?) = 0

                @Deprecated("")
                override fun saveLayer(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    paint: Paint?,
                    saveFlags: Int
                ) = 0

                override fun saveLayer(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    paint: Paint?
                ) = 0

                @Deprecated("")
                override fun saveLayerAlpha(bounds: RectF?, alpha: Int, saveFlags: Int) = 0

                override fun saveLayerAlpha(bounds: RectF?, alpha: Int) = 0

                @Deprecated("")
                override fun saveLayerAlpha(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    alpha: Int,
                    saveFlags: Int
                ) = 0

                override fun saveLayerAlpha(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    alpha: Int
                ) = 0

                override fun restore() = Unit

                override fun getSaveCount() = 0

                override fun restoreToCount(saveCount: Int) = Unit

                override fun translate(dx: Float, dy: Float) = Unit

                override fun scale(sx: Float, sy: Float) = Unit

                /*override fun scale(sx: Float, sy: Float, px: Float, py: Float) {
                    throw RuntimeException("Stub!")
                }*/

                override fun rotate(degrees: Float) = Unit

                /*override fun rotate(degrees: Float, px: Float, py: Float) {
                    throw RuntimeException("Stub!")
                }*/

                override fun skew(sx: Float, sy: Float) = Unit

                override fun concat(matrix: Matrix?) = Unit

                override fun setMatrix(matrix: Matrix?) = Unit

                @Deprecated("")
                override fun getMatrix(ctm: Matrix) = Unit

                /*@Deprecated("")
                override fun getMatrix(): Matrix {
                    throw RuntimeException("Stub!")
                }*/

                @Deprecated("")
                override fun clipRect(rect: RectF, op: Region.Op) = false

                @Deprecated("")
                override fun clipRect(rect: Rect, op: Region.Op) = false

                override fun clipRect(rect: RectF) = false

                override fun clipOutRect(rect: RectF) = false

                override fun clipRect(rect: Rect) = false

                override fun clipOutRect(rect: Rect) = false

                @Deprecated("")
                override fun clipRect(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    op: Region.Op
                ) = false

                override fun clipRect(left: Float, top: Float, right: Float, bottom: Float) = false

                override fun clipOutRect(left: Float, top: Float, right: Float, bottom: Float) =
                    false

                override fun clipRect(left: Int, top: Int, right: Int, bottom: Int) = false

                override fun clipOutRect(left: Int, top: Int, right: Int, bottom: Int) = false

                @Deprecated("")
                override fun clipPath(path: Path, op: Region.Op) = false

                override fun clipPath(path: Path) = false

                override fun clipOutPath(path: Path) = false

                override fun getDrawFilter(): DrawFilter? {
                    throw RuntimeException("Stub!")
                }

                override fun setDrawFilter(filter: DrawFilter?) = Unit

                @Deprecated("")
                override fun quickReject(rect: RectF, type: EdgeType) = false

                override fun quickReject(rect: RectF) = false

                @Deprecated("")
                override fun quickReject(path: Path, type: EdgeType) = false

                override fun quickReject(path: Path) = false

                @Deprecated("")
                override fun quickReject(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    type: EdgeType
                ) = false

                override fun quickReject(left: Float, top: Float, right: Float, bottom: Float) =
                    false

                override fun getClipBounds(bounds: Rect) = false

                /*override fun getClipBounds(): Rect {
                    throw RuntimeException("Stub!")
                }*/

                override fun drawPicture(picture: Picture) = Unit

                override fun drawPicture(picture: Picture, dst: RectF) = Unit

                override fun drawPicture(picture: Picture, dst: Rect) = Unit

                override fun drawArc(
                    oval: RectF,
                    startAngle: Float,
                    sweepAngle: Float,
                    useCenter: Boolean,
                    paint: Paint
                ) = Unit

                override fun drawArc(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    startAngle: Float,
                    sweepAngle: Float,
                    useCenter: Boolean,
                    paint: Paint
                ) = Unit

                override fun drawARGB(a: Int, r: Int, g: Int, b: Int) = Unit

                override fun drawBitmap(bitmap: Bitmap, left: Float, top: Float, paint: Paint?) =
                    Unit

                override fun drawBitmap(bitmap: Bitmap, src: Rect?, dst: RectF, paint: Paint?) =
                    Unit

                override fun drawBitmap(bitmap: Bitmap, src: Rect?, dst: Rect, paint: Paint?) = Unit

                @Deprecated("")
                override fun drawBitmap(
                    colors: IntArray,
                    offset: Int,
                    stride: Int,
                    x: Float,
                    y: Float,
                    width: Int,
                    height: Int,
                    hasAlpha: Boolean,
                    paint: Paint?
                ) = Unit

                @Deprecated("")
                override fun drawBitmap(
                    colors: IntArray,
                    offset: Int,
                    stride: Int,
                    x: Int,
                    y: Int,
                    width: Int,
                    height: Int,
                    hasAlpha: Boolean,
                    paint: Paint?
                ) = Unit

                override fun drawBitmap(bitmap: Bitmap, matrix: Matrix, paint: Paint?) = Unit

                override fun drawBitmapMesh(
                    bitmap: Bitmap,
                    meshWidth: Int,
                    meshHeight: Int,
                    verts: FloatArray,
                    vertOffset: Int,
                    colors: IntArray?,
                    colorOffset: Int,
                    paint: Paint?
                ) = Unit

                override fun drawCircle(cx: Float, cy: Float, radius: Float, paint: Paint) = Unit

                override fun drawColor(color: Int) = Unit

                override fun drawColor(color: Long) = Unit

                override fun drawColor(color: Int, mode: PorterDuff.Mode) = Unit

                override fun drawColor(color: Int, mode: BlendMode) = Unit

                override fun drawColor(color: Long, mode: BlendMode) = Unit

                override fun drawLine(
                    startX: Float,
                    startY: Float,
                    stopX: Float,
                    stopY: Float,
                    paint: Paint
                ) =
                    Unit

                override fun drawLines(pts: FloatArray, offset: Int, count: Int, paint: Paint) =
                    Unit

                override fun drawLines(pts: FloatArray, paint: Paint) = Unit

                override fun drawOval(oval: RectF, paint: Paint) = Unit

                override fun drawOval(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    paint: Paint
                ) = Unit

                override fun drawPaint(paint: Paint) = Unit

                override fun drawPatch(patch: NinePatch, dst: Rect, paint: Paint?) = Unit

                override fun drawPatch(patch: NinePatch, dst: RectF, paint: Paint?) = Unit

                override fun drawPath(path: Path, paint: Paint) = Unit

                override fun drawPoint(x: Float, y: Float, paint: Paint) = Unit

                override fun drawPoints(pts: FloatArray?, offset: Int, count: Int, paint: Paint) =
                    Unit

                override fun drawPoints(pts: FloatArray, paint: Paint) = Unit

                @Deprecated("")
                override fun drawPosText(
                    text: CharArray,
                    index: Int,
                    count: Int,
                    pos: FloatArray,
                    paint: Paint
                ) = Unit

                @Deprecated("")
                override fun drawPosText(text: String, pos: FloatArray, paint: Paint) = Unit

                override fun drawRect(rect: RectF, paint: Paint) = Unit

                override fun drawRect(r: Rect, paint: Paint) = Unit

                override fun drawRect(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    paint: Paint
                ) = Unit

                override fun drawRGB(r: Int, g: Int, b: Int) = Unit

                override fun drawRoundRect(rect: RectF, rx: Float, ry: Float, paint: Paint) = Unit

                override fun drawRoundRect(
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float,
                    rx: Float,
                    ry: Float,
                    paint: Paint
                ) = Unit

                override fun drawDoubleRoundRect(
                    outer: RectF,
                    outerRx: Float,
                    outerRy: Float,
                    inner: RectF,
                    innerRx: Float,
                    innerRy: Float,
                    paint: Paint
                ) = Unit

                override fun drawDoubleRoundRect(
                    outer: RectF,
                    outerRadii: FloatArray,
                    inner: RectF,
                    innerRadii: FloatArray,
                    paint: Paint
                ) = Unit

                override fun drawGlyphs(
                    glyphIds: IntArray,
                    glyphIdOffset: Int,
                    positions: FloatArray,
                    positionOffset: Int,
                    glyphCount: Int,
                    font: Font,
                    paint: Paint
                ) = Unit

                override fun drawText(
                    text: CharArray,
                    index: Int,
                    count: Int,
                    x: Float,
                    y: Float,
                    paint: Paint
                ) = Unit

                override fun drawText(text: String, x: Float, y: Float, paint: Paint) = Unit

                override fun drawText(
                    text: String,
                    start: Int,
                    end: Int,
                    x: Float,
                    y: Float,
                    paint: Paint
                ) =
                    Unit

                override fun drawText(
                    text: CharSequence,
                    start: Int,
                    end: Int,
                    x: Float,
                    y: Float,
                    paint: Paint
                ) = Unit

                override fun drawTextOnPath(
                    text: CharArray,
                    index: Int,
                    count: Int,
                    path: Path,
                    hOffset: Float,
                    vOffset: Float,
                    paint: Paint
                ) = Unit

                override fun drawTextOnPath(
                    text: String,
                    path: Path,
                    hOffset: Float,
                    vOffset: Float,
                    paint: Paint
                ) = Unit

                override fun drawTextRun(
                    text: CharArray,
                    index: Int,
                    count: Int,
                    contextIndex: Int,
                    contextCount: Int,
                    x: Float,
                    y: Float,
                    isRtl: Boolean,
                    paint: Paint
                ) = Unit

                override fun drawTextRun(
                    text: CharSequence,
                    start: Int,
                    end: Int,
                    contextStart: Int,
                    contextEnd: Int,
                    x: Float,
                    y: Float,
                    isRtl: Boolean,
                    paint: Paint
                ) = Unit

                override fun drawTextRun(
                    text: MeasuredText,
                    start: Int,
                    end: Int,
                    contextStart: Int,
                    contextEnd: Int,
                    x: Float,
                    y: Float,
                    isRtl: Boolean,
                    paint: Paint
                ) = Unit

                override fun drawVertices(
                    mode: VertexMode,
                    vertexCount: Int,
                    verts: FloatArray,
                    vertOffset: Int,
                    texs: FloatArray?,
                    texOffset: Int,
                    colors: IntArray?,
                    colorOffset: Int,
                    indices: ShortArray?,
                    indexOffset: Int,
                    indexCount: Int,
                    paint: Paint
                ) = Unit

                override fun drawRenderNode(renderNode: RenderNode) = Unit
            }
        }
    }
}
