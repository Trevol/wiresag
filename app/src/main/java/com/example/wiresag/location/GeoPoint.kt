package com.example.wiresag.location

import com.example.wiresag.math.toRadians
import org.osmdroid.api.IGeoPoint

fun IGeoPoint.toLocationRadians() = LocationRadians(latitude.toRadians(), longitude.toRadians())