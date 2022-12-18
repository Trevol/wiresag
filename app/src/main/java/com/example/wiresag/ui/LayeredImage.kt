package com.example.wiresag.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import com.example.wiresag.utils.rememberMutableStateOf
import kotlin.math.min


@Composable
fun Example() {
    val imageBitmap = remember {
        testBitmap().asImageBitmap()
    }
    Box {
        var translation by rememberMutableStateOf(Offset(100f, 100f))
        var scale by rememberMutableStateOf(1f)
        val clicks = remember { mutableStateListOf<Offset>() }

        LayeredImage(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            image = imageBitmap,
            translation = translation,
            scale = scale,
            onTransformationChange = { pan, zoom ->
                translation = pan
                scale = zoom
            },
            onClick = { _, imgPosition ->
                if (imageBitmap.rect().contains(imgPosition)) {
                    clicks.add(imgPosition)
                }
            },
            onLongClick = { _, _ -> clicks.clear() }
        ) {
            drawCircle(
                color = Color.DarkGray,
                50f,
                center = Offset(
                    imageBitmap.width - 175f,
                    imageBitmap.height - 275f
                ),
            )


            clicks.forEachIndexed { i, it ->
                drawCircle(
                    color = Color.DarkGray,
                    5f,
                    center = it,
                )
                if (i > 0) {
                    drawLine(Color.Red, it, clicks[i - 1])
                }
            }
        }
    }
}

fun ImageBitmap.rect() = Rect(0f, 0f, width - 1f, height - 1f)

@Composable
fun LayeredImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    onTransformationChange: (translation: Offset, scale: Float) -> Unit,
    onClick: RemappedClick = NoClick,
    onLongClick: RemappedClick = NoClick,
    onLayerDraw: DrawScope.() -> Unit
) {

    Canvas(
        modifier = modifier
            .transformableAndClickable(
                translation,
                scale,
                onTransformationChange,
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        drawImage(image)
        onLayerDraw()
    }
}

typealias RemappedClick = (position: Offset, layerPosition: Offset) -> Unit

val NoClick: RemappedClick = { _, _ -> }

fun Modifier.transformableAndClickable(
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    onTransformationChange: (translation: Offset, scale: Float) -> Unit,
    onClick: RemappedClick = NoClick,
    onLongClick: RemappedClick = NoClick
) = composed(
    factory = {
        val updatedTranslation by rememberUpdatedState(translation)
        val updatedScale by rememberUpdatedState(scale)
        val updatedOnTransformationChange by rememberUpdatedState(onTransformationChange)
        val updatedOnLongClick by rememberUpdatedState(onLongClick)
        val updatedOnClick by rememberUpdatedState(onClick)
        val transformOrigin = TransformOrigin(0f, 0f)
        pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown()

                    var event: PointerEvent
                    var firstReleaseAfterPress = true

                    do {
                        event = awaitPointerEvent()
                        updatedOnTransformationChange(
                            updatedTranslation + event.calculatePan(),
                            updatedScale * event.calculateZoom()
                        )
                        if (event.type != PointerEventType.Release) {
                            firstReleaseAfterPress = false
                        }
                    } while (event.type != PointerEventType.Release)

                    if (firstReleaseAfterPress && event.changes.isNotEmpty()) {
                        val change = event.changes.first()
                        val rawPosition = change.position
                        val remappedPosition =
                            rawPosition.remap(updatedTranslation, updatedScale, transformOrigin)
                        if (change.uptimeMillis - change.previousUptimeMillis < viewConfiguration.longPressTimeoutMillis) {
                            updatedOnClick(rawPosition, remappedPosition)
                        } else {
                            updatedOnLongClick(rawPosition, remappedPosition)
                        }
                    }

                }
            }
        }.graphicsLayer(
            transformOrigin = transformOrigin,
            scaleX = updatedScale,
            scaleY = updatedScale,
            translationX = updatedTranslation.x,
            translationY = updatedTranslation.y
        )
    }
)

private fun Offset.remap(
    translation: Offset,
    scale: Float,
    transformOrigin: TransformOrigin
): Offset {
    val offset = (this - translation) / scale
    return offset
}

private fun testBitmap(): Bitmap {
    //val (w, h) = 400 to 300
    val (w, h) = 4000 to 3000
    val borderWidth = min(w, h) / 10f
    val halfBorder = borderWidth / 2

    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    Canvas(bmp).apply {
        drawARGB(255, 200, 100, 0)

        drawRect(
            halfBorder, halfBorder, bmp.width - halfBorder, bmp.height - halfBorder,
            Paint().apply {
                color = android.graphics.Color.GREEN
                style = Paint.Style.STROKE
                strokeWidth = borderWidth
            }
        )

        /*drawCircle(bmp.width / 2f, bmp.height / 2f, min(bmp.width, bmp.height) / 2.5f,
            Paint().apply { color = android.graphics.Color.MAGENTA; style = Paint.Style.FILL; }
        )*/

        val (cx, cy) = bmp.width / 2f to bmp.height / 2f
        val l = min(bmp.width, bmp.height) / 4
        drawOval(
            cx - l, cy - 1.5f * l, cx + l, cy + 1.5f * l,
            Paint().apply { color = android.graphics.Color.MAGENTA; style = Paint.Style.FILL; }
        )
    }
    return bmp
}