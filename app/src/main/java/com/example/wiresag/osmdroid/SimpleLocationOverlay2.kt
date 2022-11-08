package com.example.wiresag.osmdroid

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay

class SimpleLocationOverlay2(theIcon: Bitmap) : SimpleLocationOverlay(theIcon) {
    init {
        //setPersonIcon(theIcon, Point(theIcon.width / 2, theIcon.height / 2))
        PERSON_HOTSPOT = Point(theIcon.width / 2, theIcon.height / 2)
    }

    override fun draw(c: Canvas?, pj: Projection?) {
        if (myLocation == null) {
            return
        }
        super.draw(c, pj)
    }
}