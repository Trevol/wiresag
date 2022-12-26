package com.example.wiresag.exps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import kotlin.math.min

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        var transformationParams by mutableStateOf(TransformationParams.Default)
        var size = IntSize.Zero

        var prevOrigin = TransformOrigin.Center
        val transformOrigin by derivedStateOf {
            if (transformationParams.centroidSize == 0f ||
                transformationParams.centroid == Offset.Unspecified ||
                transformationParams.centroid == Offset.Infinite ||
                size == IntSize.Zero
            ) {
                prevOrigin
            } else {
                prevOrigin = TransformOrigin(
                    transformationParams.centroid.x / size.width,
                    transformationParams.centroid.y / size.height
                )
                prevOrigin
            }
        }

        val clicks = mutableStateListOf<Offset>()

        setContent {
            LayeredImage2(
                modifier = Modifier
                    .fillMaxSize()
                    //.onSizeChanged { size = it }
                    .onGloballyPositioned { size = it.size },
                image = testBitmap().asImageBitmap(),
                translation = transformationParams.translation,
                scale = transformationParams.scale,
                transformOrigin = transformOrigin,
                onTransformationChange = {
                    transformationParams = it
                },
                onClick = { _, imagePosition -> clicks.add(imagePosition) }
            ) {
                drawPoints(clicks, PointMode.Points, Color.Black, 10f)
                /*if (transformationParams.centroidSize > 0f)
                    drawCircle(
                        Color.Cyan,
                        transformationParams.centroidSize / 2,
                        transformationParams.centroid
                    )*/

            }
        }
    }
}
