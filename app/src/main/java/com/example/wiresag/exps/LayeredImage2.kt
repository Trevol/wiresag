package com.example.wiresag.exps

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun LayeredImage2(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    transform: TransformParameters = TransformParameters(),
    onTransform: (TransformParameters) -> Unit = {},
    onClick: RemappedClick2 = NoRemappedClick2,
    onLongClick: RemappedClick2 = NoRemappedClick2,
    onLayerDraw: DrawScope.() -> Unit
) {
    Canvas(
        modifier = modifier
            .transformableAndClickable2(
                transform,
                onGesture = { onTransform(transform.applyGesture(it)) },
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        drawImage(image)
        onLayerDraw()
    }
}

fun TransformParameters.applyGesture(gesture: PanZoomGesture) =
    if (gesture.isReleaseEvent) {
        this
    } else
        TransformParameters(
            translation = translation + gesture.pan + (gesture.centroid - translation) * (1 - gesture.zoom),
            scale = scale * gesture.zoom,
            gesture = gesture
        )