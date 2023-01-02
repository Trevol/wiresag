package com.example.wiresag.exps

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.zIndex
import com.example.wiresag.activity.FullScreenActivity
import com.example.wiresag.utils.rememberMutableStateOf

class TestActivity : FullScreenActivity(keepScreenOn = true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clicks = mutableStateListOf<Offset>()

        setContent {
            var transform by rememberMutableStateOf(
                TransformParameters()
                /*TransformParameters(
                    translation = Offset(50f, 350f),
                    scale = 2.1f,
                    transformOrigin = TransformOrigin(.0f, .0f)
                )*/
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
            ) {
                drawCircle(Color.Red, 10f, center)
                drawCircle(Color.Black, 1f, center)
            }

            LayeredImage2(
                modifier = Modifier.fillMaxSize(),
                image = testBitmap().asImageBitmap(),
                transform = transform,
                onTransform = { transform = it },
                onClick = { _, imagePosition -> clicks.add(imagePosition) }
            ) {
                drawPoints(clicks, PointMode.Points, Color(0, 0, 0, 120), 10 * transform.scale)
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
