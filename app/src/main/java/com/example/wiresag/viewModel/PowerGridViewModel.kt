package com.example.wiresag.viewModel

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.wiresag.model.Pylon
import com.example.wiresag.utils.minByOrNullWithValue

class PowerGridViewModel(
    val pylons: SnapshotStateList<Pylon> = mutableStateListOf()
) {
    fun createPylon(location: Location) {
        val distToNearestPylon = pylons.minOfOrNull { it.location.distanceTo(location) }
        if (distToNearestPylon == null || distToNearestPylon > PYLON_DISTANCE_THRESHOLD) {
            pylons.add(Pylon(location))
        }
    }

    companion object {
        const val PYLON_DISTANCE_THRESHOLD = 3f
    }
}