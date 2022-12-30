package com.example.wiresag.exps

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.example.wiresag.utils.rememberDerivedStateOf
import com.example.wiresag.utils.rememberMutableStateOf

@Composable
fun LayeredImage2(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    transform: TransformParameters = TransformParameters.Default,
    onTransform: (TransformParameters) -> Unit = {},
    onClick: RemappedClick2 = NoRemappedClick2,
    onLongClick: RemappedClick2 = NoRemappedClick2,
    onLayerDraw: DrawScope.() -> Unit
) {
    val state = remember { TransformState() }
    Canvas(
        modifier = modifier
            //.onSizeChanged { state.size = it }
            .onGloballyPositioned { state.size = it.size }
            .transformableAndClickable2(
                transform,
                onGesture = { onTransform(state.toTransformation(it)) },
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        drawImage(image)
        onLayerDraw()
    }
}

private class TransformState {
    var size by mutableStateOf(IntSize.Zero)

    fun toTransformation(event: GestureEvent): TransformParameters {
        val origin = TransformOrigin(0f, 0f)
        return TransformParameters(
            translation = event.pan,
            scale = event.zoom,
            transformOrigin = origin
        )
    }

    fun oldStuff() {
        /*
        var gestureEvent by rememberMutableStateOf(null as GestureEvent?)
        var size = remember { IntSize.Zero }
        var prevOrigin = remember { TransformOrigin.Center }
        val transformParameters by rememberDerivedStateOf {

            val gestureRequest = gestureEvent
            val transformParams = if (gestureRequest == null) {
                transform
            } else {
                val origin = TransformOrigin(0f, 0f)
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
                TransformParameters(
                    translation = gestureRequest.pan,
                    scale = gestureRequest.zoom,
                    transformOrigin = origin
                )
            }
            transformParams
        }
        */
    }
}