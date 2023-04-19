package com.example.wiresag.viewModel.views.annotation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.wiresag.math.toDegrees
import com.example.wiresag.state.SagTriangle
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.state.WireSpanPhotoEstimations
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.image.LayeredImage
import com.example.wiresag.ui.image.rect
import com.example.wiresag.ui.input.TransformParameters
import com.example.wiresag.ui.stopClickPropagation
import com.example.wiresag.utils.rememberDerivedStateOf
import com.example.wiresag.utils.rememberMutableStateOf
import com.example.wiresag.utils.round

@Composable
fun WireSagAnnotationTool(
    modifier: Modifier,
    spanPhoto: WireSpanPhoto,
    imageById: (id: String) -> Bitmap?,
    onClose: () -> Unit,
    onDelete: (WireSpanPhoto) -> Unit
) {
    val photo = imageById(spanPhoto.photoWithGeoPoint.photoId)

    BackHandler(onBack = { onClose() })
    Box(modifier.fillMaxSize()) {
        if (photo != null) {
            Annotation(spanPhoto, photo, onClose, onDelete)
        } else {
            ImageNotFound(spanPhoto.photoWithGeoPoint.photoId, onClose)
        }
    }

}

@Composable
fun Annotation(
    spanPhoto: WireSpanPhoto,
    photo: Bitmap,
    onClose: () -> Unit,
    onDelete: (WireSpanPhoto) -> Unit
) {
    var transform by rememberMutableStateOf(TransformParameters())
    val estimations by rememberDerivedStateOf { spanPhoto.wireAnnotation.estimations() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Row {
            Icon(Icons.Outlined.Done,
                modifier = Modifier
                    .padding(ButtonDefaults.ContentPadding)
                    .background(Color(127, 127, 127, 100))
                    .clickable { onClose() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(Icons.Outlined.Refresh,
                modifier = Modifier
                    .padding(ButtonDefaults.ContentPadding)
                    .background(Color(127, 127, 127, 100))
                    .clickable { spanPhoto.wireAnnotation.points.clear() }
            )

            Icon(Icons.Outlined.Delete,
                modifier = Modifier
                    .padding(ButtonDefaults.ContentPadding)
                    .background(Color(127, 127, 127, 100))
                    .clickable { onDelete(spanPhoto) }
            )
        }
        Spacer(Modifier.weight(1f))
        SagInfo(estimations)
    }

    LayeredImage(
        modifier = Modifier.fillMaxSize(),
        image = photo.asImageBitmap(),
        transform = transform,
        onTransform = { transform = it },
        onClick = {
            val pointOnImage = it.layerPosition
            if (photo.rect.contains(pointOnImage)) {
                spanPhoto.wireAnnotation.tryAddOrUpdatePoint(pointOnImage)
            }
        }
    ) {
        draw(spanPhoto.wireAnnotation.points, estimations, transform.scale)
    }
}


@Composable
private fun ImageNotFound(imageId: String, onClose: () -> Unit) = Row {
    Icon(Icons.Outlined.ArrowBack,
        modifier = Modifier
            .padding(ButtonDefaults.ContentPadding)
            .clickable { onClose() }
    )
    Text("Image ($imageId) not found")
}

@Composable
private fun SagInfo(estimations: WireSpanPhotoEstimations?) {
    if (estimations != null) {
        Row(
            modifier = Modifier.background(Color.Gray),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("AB: ${estimations.photo.span.length.round(2)}m")
            Text("∠A: ${estimations.triangle.angA.toDegrees().round(1)}°")
            Text("∠B: ${estimations.triangle.angB.toDegrees().round(1)}°")
            Text("Sag: ${estimations.estimatedWireSag.round(2)}m")
        }
    }
}

private fun DrawScope.draw(
    wireAnnotationPoints: List<Offset>,
    estimations: WireSpanPhotoEstimations?,
    scale: Float
) {
    if (estimations != null) {
        drawEstimations(estimations, scale)
    } else {
        drawPoints(wireAnnotationPoints, Color.Blue, scale)
    }
}

private fun vertexLabelPaint(scale: Float) = NativePaint().apply {
    color = android.graphics.Color.GREEN
    textSize = 24f / scale
}

private fun DrawScope.drawText(text: String, textStart: Offset, paint: NativePaint) {
    drawContext.canvas.nativeCanvas.drawText(text, textStart.x, textStart.y, paint)
}

private fun radius(scale: Float) = 5f / scale

private fun DrawScope.drawEstimations(estimations: WireSpanPhotoEstimations, scale: Float) {
    drawPoints(
        estimations.wireCurvePoints,
        pointMode = PointMode.Polygon,
        color = Color.Red,
        strokeWidth = 1f / scale
    )
    val radius = radius(scale)
    drawCircle(Color.Red, radius, estimations.wireCurve.vertex)

    drawPoints(estimations.photo.wireAnnotation.points, Color.Blue, scale)

    drawLine(Color.Green, estimations.triangle.a, estimations.triangle.b)
    drawLine(Color.Green, estimations.triangle.a, estimations.triangle.c)
    drawLine(Color.Green, estimations.triangle.b, estimations.triangle.c)

    val labelPaint = vertexLabelPaint(scale)
    drawText("A", estimations.triangle.a + Offset(0f, -15f) / scale, labelPaint)
    drawText("B", estimations.triangle.b + Offset(0f, -15f) / scale, labelPaint)
    drawText("C", estimations.triangle.c + Offset(0f, 24 + 5f) / scale, labelPaint)
}

private fun DrawScope.drawPoints(points: List<Offset>, color: Color, scale: Float) {
    val radius = radius(scale)
    points.forEachIndexed { i, it ->
        drawCircle(color, radius, it)
    }
}