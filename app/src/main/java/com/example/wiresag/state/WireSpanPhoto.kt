package com.example.wiresag.state

import androidx.compose.ui.geometry.Offset
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.math.pow2
import com.example.wiresag.math.squareDistance
import com.example.wiresag.utils.LimitedSnapshotStateList

data class WireSpanPhoto(
    val span: WireSpan,
    val photoWithGeoPoint: PhotoWithGeoPoint
) : GeoPointAware by photoWithGeoPoint {
    val wireAnnotation = WireAnnotation(this)

    class WireAnnotation(val photo: WireSpanPhoto) {
        val points = LimitedSnapshotStateList<Offset>(maxSize = 3)

        fun tryAddOrUpdatePoint(point: Offset) {
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

        fun estimations() = if (points.isFull()) {
            WireSpanPhotoEstimations(photo)
        } else {
            null
        }
    }
}

class WireSpanPhotoEstimations(val photo: WireSpanPhoto) {
    init {
        if (!photo.wireAnnotation.points.isFull()) {
            throw Exception("Unexpected: !photo.wirePoints.isFull()")
        }
    }

    val triangle = SagTriangle(photo.wireAnnotation.points)
    val estimatedWireSag = triangle.estimatedWireSag(photo.span.length)
}