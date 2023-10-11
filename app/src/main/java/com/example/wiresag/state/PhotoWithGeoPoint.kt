package com.example.wiresag.state

import android.graphics.Bitmap
import com.example.wiresag.location.GeoPointAware
import org.osmdroid.util.GeoPoint

data class PhotoWithGeoPoint(val photoId: String, override val geoPoint: GeoPoint): GeoPointAware