package com.example.wiresag.osmdroid

import android.graphics.Bitmap
import android.graphics.Canvas
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay

class SimpleLocationOverlay2(theIcon: Bitmap): SimpleLocationOverlay(theIcon) {
    override fun draw(c: Canvas?, pj: Projection?) {
        if (myLocation == null){
            return
        }
        super.draw(c, pj)
    }
}