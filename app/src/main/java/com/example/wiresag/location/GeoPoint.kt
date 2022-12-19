package com.example.wiresag.location

import com.example.wiresag.math.geo.midpoint
import com.example.wiresag.math.toRadians
import com.example.wiresag.osmdroid.toGeoPoint
import org.osmdroid.api.IGeoPoint

fun IGeoPoint.toLocationRadians() = LocationRadians(latitude.toRadians(), longitude.toRadians())
fun IGeoPoint.midpoint(other: IGeoPoint) = toLocationRadians().midpoint(other.toLocationRadians()).toLocation().toGeoPoint()