package com.example.wiresag.model

import android.graphics.Bitmap
import org.osmdroid.util.GeoPoint

data class PhotoWithGeoPoint(val photo: Bitmap, val geoPoint: GeoPoint)