package com.example.wiresag.ui.image

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap

fun ImageBitmap.rect() = Rect(0f, 0f, width - 1f, height - 1f)