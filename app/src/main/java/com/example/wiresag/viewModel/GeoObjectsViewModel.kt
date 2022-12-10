package com.example.wiresag.viewModel

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.wiresag.math.PointsAtDistanceToLineSegmentMidpoint
import com.example.wiresag.math.geo.Earth
import com.example.wiresag.math.invoke
import com.example.wiresag.model.Pylon
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.utils.map
import org.osmdroid.api.IGeoPoint

class GeoObjectsViewModel {
    val pylons: SnapshotStateList<Pylon> = mutableStateListOf()
    val photoPlaces: SnapshotStateList<IGeoPoint> = mutableStateListOf()
    private val photoPlacesSolver = PointsAtDistanceToLineSegmentMidpoint(PhotoDistToSpan / Earth.r)

    fun markPylon(location: Location) {
        val distanceToNearestPylon = pylons.minOfOrNull { it.location.distanceTo(location) }
            ?: Float.POSITIVE_INFINITY
        if (distanceToNearestPylon <= PylonDistanceThreshold) return

        val thisPylon = Pylon(location).also { pylons.add(it) }
        if (pylons.size > 1) {
            val span = thisPylon.location to pylons[pylons.lastIndex - 1].location
            val newPhotoPlaces = photoPlacesSolver(span).map { it.toGeoPoint() }
            photoPlaces.add(newPhotoPlaces.first)
            photoPlaces.add(newPhotoPlaces.second)
        }
    }

    companion object {
        const val PylonDistanceThreshold = 3f
        const val PhotoDistToSpan = 20.0
    }
}