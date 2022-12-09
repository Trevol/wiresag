package com.example.wiresag.utils

import android.location.Location

fun Location(lat: Double, lon: Double, provider: String = "dummy") = Location(provider)
    .apply { latitude = lat; longitude = lon }

fun Location.plus(latDelta: Double, lonDelta: Double) =
    Location(latitude + latDelta, longitude + lonDelta)

