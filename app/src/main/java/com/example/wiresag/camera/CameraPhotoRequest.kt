package com.example.wiresag.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File

interface PhotoRequest {
    fun takePhoto(photo: (Bitmap?) -> Unit)
}

class CameraPhotoRequest(
    val context: ComponentActivity,
    val tmpImagesDirectory: File,
    val authority: String,
    val tmpFilePrefix: () -> String = { "tmp_" }
) : PhotoRequest {
    val action = context.registerTakePictureAction()

    override fun takePhoto(photo: (Bitmap?) -> Unit) {
        val imageFile = File.createTempFile(tmpFilePrefix(), ".jpg", tmpImagesDirectory)
        action(FileProvider.getUriForFile(context, authority, imageFile)) { taken ->
            val bmp = if (taken) {
                imageFile.inputStream().use { BitmapFactory.decodeStream(it) }
            } else {
                null
            }
            photo(bmp)
        }
    }
}

private fun ActivityResultCaller.registerTakePictureAction(): (uri: Uri, result: (taken: Boolean) -> Unit) -> Unit {
    var resultCallback: (Boolean) -> Unit = {}
    val launcher = registerForActivityResult(ActivityResultContracts.TakePicture()) { taken ->
        resultCallback(taken)
    }

    return { uri, cb ->
        resultCallback = cb
        launcher.launch(uri)
    }
}
