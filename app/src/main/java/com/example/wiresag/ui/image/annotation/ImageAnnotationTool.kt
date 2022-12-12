package com.example.wiresag.ui.image.annotation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.example.wiresag.R
import com.example.wiresag.ui.image.zoomable.ZoomableImage

@ExperimentalFoundationApi
@Composable
fun ImageAnnotationTool(image: Bitmap, onClose: () -> Unit, modifier: Modifier) {
    BackHandler(onBack = { onClose() })
    Box(modifier = modifier) {
        FloatingActionButton(onClick = { onClose() }, modifier = Modifier.zIndex(1f)) {
            Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back))
        }
        /*Image(
            image.asImageBitmap(),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )*/
        ZoomableImage(
            painter = BitmapPainter(image.asImageBitmap()),
            modifier = Modifier.fillMaxSize(),
            backgroundColor = Color.White,
            contentScale = ContentScale.Crop
        )
    }

}