package com.example.wiresag.storage.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class FileImageStorage(val storageDirectory: File) : ImageStorage {

    override fun read(id: String): Bitmap? {
        val imageFile = imageFile(id)
        if (!imageFile.exists()) {
            return null
        }
        return FileInputStream(imageFile).use {
            BitmapFactory.decodeStream(it)
        }
    }

    override fun save(image: Bitmap): String {
        val id = UUID.randomUUID().toString()
        update(id, image)
        return id
    }

    override fun update(id: String, image: Bitmap) {
        FileOutputStream(imageFile(id)).use {
            image.compress(Bitmap.CompressFormat.JPEG, 80, it)
        }
    }

    override fun delete(id: String): Boolean {
        val imageFile = imageFile(id)
        val exists = imageFile.exists()
        if (exists) {
            imageFile.delete()
        }
        return exists
    }

    private fun imageFile(id: String): File {
        return File(storageDirectory, "$id.jpg")
    }
}