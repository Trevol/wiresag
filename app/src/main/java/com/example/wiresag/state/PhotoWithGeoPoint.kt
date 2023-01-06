package com.example.wiresag.state

import android.graphics.Bitmap
import org.osmdroid.util.GeoPoint

data class PhotoWithGeoPoint(val photo: Bitmap, val geoPoint: GeoPoint)