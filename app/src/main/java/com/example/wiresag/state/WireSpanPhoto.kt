package com.example.wiresag.state

import android.graphics.Bitmap
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.math.pow2
import com.example.wiresag.math.squareDistance
import com.example.wiresag.ui.image.rect
import com.example.wiresag.utils.LimitedSnapshotStateList
import org.osmdroid.util.GeoPoint

data class WireSpanPhoto(
    val span: WireSpan,
    val photoWithGeoPoint: PhotoWithGeoPoint
) : GeoPointAware by photoWithGeoPoint {

    val annotation = Annotation(this)

    val estimatedWireSag by derivedStateOf {
        annotation.triangle?.let { annotatedTriangle ->
            //Из подобия треугольников: span.length / ab = sagMeters / sagPx
            val sagMeters = annotatedTriangle.sagPx * span.length / annotatedTriangle.ab
            sagMeters
        }
    }

    class Annotation(private val spanPhoto: WireSpanPhoto) {

        val points = LimitedSnapshotStateList<Offset>(maxSize = 3)

        val triangle by derivedStateOf {
            if (points.isFull()) {
                SagTriangle(points)
            } else {
                null
            }
        }

        fun tryAddOrReplace(point: Offset) {
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