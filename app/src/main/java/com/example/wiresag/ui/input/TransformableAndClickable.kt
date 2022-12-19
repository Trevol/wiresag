package com.example.wiresag.ui.input

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
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

typealias RemappedClick = (position: Offset, layerPosition: Offset) -> Unit

val NoRemappedClick: RemappedClick = { _, _ -> }

fun Modifier.transformableAndClickable(
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    onTransformationChange: (translation: Offset, scale: Float) -> Unit,
    onClick: RemappedClick = NoRemappedClick,
    onLongClick: RemappedClick = NoRemappedClick
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
) = (this - translation) / scale