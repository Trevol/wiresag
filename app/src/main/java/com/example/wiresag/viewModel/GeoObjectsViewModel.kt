package com.example.wiresag.viewModel

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.wiresag.math.PointsAtDistanceToLineSegmentMidpoint
import com.example.wiresag.math.geo.Earth
import com.example.wiresag.math.invoke
import com.example.wiresag.model.PhotoWithGeoPoint
import com.example.wiresag.model.Pylon
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.utils.map
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class GeoObjectsViewModel {
    val pylons: SnapshotStateList<Pylon> = mutableStateListOf()
    val placesForPhoto: SnapshotStateList<IGeoPoint> = mutableStateListOf()
    val photos: SnapshotStateList<PhotoWithGeoPoint> = mutableStateListOf()

    private val photoPlacesSolver = PointsAtDistanceToLineSegmentMidpoint(PhotoDistToSpan / Earth.r)

    fun markPylon(location: Location) {
        val location = GeoPoint(location)
        val distanceToNearestPylon = pylons.minOfOrNull { it.geoPoint.distanceToAsDouble(location) }
            ?: Double.POSITIVE_INFINITY
        if (distanceToNearestPylon <= PylonDistanceThreshold) return

        val thisPylon = Pylon(location).also { pylons.add(it) }
        if (pylons.size > 1) {
            val span = thisPylon.geoPoint to pylons[pylons.lastIndex - 1].geoPoint
            val newPhotoPlaces = photoPlacesSolver(span).map { it.toGeoPoint() }
            placesForPhoto.add(newPhotoPlaces.first)
            placesForPhoto.add(newPhotoPlaces.second)
        }
    }

    companion object {
        const val PylonDistanceThreshold = 3.0
        const val PhotoDistToSpan = 20.0
    }
}