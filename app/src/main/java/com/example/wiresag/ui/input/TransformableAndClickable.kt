package com.example.wiresag.ui.input


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

data class PointerInputPositions(val position: Offset, val layerPosition: Offset)

data class PanZoomGesture(
    val pan: Offset,
    val zoom: Float,
    val centroid: Offset,
    val centroidSize: Float,
    val type: PointerEventType
)

val PanZoomGesture.isReleaseEvent get() = type == PointerEventType.Release
val PanZoomGesture.isSmallPan get() = zoom == 1f && pan.getDistanceSquared() <= 9f

data class TransformParameters(
    val translation: Offset = Offset.Zero,
    val scale: Float = 1f,
    val gesture: PanZoomGesture? = null
)

fun Modifier.transformableAndClickable(
    transform: TransformParameters,
    onGesture: (PanZoomGesture) -> Unit,
    onClick: (PointerInputPositions) -> Unit = {},
    onLongClick: (PointerInputPositions) -> Unit = {},
) = composed(
    factory = {
        val updatedTransform by rememberUpdatedState(transform)
        val updatedOnGesture by rememberUpdatedState(onGesture)
        val updatedOnLongClick by rememberUpdatedState(onLongClick)
        val updatedOnClick by rememberUpdatedState(onClick)

        pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    val firstDown = awaitFirstDown()

                    var event: PointerEvent
                    var isTransformEventSequence = false
                    val smallPans = mutableListOf<PanZoomGesture>()

                    do {
                        event = awaitPointerEvent()

                        val gesture = PanZoomGesture(
                            pan = event.calculatePan(),
                            zoom = event.calculateZoom(),
                            centroid = event.calculateCentroid(),
                            centroidSize = event.calculateCentroidSize(),
                            type = event.type
                        )
                        if (isTransformEventSequence) {
                            updatedOnGesture(gesture)
                        } else {
                            if (gesture.isSmallPan) {
                                smallPans.add(gesture)
                            } else {
                                isTransformEventSequence = true
                                smallPans.forEach(updatedOnGesture)
                                updatedOnGesture(gesture)
                            }
                        }
                    } while (!gesture.isReleaseEvent)

                    if (!isTransformEventSequence && event.changes.isNotEmpty()) {
                        val change = event.changes.first()
                        val rawPosition = change.position
                        val layerPosition = rawPosition.transform(updatedTransform)
                        if (change.uptimeMillis - firstDown.previousUptimeMillis < viewConfiguration.longPressTimeoutMillis) {
                            updatedOnClick(PointerInputPositions(rawPosition, layerPosition))
                        } else {
                            updatedOnLongClick(PointerInputPositions(rawPosition, layerPosition))
                        }
                    }

                }
            }
        }.graphicsLayer(
            transformOrigin = TransformOrigin(0f, 0f),
            scaleX = updatedTransform.scale,
            scaleY = updatedTransform.scale,
            translationX = updatedTransform.translation.x,
            translationY = updatedTransform.translation.y
        )
    }
)

private fun Offset.transform(transform: TransformParameters) =
    (this - transform.translation) / transform.scale
