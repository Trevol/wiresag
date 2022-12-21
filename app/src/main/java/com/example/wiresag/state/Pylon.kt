package com.example.wiresag.state

import android.graphics.Bitmap
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.location.midpoint
import com.example.wiresag.math.invoke
import com.example.wiresag.math.squareDistance
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.LimitedSnapshotStateList
import com.example.wiresag.utils.map
import org.osmdroid.util.GeoPoint
import kotlin.math.abs
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
    val length by lazy { pylon1.geoPoint.distanceToAsDouble(pylon2.geoPoint).toFloat() }
    val midpoint by lazy { pylon1.geoPoint.midpoint(pylon2.geoPoint) }
    override val geoPoint: GeoPoint get() = midpoint
    val photos = mutableStateListOf<WireSpanPhoto>()
    val placesForPhoto by derivedStateOf {
        photoPlacesSolver(pylon1.geoPoint, pylon2.geoPoint).map { it.toGeoPoint() }
    }
}

data class PhotoWithGeoPoint(val photo: Bitmap, val geoPoint: GeoPoint)

data class WireSpanPhoto(
    val span: WireSpan,
    val photoWithGeoPoint: PhotoWithGeoPoint
) {
    val annotation = Annotation()

    val estimatedWireSag by derivedStateOf {
        annotation.triangle?.let { annotatedTriangle ->
            //Из подобия треугольников: span.length / ab = sagMeters / sagPx
            // TODO: из разных ракурсов и расстояний от пролета надо получать примерно одинаковые провисания
            // TODO: если съемка ведется НЕ на нормали к середине пролета???
            val sagMeters = annotatedTriangle.sagPx * span.length / annotatedTriangle.ab
            sagMeters
        }
    }

    class Annotation {
        val points = LimitedSnapshotStateList<Offset>(maxSize = 3)

        val triangle by derivedStateOf {
            if (points.isFull()) {
                SagTriangle(points)
            } else {
                null
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
