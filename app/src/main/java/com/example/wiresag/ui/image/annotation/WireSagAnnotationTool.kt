package com.example.wiresag.ui.image.annotation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.example.wiresag.R
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.ui.components.TransparentButton
import com.example.wiresag.ui.image.LayeredImage
import com.example.wiresag.ui.image.rect
import com.example.wiresag.utils.rememberMutableStateOf


@Composable
fun WireSagAnnotationTool(
    modifier: Modifier,
    wireSpanPhoto: WireSpanPhoto,
    onClose: () -> Unit
) {
    BackHandler(onBack = { onClose() })
    Box(modifier = modifier) {

        TransparentButton(onClose, Modifier.zIndex(1f)) {
            Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back))
        }

        var translation by rememberMutableStateOf(Offset.Zero)
        var scale by rememberMutableStateOf(1f)
        val clicks = remember { mutableStateListOf<Offset>() }

        val imageBitmap = wireSpanPhoto.photoWithGeoPoint.photo.asImageBitmap()

        LayeredImage(
            modifier = Modifier.fillMaxSize(),
            image = imageBitmap,
            translation = translation,
            scale = scale,
            onTransformationChange = { pan, zoom ->
                translation = pan
                scale = zoom
            },
            onClick = { _, imgPosition ->
                if (imageBitmap.rect().contains(imgPosition)) {
                    clicks.add(imgPosition)
                }
            },
            onLongClick = { _, _ -> clicks.clear() }
        ) {

            clicks.forEachIndexed { i, it ->
                drawCircle(
                    color = Color.Green,
                    5f,
                    center = it,
                )
                if (i > 0) {
                    drawLine(Color.Green, it, clicks[i - 1])
                }
            }
        }
    }

}