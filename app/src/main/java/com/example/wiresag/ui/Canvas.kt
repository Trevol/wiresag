package com.example.wiresag.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

fun Canvas.drawLine(start: PointF, stop: PointF, paint: Paint) =
    drawLine(start.x, start.y, stop.x, stop.y, paint)

fun Canvas.drawCircle(c: PointF, radius: Float, paint: Paint) = drawCircle(c.x, c.y, radius, paint)