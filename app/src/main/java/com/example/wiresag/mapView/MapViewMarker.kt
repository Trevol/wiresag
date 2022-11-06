package com.example.wiresag.mapView

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable

object MapViewMarker {
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
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = 6f
                this.color = circleColor
            })
        return bmp
    }
}