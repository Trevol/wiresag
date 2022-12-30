package com.example.wiresag.exps

import androidx.compose.foundation.gestures.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

typealias RemappedClick2 = (position: Offset, layerPosition: Offset) -> Unit

val NoRemappedClick2: RemappedClick2 = { _, _ -> }

data class GestureEvent(
    val pan: Offset,
    val zoom: Float,
    val centroid: Offset,
    val centroidSize: Float
) {
    companion object
}

val GestureEvent.Companion.Default
    get() = GestureEvent(
        pan = Offset.Zero,
        zoom = 1f,
        centroid = Offset.Zero,
        centroidSize = 0f
    )

data class TransformParameters(
    val translation: Offset,
    val scale: Float,
    val transformOrigin: TransformOrigin
) {
    companion object
}

val TransformParameters.Companion.Default
    get() = TransformParameters(
        Offset.Zero,
        1f,
        TransformOrigin.Center
    )

fun Modifier.transformableAndClickable2(
    transform: TransformParameters,
    onGesture: (GestureEvent) -> Unit,
    onClick: RemappedClick2 = NoRemappedClick2,
    onLongClick: RemappedClick2 = NoRemappedClick2
) = composed(
    factory = {
        val updatedTransform by rememberUpdatedState(transform)
        val updatedOnGesture by rememberUpdatedState(onGesture)
        val updatedOnLongClick by rememberUpdatedState(onLongClick)
        val updatedOnClick by rememberUpdatedState(onClick)

        pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown()

                    var event: PointerEvent
                    var firstReleaseAfterPress = true

                    do {
                        event = awaitPointerEvent()
                        updatedOnGesture(
                            GestureEvent(
                                pan = event.calculatePan(),
                                zoom = event.calculateZoom(),
                                centroid = event.calculateCentroid(),
                                centroidSize = event.calculateCentroidSize()
                            ),
                        )
                        if (event.type != PointerEventType.Release) {
                            firstReleaseAfterPress = false
                        }
                    } while (event.type != PointerEventType.Release)

                    if (firstReleaseAfterPress && event.changes.isNotEmpty()) {
                        val change = event.changes.first()
                        val rawPosition = change.position
                        val remappedPosition = rawPosition.remap(updatedTransform)
                        if (change.uptimeMillis - change.previousUptimeMillis < viewConfiguration.longPressTimeoutMillis) {
                            updatedOnClick(rawPosition, remappedPosition)
                        } else {
                            updatedOnLongClick(rawPosition, remappedPosition)
                        }
                    }

                }
            }
        }.graphicsLayer(
            transformOrigin = updatedTransform.transformOrigin,
            scaleX = updatedTransform.scale,
            scaleY = updatedTransform.scale,
            translationX = updatedTransform.translation.x,
            translationY = updatedTransform.translation.y
        )
    }
)

private fun Offset.remap(transformParameters: TransformParameters) =
    (this - transformParameters.translation) / transformParameters.scale