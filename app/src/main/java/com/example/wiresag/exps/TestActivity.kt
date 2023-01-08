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
import com.example.wiresag.ui.image.LayeredImage
import com.example.wiresag.ui.image.applyGesture
import com.example.wiresag.ui.input.PanZoomGesture
import com.example.wiresag.ui.input.TransformParameters
import com.example.wiresag.utils.rememberMutableStateOf
import java.time.LocalDateTime

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

            LayeredImage(
                modifier = Modifier.fillMaxSize(),
                image = testBitmap().asImageBitmap(),
                transform = transform,
                onTransform = {
                    //it.log()
                    transform = it
                },
                onClick = { _, imagePosition -> clicks.add(imagePosition) }
            ) {
                drawPoints(clicks, PointMode.Points, Color(0, 0, 0, 120), 10 * transform.scale)
            }
        }
    }
}

private fun TransformParameters.log() {
    val stamp = LocalDateTime.now().let { "${it.second}.${it.minute}.${it.hour}" }
    println("---------------------")
    println("$stamp: tr: $translation  pan: ${gesture?.pan}(${gesture?.pan?.getDistanceSquared()})")
}
