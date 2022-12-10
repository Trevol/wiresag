package com.example.wiresag.mapView

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import kotlin.math.ceil

object MapViewMarker {
    val pylon = circleBitmap(Color.argb(127, 0, 0, 255))
    val photoPlace = circleBitmap(Color.argb(200, 255, 255, 0))
    val location = circleBitmap(Color.GREEN)

    private fun circleBitmap(circleColor: Int, radius: Float = 15f): Bitmap {
        val side = ceil(radius * 2.0).toInt()
        val bmp = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888)
        val cnv = Canvas(bmp)
        cnv.drawCircle(
            radius,
            radius,
            radius,
            Paint().apply {
                style = Paint.Style.FILL
                strokeWidth = 0f
                this.color = circleColor
            })
        return bmp
    }
}