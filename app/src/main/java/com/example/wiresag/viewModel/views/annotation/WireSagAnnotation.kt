package com.example.wiresag.viewModel.views.annotation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.wiresag.viewModel.WireSagViewModel

@Composable
fun WireSagViewModel.WireSagAnnotation() {
    if (photoForAnnotation != null) {
        WireSagAnnotationTool(
            modifier = Modifier.fillMaxSize(),
            spanPhoto = photoForAnnotation!!,
            imageById = { id -> objectContext.readImage(id) },
            onClose = {
                photoForAnnotation = null
            },
            onDelete = {
                if (photoForAnnotation != null) {
                    objectContext.deleteSpanPhoto(photoForAnnotation!!)
                    photoForAnnotation = null
                }
            }
        )
    }
}
