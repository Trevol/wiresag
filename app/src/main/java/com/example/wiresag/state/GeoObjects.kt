package com.example.wiresag.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.wiresag.math.PointsAtDistanceToLineSegmentMidpoint
import com.example.wiresag.math.geo.Earth
import com.example.wiresag.math.invoke
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.utils.map
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class GeoObjects {
    val pylons: SnapshotStateList<Pylon> = mutableStateListOf()
    val placesForPhoto: SnapshotStateList<IGeoPoint> = mutableStateListOf()
    val photos: SnapshotStateList<PhotoWithGeoPoint> = mutableStateListOf()

    fun markPylon(geoPoint: GeoPoint) {
        val distanceToNearestPylon = pylons.minOfOrNull { it.geoPoint.distanceToAsDouble(geoPoint) }
            ?: Double.POSITIVE_INFINITY
        if (distanceToNearestPylon <= PylonDistanceThreshold) return

        val thisPylon = Pylon(geoPoint).also { pylons.add(it) }
        if (pylons.size > 1) {
            val span = thisPylon.geoPoint to pylons[pylons.lastIndex - 1].geoPoint
            val newPhotoPlaces = photoPlacesSolver(span)
                .map { it.toGeoPoint() }
            placesForPhoto.add(newPhotoPlaces.first)
            placesForPhoto.add(newPhotoPlaces.second)
        }
    }

    companion object {
        const val PylonDistanceThreshold = 3.0
    }
}