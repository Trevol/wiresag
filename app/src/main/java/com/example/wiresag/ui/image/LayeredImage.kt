package com.example.wiresag.ui.image

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.wiresag.ui.input.*

@Composable
fun LayeredImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    transform: TransformParameters = TransformParameters(),
    onTransform: (TransformParameters) -> Unit = {},
    onClick: (PointerInputPositions) -> Unit = {},
    onLongClick: (PointerInputPositions) -> Unit = {},
    onLayerDraw: DrawScope.() -> Unit
) {
    Canvas(
        modifier = modifier
            .transformableAndClickable(
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
    } else {
        TransformParameters(
            translation = translation + gesture.pan + (gesture.centroid - translation) * (1 - gesture.zoom),
            scale = scale * gesture.zoom,
            gesture = gesture
        )
    }