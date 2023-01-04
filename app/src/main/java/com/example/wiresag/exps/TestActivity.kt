package com.example.wiresag.exps

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.wiresag.activity.FullScreenActivity
import com.example.wiresag.utils.rememberMutableStateOf

class TestActivity : FullScreenActivity(keepScreenOn = true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clicks = mutableStateListOf<Offset>()

        val origTransform = TransformParameters(
            translation = Offset(50f, 350f),
            scale = .6f
        )

        val centroid = Offset(75f, 375f)
        fun newGesture(zoom: Float) = PanZoomGesture(
            pan = Offset.Zero,
            zoom = zoom,
            centroid = centroid,
            centroidSize = 10f,
            type = PointerEventType.Move
        )

        setContent {
            var transform by rememberMutableStateOf(
                origTransform
            )

            Row(
                modifier = Modifier
                    .zIndex(2f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { transform = transform.applyGesture(newGesture(1.1f)) }) {
                    Text("+")
                }
                Spacer(Modifier.width(6.dp))
                Button(onClick = { transform = transform.applyGesture(newGesture(0.9f)) }) {
                    Text("-")
                }
                Spacer(Modifier.width(6.dp))
                Button(onClick = { transform = origTransform }) {
                    Text("Orig")
                }

            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
            ) {
                /*drawCircle(Color.Red, 10f, center)
                drawCircle(Color.Black, 1f, center)*/

                drawCircle(Color(255, 0, 0, 127), 10f, centroid)
                drawCircle(Color.Black, 1f, centroid)
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
