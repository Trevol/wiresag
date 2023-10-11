package com.example.wiresag

import java.io.File

data class AppSettings(
    val dataDirectory: File,
    val imagesDirectory: File,
    val photoRequestTmpDirectory: File,
    val spanImagesDirectory: File
)