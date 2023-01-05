package com.example.wiresag.state

import androidx.compose.runtime.mutableStateListOf
import com.example.wiresag.location.nearest
import com.example.wiresag.utils.addItem
import org.osmdroid.util.GeoPoint

class GeoObjects {
    val pylons = mutableStateListOf<Pylon>()
    val spans = mutableStateListOf<WireSpan>()

    fun markPylon(geoPoint: GeoPoint) {
        val distanceToNearestPylon = pylons.nearest(geoPoint)?.distance ?: Double.POSITIVE_INFINITY
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
        spans.nearest(geoPoint, maxDistance)?.item

    fun deleteSpanPhoto(photoForAnnotation: WireSpanPhoto) {
        photoForAnnotation.span.photos.remove(photoForAnnotation)
    }

    companion object {
        const val PylonDistanceThreshold = 3.0
    }
}