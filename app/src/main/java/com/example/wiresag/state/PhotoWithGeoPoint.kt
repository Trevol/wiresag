package com.example.wiresag.state

import android.graphics.Bitmap
import com.example.wiresag.location.GeoPointAware
import org.osmdroid.util.GeoPoint

data class PhotoWithGeoPoint(val photo: Bitmap, override val geoPoint: GeoPoint): GeoPointAware