package com.example.wiresag.location

import com.example.wiresag.math.degrees

data class LocationRadians(val latitude: Double, val longitude: Double)

fun LocationRadians.toLocation() = Location(degrees(latitude), degrees(longitude))

operator fun LocationRadians.plus(other: LocationRadians) =
    this.plus(other.latitude, other.longitude)

fun LocationRadians.plus(latitude: Double, longitude: Double) =
    LocationRadians(this.latitude + latitude, this.longitude + longitude)