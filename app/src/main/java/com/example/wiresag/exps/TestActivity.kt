package com.example.wiresag.exps

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.example.wiresag.utils.rememberDerivedStateOf
import com.example.wiresag.utils.rememberMutableStateOf

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        val clicks = mutableStateListOf<Offset>()

        setContent {
            var transform by rememberMutableStateOf(TransformParameters.Default)
            LayeredImage2(
                modifier = Modifier.fillMaxSize(),
                image = testBitmap().asImageBitmap(),
                transform = transform,
                onTransform = { transform = it },
                onClick = { _, imagePosition -> clicks.add(imagePosition) }
            ) {
                drawPoints(clicks, PointMode.Points, Color.Black, 10 * transform.scale)
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
