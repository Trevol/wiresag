package com.example.wiresag.ui.image

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap

val ImageBitmap.rect get() = Rect(0f, 0f, width - 1f, height - 1f)
val Bitmap.rect get() = Rect(0f, 0f, width - 1f, height - 1f)
