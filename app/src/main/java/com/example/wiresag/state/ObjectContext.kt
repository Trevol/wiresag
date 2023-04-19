package com.example.wiresag.state

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.wiresag.location.nearest
import com.example.wiresag.storage.entities.EntitiesStore
import com.example.wiresag.storage.image.ImageStorage
import com.example.wiresag.utils.addItem
import org.osmdroid.util.GeoPoint

class ObjectContext(
    private val entitiesStore: EntitiesStore,
    private val imageStorage: ImageStorage
) {
    val pylons: SnapshotStateList<Pylon> //= mutableStateListOf<Pylon>()
    val spans: SnapshotStateList<WireSpan> //= mutableStateListOf<WireSpan>()

    init {
        entitiesStore.load().also {
            pylons = it.pylons.toList().toMutableStateList()
            spans = it.spans.toList().toMutableStateList()
        }
    }

    private var nextSaveRequestEval = false
    val saveRequest by derivedStateOf {
        pylons.size
        spans.forEach { it.photos.forEach { it.wireAnnotation.points.size } }
        if (nextSaveRequestEval) {
            entitiesStore.save(pylons.toTypedArray(), spans.toTypedArray())
        }
        nextSaveRequestEval = true
    }

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

    fun nearestSpanPhoto(geoPoint: GeoPoint, maxDistance: Double) =
        spans.flatMap { it.photos }.nearest(geoPoint, maxDistance)?.item

    fun deleteSpanPhoto(spanPhoto: WireSpanPhoto) {
        spanPhoto.span.photos.remove(spanPhoto)
        imageStorage.delete(spanPhoto.photoWithGeoPoint.photoId)
    }

    fun addWireSpanPhoto(span: WireSpan, photo: Bitmap, photoGeoPoint: GeoPoint): WireSpanPhoto {
        val photoId = imageStorage.save(photo)
        return span.photos.addItem(
            WireSpanPhoto(span, PhotoWithGeoPoint(photoId, photoGeoPoint))
        )
    }

    fun readImage(id: String) = imageStorage.read(id)

    companion object {
        const val PylonDistanceThreshold = 3.0
    }
}