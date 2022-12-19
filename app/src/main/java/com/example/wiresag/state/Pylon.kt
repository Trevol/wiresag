package com.example.wiresag.state

import android.graphics.Bitmap
import android.graphics.PointF
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import com.example.wiresag.math.invoke
import com.example.wiresag.math.squareDistance
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.map
import org.osmdroid.util.GeoPoint
import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt

class Pylon(val geoPoint: GeoPoint) {
    val name = "Опора ${DMS(geoPoint.latitude)}/${DMS(geoPoint.longitude)}"
    val spans: MutableList<WireSpan> = mutableStateListOf()
}

data class WireSpan(
    val pylon1: Pylon,
    val pylon2: Pylon
) {
    val length by lazy { pylon1.geoPoint.distanceToAsDouble(pylon2.geoPoint).toFloat() }
    val photos = mutableStateListOf<WireSpanPhoto>()
    val placesForPhoto = derivedStateOf {
        photoPlacesSolver(pylon1.geoPoint, pylon2.geoPoint).map { it.toGeoPoint() }
    }
}

data class PhotoWithGeoPoint(val photo: Bitmap, val geoPoint: GeoPoint)

data class WireSpanPhoto(
    val span: WireSpan,
    val photoWithGeoPoint: PhotoWithGeoPoint
) {
    val annotation = Annotation()
    val estimatedWireSag = derivedStateOf { estimateWireSag() }

    private fun estimateWireSag(): WireSagParams? {
        val points = annotation.points.toList()
        if (points.size < 3) {
            return null
        }

        // ab - это обозначеные опоры, с - нижняя точка провисания.
        // Предполагается, что ab > ac и ab > bc
        val (sqAB, sqAC, sqBC) = listOf(
            points[0].squareDistance(points[1]),
            points[0].squareDistance(points[2]),
            points[1].squareDistance(points[2])
        ).sortedDescending()
        val ab = sqrt(sqAB)
        val bc = sqrt(sqBC)
        val ac = sqrt(sqAC)
        val angB = acos((sqAB + sqBC - sqAC) / (2 * ab * bc))
        val angA = acos((sqAB + sqAC - sqBC) / (2 * ab * ac))
        val sagPx = bc * sin(angB)
        //span.length / ab = sagMeters / sagPx
        val sagMeters = sagPx * span.length / ab
        // TODO: из разных ракурсов и расстояний от пролета надо получать примерно одинаковые провисания
        // TODO: если съемка ведется НЕ на нормали к середине пролета???
        return WireSagParams(angleA = angA, angleB = angB, sag = sagMeters)
    }

    class Annotation {
        val points: MutableList<PointF> = mutableStateListOf()
    }
}

data class WireSagParams(
    val angleA: Float,
    val angleB: Float,
    val sag: Float
)
