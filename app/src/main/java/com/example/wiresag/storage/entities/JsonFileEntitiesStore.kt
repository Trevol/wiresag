package com.example.wiresag.storage.entities

import androidx.compose.ui.geometry.Offset
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.state.PhotoWithGeoPoint
import com.example.wiresag.state.Pylon
import com.example.wiresag.state.WireSpan
import com.example.wiresag.state.WireSpanPhoto
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class JsonFileEntitiesStore(val directory: File, fileName: String = "entities.json") :
    EntitiesStore {
    private val dataFile = File(directory, fileName)

    override fun save(pylons: Array<Pylon>, spans: Array<WireSpan>) {
        val storedPackage = EntitiesStore.Entities(pylons, spans).toStoredPackage()
        dataFile.outputStream().use {
            Json.encodeToStream(storedPackage, it)
        }
    }

    override fun load() = if (dataFile.exists()) {
        dataFile.inputStream().use {
            Json.decodeFromStream<StoredPackage>(it)
        }.toEntities()
    } else {
        EntitiesStore.Entities(emptyArray(), emptyArray())
    }

    private fun StoredPackage.toEntities(): EntitiesStore.Entities {
        val pylonById = pylons.associateBy(
            { it.id },
            { Pylon(it.geoPoint.toGeoPoint()) }
        )
        val spanById = spans.associateBy(
            { it.id },
            {

                val pylon1 = pylonById.getValue(it.pylon1Id)
                val pylon2 = pylonById.getValue(it.pylon2Id)
                val span = WireSpan(pylon1, pylon2)
                pylon1.spans.add(span)
                pylon2.spans.add(span)
                span
            }
        )
        photos.forEach {
            val span = spanById.getValue(it.wireSpanId)
            val spanPhoto = WireSpanPhoto(
                span,
                photoWithGeoPoint = PhotoWithGeoPoint(it.photoId, it.geoPoint.toGeoPoint())
            ).apply {
                wireAnnotation.points.addAll(it.annotationPoints.map { it.toOffset() })
            }
            span.photos.add(spanPhoto)
        }
        return EntitiesStore.Entities(
            pylons = pylonById.values.toTypedArray(),
            spans = spanById.values.toTypedArray()
        )
    }

    private fun EntitiesStore.Entities.toStoredPackage(): StoredPackage {
        val pylonRecords = pylons.mapIndexed { i, pylon ->
            PylonRecord(i, GeoPointRecord(pylon))
        }

        val spanRecords = spans.mapIndexed { i, span ->
            WireSpanRecord(i, pylons.indexOf(span.pylon1), pylons.indexOf(span.pylon2))
        }

        val spanPhotoRecords = spans.flatMap { it.photos }.mapIndexed { i, photo ->
            WireSpanPhotoRecord(i,
                spans.indexOf(photo.span),
                photo.photoWithGeoPoint.photoId,
                GeoPointRecord(photo.photoWithGeoPoint),
                photo.wireAnnotation.points.map { it.toOffsetRecord() }
            )
        }

        return StoredPackage(pylonRecords, spanRecords, spanPhotoRecords)
    }


    @Serializable
    private data class StoredPackage(
        val pylons: List<PylonRecord>,
        val spans: List<WireSpanRecord>,
        val photos: List<WireSpanPhotoRecord>
    )

    @Serializable
    private data class GeoPointRecord(val latitude: Double, val longitude: Double)

    private fun GeoPointRecord(geoPoint: IGeoPoint) =
        GeoPointRecord(geoPoint.latitude, geoPoint.longitude)

    private fun GeoPointRecord(geoPointAware: GeoPointAware) =
        GeoPointRecord(geoPointAware.geoPoint)

    private fun GeoPointRecord.toGeoPoint() = GeoPoint(latitude, longitude)

    @Serializable
    private data class OffsetRecord(val x: Float, val y: Float)

    private fun Offset.toOffsetRecord() = OffsetRecord(x, y)
    private fun OffsetRecord.toOffset() = Offset(x, y)

    @Serializable
    private data class PylonRecord(val id: Int, val geoPoint: GeoPointRecord)

    @Serializable
    private data class WireSpanRecord(val id: Int, val pylon1Id: Int, val pylon2Id: Int)

    @Serializable
    private data class WireSpanPhotoRecord(
        val id: Int,
        val wireSpanId: Int,
        val photoId: String,
        val geoPoint: GeoPointRecord,
        val annotationPoints: List<OffsetRecord>
    )
}
