package com.example.wiresag.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import com.example.wiresag.math.pow2
import com.example.wiresag.math.squareDistance
import com.example.wiresag.ui.image.rect
import com.example.wiresag.utils.LimitedSnapshotStateList

data class WireSpanPhoto(
    val span: WireSpan,
    val photoWithGeoPoint: PhotoWithGeoPoint
) {
    val annotation = Annotation(this)

    val estimatedWireSag by derivedStateOf {
        annotation.triangle?.let { annotatedTriangle ->
            //Из подобия треугольников: span.length / ab = sagMeters / sagPx
            val sagMeters = annotatedTriangle.sagPx * span.length / annotatedTriangle.ab
            sagMeters
        }
    }

    class Annotation(val spanPhoto: WireSpanPhoto) {
        val points = LimitedSnapshotStateList<Offset>(maxSize = 3)

        val triangle by derivedStateOf {
            if (points.isFull()) {
                SagTriangle(points)
            } else {
                null
            }
        }

        fun tryAddOrReplace(point: Offset) {
            if (spanPhoto.photoWithGeoPoint.photo.rect.contains(point)) {
                val nearestPointWithDist = points.map { it to it.squareDistance(point) }
                    .minByOrNull { (_, sqDist) -> sqDist }
                if (nearestPointWithDist == null) {
                    points.add(point)
                } else {
                    val (nearestPoint, squaredDistance) = nearestPointWithDist
                    if (squaredDistance < pow2(100f)) {
                        points.remove(nearestPoint)
                    }
                    points.add(point)
                }
            }
        }
    }
}