package com.example.wiresag.ui.image.annotation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.example.wiresag.R

@Composable
fun ImageAnnotationTool(image: Bitmap, onClose: () -> Unit, modifier: Modifier) {
    BackHandler(onBack = { onClose() })

    Image(
        image.asImageBitmap(),
        contentDescription = "",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )

    FloatingActionButton(onClick = { onClose() }) {
        Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back))
    }

}