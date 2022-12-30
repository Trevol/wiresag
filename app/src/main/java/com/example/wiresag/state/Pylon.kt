package com.example.wiresag.state

import android.graphics.Bitmap
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.location.midpoint
import com.example.wiresag.math.invoke
import com.example.wiresag.math.pow2
import com.example.wiresag.math.squareDistance
import com.example.wiresag.ui.image.rect
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.LimitedSnapshotStateList
import org.osmdroid.util.GeoPoint
import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt


data class Pylon(override val geoPoint: GeoPoint) : GeoPointAware {
    val name = "Опора ${DMS(geoPoint.latitude)}/${DMS(geoPoint.longitude)}"
    val spans: MutableList<WireSpan> = mutableStateListOf()
}

data class WireSpan(
    val pylon1: Pylon,
    val pylon2: Pylon
) : GeoPointAware {
    override val geoPoint: GeoPoint get() = midpoint

    val length by lazy { pylon1.geoPoint.distanceToAsDouble(pylon2.geoPoint).toFloat() }
    val midpoint by lazy { pylon1.geoPoint.midpoint(pylon2.geoPoint) }
    val photos = mutableStateListOf<WireSpanPhoto>()
    val photoLine by lazy { PhotoLine(this) }

    class PhotoLine(span: WireSpan) {
        val pointsWithDistances = photoPlacesSolver(span.pylon1.geoPoint, span.pylon2.geoPoint)
        val allPoints = pointsWithDistances.flatMap { listOf(it.point1, it.point2) }
        val normalPoints = pointsWithDistances.maxByOrNull { it.distance }!!
            .let { p -> p.point1 to p.point2 }
    }
}

data class PhotoWithGeoPoint(val photo: Bitmap, val geoPoint: GeoPoint)

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

// ab - это обозначеные опоры, с - нижняя точка провисания.
// Предполагается, что ab > ac и ab > bc
data class SagTriangle(val a: Offset, val b: Offset, val c: Offset) {
    init {
        if (a == b || a == c || b == c) {
            throw Exception("a == b || a == c || b == c")
        }
    }

    private val abSquared = a.squareDistance(b)
    private val acSquared = a.squareDistance(c)

    private val bcSquared = b.squareDistance(c)
    val ab = sqrt(abSquared)
    val bc = sqrt(bcSquared)

    val ac = sqrt(acSquared)
    val angB = acos((abSquared + bcSquared - acSquared) / (2 * ab * bc))
    val angA = acos((abSquared + acSquared - bcSquared) / (2 * ab * ac))

    val sagPx = bc * sin(angB)
}

fun SagTriangle(unclassifiedVertices: List<Offset>): SagTriangle {
    val sides = listOf(
        unclassifiedVertices[0] to unclassifiedVertices[1],
        unclassifiedVertices[0] to unclassifiedVertices[2],
        unclassifiedVertices[1] to unclassifiedVertices[2]
    )
    val (ab, ac, bc) = sides.sortedByDescending { it.squareDistance() }
    val (a, b) = ab
    // C is common point of AC and BC
    val c = if (ac.first == bc.first || ac.first == bc.second) ac.first else ac.second
    return SagTriangle(a = a, b = b, c = c)
}
