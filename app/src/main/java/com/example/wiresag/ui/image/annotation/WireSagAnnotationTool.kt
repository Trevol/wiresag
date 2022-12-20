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
import androidx.compose.ui.graphics.drawscope.DrawScope
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
    spanPhoto: WireSpanPhoto,
    onClose: () -> Unit
) {
    BackHandler(onBack = { onClose() })
    Box(modifier = modifier) {

        TransparentButton(onClose, Modifier.zIndex(1f)) {
            Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back))
        }

        var translation by rememberMutableStateOf(Offset.Zero)
        var scale by rememberMutableStateOf(1f)

        val imageBitmap = spanPhoto.photoWithGeoPoint.photo.asImageBitmap()

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
                    spanPhoto.points.add(imgPosition)
                }
            }
        ) {
            annotationAndSagEstimation(spanPhoto, scale)
        }
    }

}

private fun DrawScope.annotationAndSagEstimation(spanPhoto: WireSpanPhoto, scale: Float) {
    spanPhoto.points.forEachIndexed { i, it ->
        drawCircle(Color.Green, 5f, it)
        if (i > 0) {
            drawLine(Color.Green, it, spanPhoto.points[i - 1])
        }
    }
    if (spanPhoto.points.size == 3) {
        drawLine(Color.Green, spanPhoto.points.first(), spanPhoto.points.last())
    }
}