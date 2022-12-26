package com.example.wiresag.exps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.min

fun testBitmap(): Bitmap {
    // val (w, h) = 400 to 300
    val (w, h) = 4000 to 3000

    val borderWidth = min(w, h) / 10f
    val halfBorder = borderWidth / 2

    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    with(Canvas(bmp)) {
        drawARGB(255, 200, 100, 0)

        drawRect(
            halfBorder, halfBorder, bmp.width - halfBorder, bmp.height - halfBorder,
            Paint().apply {
                color = android.graphics.Color.GREEN
                style = Paint.Style.STROKE
                strokeWidth = borderWidth
            }
        )

        val (cx, cy) = bmp.width / 2f to bmp.height / 2f
        val l = min(bmp.width, bmp.height) / 4
        drawOval(
            cx - l, cy - 1.5f * l, cx + l, cy + 1.5f * l,
            Paint().apply { color = android.graphics.Color.MAGENTA; style = Paint.Style.FILL; }
        )

        // draw grid
        val gridPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            strokeWidth = 2f
        }
        val delta = 50
        0.rangeTo(h / delta)
            .map { it * delta.toFloat() }
            .forEach { y ->
                drawLine(0f, y, w.toFloat(), y, gridPaint)
            }
        0.rangeTo(w / delta)
            .map { it * delta.toFloat() }
            .forEach { x ->
                drawLine(x, 0f, x, h.toFloat(), gridPaint)
            }
    }
    return bmp
}