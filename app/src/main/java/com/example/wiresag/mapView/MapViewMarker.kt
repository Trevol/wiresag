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
    val pylonDrawable: Drawable =
        circleBitmap(Color.BLUE).toDrawable(Resources.getSystem())
    val location = circleBitmap(Color.GREEN)
    val direction = circleBitmap(Color.BLACK)

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

        /*cnv.drawCircle(
            radius,
            radius,
            3f,
            Paint().apply {
                style = Paint.Style.FILL
                strokeWidth = 0f
                this.color = Color.BLACK
            })*/
        //bmp.set(radius.toInt(), radius.toInt(), Color.BLACK)
        return bmp
    }
}