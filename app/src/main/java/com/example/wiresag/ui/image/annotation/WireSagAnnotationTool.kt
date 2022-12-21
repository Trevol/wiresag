package com.example.wiresag.ui.image.annotation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.zIndex
import com.example.wiresag.state.SagTriangle
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.components.TransparentButton
import com.example.wiresag.ui.image.LayeredImage
import com.example.wiresag.ui.image.rect
import com.example.wiresag.utils.rememberMutableStateOf

@Composable
fun WireSagAnnotationTool(
    modifier: Modifier,
    spanPhoto: WireSpanPhoto,
    onClose: () -> Unit,
    onDelete: (WireSpanPhoto) -> Unit
) {
    BackHandler(onBack = { onClose() })
    Box(modifier = modifier) {

        Row(Modifier.zIndex(1f)) {
            TransparentButton(onClose) {
                Icon(Icons.Outlined.ArrowBack)
            }
            Spacer(modifier = Modifier.weight(1f))
            TransparentButton(onClick = { spanPhoto.annotation.points.clear() }) {
                Icon(Icons.Outlined.Refresh)
            }
            TransparentButton(onClick = { onDelete(spanPhoto) }) {
                Icon(Icons.Outlined.Delete)
            }
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
                    spanPhoto.annotation.points.add(imgPosition)
                }
            }
        ) {
            drawAnnotation(spanPhoto, scale)
        }
    }

}

private fun DrawScope.drawAnnotation(spanPhoto: WireSpanPhoto, scale: Float) {
    if (spanPhoto.annotation.triangle != null) {
        drawAnnotatedTriangle(spanPhoto.annotation.triangle!!)
    } else {
        drawAnnotatedPoints(spanPhoto.annotation.points)
    }
}

private fun DrawScope.drawAnnotatedTriangle(triangle: SagTriangle) {
    drawCircle(Color.Green, 5f, triangle.a)
    drawCircle(Color.Green, 5f, triangle.b)
    drawCircle(Color.Green, 5f, triangle.c)
    drawLine(Color.Green, triangle.a, triangle.b)
    drawLine(Color.Green, triangle.a, triangle.c)
    drawLine(Color.Green, triangle.b, triangle.c)
}

private fun DrawScope.drawAnnotatedPoints(points: List<Offset>) {
    points.forEachIndexed { i, it ->
        drawCircle(Color.Blue, 5f, it)
        if (i > 0) {
            drawLine(Color.Blue, it, points[i - 1])
        }
    }
}