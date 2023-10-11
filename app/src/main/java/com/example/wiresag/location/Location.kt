package com.example.wiresag.location

import android.location.Location
import com.example.wiresag.math.radians
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.prettyFormat
import com.example.wiresag.utils.round

fun Location.toLocationRadians() = LocationRadians(radians(latitude), radians(longitude))

fun Location.plus(latitude: Double, longitude: Double) =
    Location(this.latitude + latitude, this.longitude + longitude)

fun Location(latitude: Double, longitude: Double) = Location("").apply {
    this.latitude = latitude
    this.longitude = longitude
}

fun Location.info(): String {
    val speedInfo = if (hasSpeed()) speed.round(3) else ""
    val latLon = "${DMS(latitude).prettyFormat()}, ${DMS(longitude).prettyFormat()}"
    return "$provider: $latLon   ${accuracy.round(1)}   $speedInfo"
}