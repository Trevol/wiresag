package com.example.wiresag.ui.image

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.wiresag.ui.input.NoRemappedClick
import com.example.wiresag.ui.input.RemappedClick
import com.example.wiresag.ui.input.transformableAndClickable

@Composable
fun LayeredImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    translation: Offset = Offset.Zero,
    scale: Float = 1f,
    onTransformationChange: (translation: Offset, scale: Float) -> Unit,
    onClick: RemappedClick = NoRemappedClick,
    onLongClick: RemappedClick = NoRemappedClick,
    onLayerDraw: DrawScope.() -> Unit
) {

    Canvas(
        modifier = modifier
            .transformableAndClickable(
                translation,
                scale,
                onTransformationChange,
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        drawImage(image)
        onLayerDraw()
    }
}


/*
@Composable
fun Example() {
    val imageBitmap = remember {
        testBitmap().asImageBitmap()
    }
    Box {
        var translation by rememberMutableStateOf(Offset(100f, 100f))
        var scale by rememberMutableStateOf(1f)
        val clicks = remember { mutableStateListOf<Offset>() }

        LayeredImage(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
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
            drawCircle(
                color = Color.DarkGray,
                50f,
                center = Offset(
                    imageBitmap.width - 175f,
                    imageBitmap.height - 275f
                ),
            )


            clicks.forEachIndexed { i, it ->
                drawCircle(
                    color = Color.DarkGray,
                    5f,
                    center = it,
                )
                if (i > 0) {
                    drawLine(Color.Red, it, clicks[i - 1])
                }
            }
        }
    }
}

private fun testBitmap(): Bitmap {
    //val (w, h) = 400 to 300
    val (w, h) = 4000 to 3000
    val borderWidth = min(w, h) / 10f
    val halfBorder = borderWidth / 2

    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    Canvas(bmp).apply {
        drawARGB(255, 200, 100, 0)

        drawRect(
            halfBorder, halfBorder, bmp.width - halfBorder, bmp.height - halfBorder,
            Paint().apply {
                color = android.graphics.Color.GREEN
                style = Paint.Style.STROKE
                strokeWidth = borderWidth
            }
        )

        val (cx, cy) = bmp.width / 2f to bmp.height / 2f
        val l = min(bmp.width, bmp.height) / 4
        drawOval(
            cx - l, cy - 1.5f * l, cx + l, cy + 1.5f * l,
            Paint().apply { color = android.graphics.Color.MAGENTA; style = Paint.Style.FILL; }
        )
    }
    return bmp
}
*/
