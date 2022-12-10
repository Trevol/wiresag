package com.example.wiresag.location

import android.location.Location
import com.example.wiresag.math.radians

fun Location.toLocationRadians() = LocationRadians(radians(latitude), radians(longitude))

fun Location.plus(latitude: Double, longitude: Double) =
    Location(this.latitude + latitude, this.longitude + longitude)

fun Location(latitude: Double, longitude: Double) = Location("").apply {
    this.latitude = latitude
    this.longitude = longitude
}