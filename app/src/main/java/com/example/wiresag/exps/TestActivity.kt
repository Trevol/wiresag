package com.example.wiresag.exps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.asImageBitmap
import com.example.wiresag.utils.rememberMutableStateOf
import kotlin.math.min

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        var transformationParams by mutableStateOf(TransformationParams())

        val clicks = mutableStateListOf<Offset>()

        setContent {
            LayeredImage2(
                modifier = Modifier.fillMaxSize(),
                image = testBitmap().asImageBitmap(),
                translation = transformationParams.translation,
                scale = transformationParams.scale,
                onTransformationChange = { transformationParams = it },
                onClick = { _, imagePosition -> clicks.add(imagePosition) }
            ) {
                drawPoints(clicks, PointMode.Points, Color.Black, 10f)
                if (transformationParams.centroidSize > 0f)
                    drawCircle(
                        Color.Cyan,
                        transformationParams.centroidSize / 2,
                        transformationParams.centroid
                    )

            }
        }
    }


}


private fun testBitmap(): Bitmap {
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
    }
    return bmp
}