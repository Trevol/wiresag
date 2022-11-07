package com.example.wiresag.model

import android.location.Location
import com.example.wiresag.utils.DMS

class Pylon(val location: Location) {
    val name = "Опора ${DMS(location.latitude)}/${DMS(location.longitude)}"
}