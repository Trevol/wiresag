package com.example.wiresag.ui.image.annotation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.wiresag.math.toDegrees
import com.example.wiresag.state.SagTriangle
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.image.LayeredImage
import com.example.wiresag.ui.image.rect
import com.example.wiresag.ui.input.TransformParameters
import com.example.wiresag.utils.rememberMutableStateOf
import com.example.wiresag.utils.round

@Composable
fun WireSagAnnotationTool(
    modifier: Modifier,
    spanPhoto: WireSpanPhoto,
    onClose: () -> Unit,
    onDelete: (WireSpanPhoto) -> Unit
) {
    var transform by rememberMutableStateOf(TransformParameters())
    val imageBitmap = spanPhoto.photoWithGeoPoint.photo.asImageBitmap()

    BackHandler(onBack = { onClose() })
    Box(modifier = modifier.background(Color.White)) {

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
                        .clickable { spanPhoto.annotation.points.clear() }
                )
                /*TransparentButton(onClick = { spanPhoto.annotation.points.clear() }) {
                    Icon(Icons.Outlined.Refresh)
                }*/

                Icon(Icons.Outlined.Delete,
                    modifier = Modifier
                        .padding(ButtonDefaults.ContentPadding)
                        .background(Color(127, 127, 127, 100))
                        .clickable { onDelete(spanPhoto) }
                )
                /*TransparentButton(onClick = { onDelete(spanPhoto) }) {
                    Icon(Icons.Outlined.Delete)
                }*/
            }
            Spacer(Modifier.weight(1f))
            SagInfo(spanPhoto)
        }

        LayeredImage(
            modifier = Modifier.fillMaxSize(),
            image = imageBitmap,
            transform = transform,
            onTransform = { transform = it },
            onClick = { _, imgPosition ->
                spanPhoto.annotation.tryAddOrReplace(imgPosition)
            }
        ) {
            drawAnnotation(spanPhoto, transform.scale)
        }
    }

}

@Composable
private fun SagInfo(photo: WireSpanPhoto) {
    val triangle = photo.annotation.triangle
    if (triangle != null) {
        Row(
            modifier = Modifier.background(Color.Gray),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("AB: ${photo.span.length.round(2)}m")
            Text("∠A: ${triangle.angA.toDegrees().round(1)}°")
            Text("∠B: ${triangle.angB.toDegrees().round(1)}°")
            Text("Sag: ${photo.estimatedWireSag?.round(2)}m")
        }
    }
}

private fun DrawScope.drawAnnotation(spanPhoto: WireSpanPhoto, scale: Float) {
    if (spanPhoto.annotation.triangle != null) {
        drawAnnotatedTriangle(spanPhoto.annotation.triangle!!)
    } else {
        drawPoints(spanPhoto.annotation.points, Color.Blue)
    }
}

private val vertexLabelPaint = NativePaint().apply {
    color = android.graphics.Color.GREEN
    textSize = 24f
}

private fun DrawScope.drawText(text: String, textStart: Offset, paint: NativePaint) {
    drawContext.canvas.nativeCanvas.drawText(text, textStart.x, textStart.y, paint)
}

private fun DrawScope.drawAnnotatedTriangle(triangle: SagTriangle) {
    drawCircle(Color.Green, 5f, triangle.a)
    drawCircle(Color.Green, 5f, triangle.b)
    drawCircle(Color.Green, 5f, triangle.c)

    drawLine(Color.Green, triangle.a, triangle.b)
    drawLine(Color.Green, triangle.a, triangle.c)
    drawLine(Color.Green, triangle.b, triangle.c)

    drawText("A", triangle.a + Offset(0f, -15f), vertexLabelPaint)
    drawText("B", triangle.b + Offset(0f, -15f), vertexLabelPaint)
    drawText("C", triangle.c + Offset(0f, 24 + 5f), vertexLabelPaint)
}

private fun DrawScope.drawPoints(points: List<Offset>, color: Color) {
    points.forEachIndexed { i, it ->
        drawCircle(color, 5f, it)
        if (i > 0) {
            drawLine(color, it, points[i - 1])
        }
    }
}