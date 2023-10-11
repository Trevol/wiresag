package com.example.wiresag.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

fun Canvas.drawLine(start: PointF, stop: PointF, paint: Paint) =
    drawLine(start.x, start.y, stop.x, stop.y, paint)

fun Canvas.drawCircle(c: PointF, radius: Float, paint: Paint) = drawCircle(c.x, c.y, radius, paint)

fun DrawScope.drawText(text: String, textStart: Offset, paint: NativePaint) {
    drawContext.canvas.nativeCanvas.drawText(text, textStart.x, textStart.y, paint)
}