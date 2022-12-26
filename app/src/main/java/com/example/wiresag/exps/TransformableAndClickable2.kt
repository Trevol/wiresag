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

data class TransformationParams(
    val translation: Offset,
    val scale: Float,
    val centroid: Offset,
    val centroidSize: Float
) {
    companion object
}

val TransformationParams.Companion.Default
    get() = TransformationParams(
        translation = Offset.Zero,
        scale = 1f,
        centroid = Offset.Zero,
        centroidSize = 0f
    )

fun Modifier.transformableAndClickable2(
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    transformOrigin: TransformOrigin,
    onTransformationChange: (TransformationParams) -> Unit,
    onClick: RemappedClick2 = NoRemappedClick2,
    onLongClick: RemappedClick2 = NoRemappedClick2
) = composed(
    factory = {
        val updatedTranslation by rememberUpdatedState(translation)
        val updatedScale by rememberUpdatedState(scale)
        val updatedOnTransformationChange by rememberUpdatedState(onTransformationChange)
        val updatedOnLongClick by rememberUpdatedState(onLongClick)
        val updatedOnClick by rememberUpdatedState(onClick)
        val updatedTransformOrigin by rememberUpdatedState(transformOrigin)

        pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown()

                    var event: PointerEvent
                    var firstReleaseAfterPress = true

                    do {
                        event = awaitPointerEvent()
                        updatedOnTransformationChange(
                            TransformationParams(
                                translation = updatedTranslation + event.calculatePan(),
                                scale = updatedScale * event.calculateZoom(),
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
                        val remappedPosition =
                            rawPosition.remap(
                                updatedTranslation,
                                updatedScale,
                                updatedTransformOrigin
                            )
                        if (change.uptimeMillis - change.previousUptimeMillis < viewConfiguration.longPressTimeoutMillis) {
                            updatedOnClick(rawPosition, remappedPosition)
                        } else {
                            updatedOnLongClick(rawPosition, remappedPosition)
                        }
                    }

                }
            }
        }.graphicsLayer(
            transformOrigin = updatedTransformOrigin,
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
) = (this - translation) / scale