package com.example.wiresag.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.wiresag.math.PointsAtDistanceToLineSegmentMidpoint
import com.example.wiresag.math.geo.Earth
import com.example.wiresag.math.invoke
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.utils.addItem
import com.example.wiresag.utils.map
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import kotlin.math.max

class GeoObjects {
    val pylons = mutableStateListOf<Pylon>()
    val spans = mutableStateListOf<WireSpan>()

    //val placesForPhoto = mutableStateListOf<GeoPoint>()
    val placesForPhoto by derivedStateOf { spans.flatMap { it.placesForPhoto.value.toList() } }

    //val photos = mutableStateListOf<PhotoWithGeoPoint>()
    val photos by derivedStateOf { spans.flatMap { it.photos } }

    fun markPylon(geoPoint: GeoPoint) {
        val distanceToNearestPylon = pylons.minOfOrNull { it.geoPoint.distanceToAsDouble(geoPoint) }
            ?: Double.POSITIVE_INFINITY
        if (distanceToNearestPylon <= PylonDistanceThreshold) return

        val thisPylon = pylons.addItem(Pylon(geoPoint))
        if (pylons.size > 1) {
            val otherPylon = pylons[pylons.lastIndex - 1]
            val span = spans.addItem(WireSpan(thisPylon, otherPylon))
            thisPylon.spans.add(span)
            otherPylon.spans.add(span)
        }
    }

    fun nearestWireSpan(geoPoint: GeoPoint, maxDistance: Double) =
        spans.map { it to it.midpoint.distanceToAsDouble(geoPoint) }
            .minByOrNull { (span, dist) -> dist }
            ?.let { (span, dist) ->
                if (dist <= maxDistance) {
                    span
                } else {
                    null
                }
            }

    companion object {
        const val PylonDistanceThreshold = 3.0
    }
}