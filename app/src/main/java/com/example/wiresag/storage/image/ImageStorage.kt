package com.example.wiresag.storage.image

import android.graphics.Bitmap

interface ImageStorage {
    fun read(id: String): Bitmap?
    fun save(image: Bitmap): String
    fun update(id: String, image: Bitmap): Boolean
    fun delete(id: String): Boolean
}

