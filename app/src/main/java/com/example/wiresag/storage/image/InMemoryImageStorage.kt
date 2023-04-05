package com.example.wiresag.storage.image

import android.graphics.Bitmap
import java.util.*

class InMemoryImageStorage : ImageStorage {
    private val images = mutableMapOf<String, Bitmap>()

    override fun read(id: String) = images[id]

    override fun save(image: Bitmap): String {
        val id = UUID.randomUUID().toString()
        images[id] = image
        return id
    }

    override fun update(id: String, image: Bitmap) {
        images[id] = image
    }

    override fun delete(id: String): Boolean {
        val imageExists = images.containsKey(id)
        if (imageExists) {
            images.remove(id)
        }
        return imageExists
    }

}

