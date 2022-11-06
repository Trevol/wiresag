package com.example.wiresag.model

import com.example.wiresag.utils.DMS

class Pylon(val latitude: Double, val longitude: Double) {
    val name = "Опора ${DMS(latitude)}/${DMS(longitude)}"
}