package com.example.wiresag.ui.image

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.wiresag.ui.input.NoRemappedClick
import com.example.wiresag.ui.input.RemappedClick
import com.example.wiresag.ui.input.transformableAndClickable

@Composable
fun LayeredImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    onTransformationChange: (translation: Offset, scale: Float) -> Unit,
    onClick: RemappedClick = NoRemappedClick,
    onLongClick: RemappedClick = NoRemappedClick,
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
