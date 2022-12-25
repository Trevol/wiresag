package com.example.wiresag.exps

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun LayeredImage2(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    onTransformationChange: (TransformationParams) -> Unit,
    onClick: RemappedClick2 = NoRemappedClick2,
    onLongClick: RemappedClick2 = NoRemappedClick2,
    onLayerDraw: DrawScope.() -> Unit
) {
    Canvas(
        modifier = modifier
            .transformableAndClickable2(
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
