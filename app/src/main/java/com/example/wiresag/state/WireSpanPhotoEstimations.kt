package com.example.wiresag.state

import androidx.compose.ui.geometry.Offset
import com.example.wiresag.math.Parabola
import com.example.wiresag.math.squareDistance
import com.example.wiresag.utils.pairwise

class WireSpanPhotoEstimations(val photo: WireSpanPhoto) {
    init {
        if (!photo.wireAnnotation.points.isFull()) {
            throw Exception("Unexpected: !photo.wirePoints.isFull()")
        }
    }

    val anchorPoints = photo.wireAnnotation.points.pairwise().maxBy { it.squareDistance() }
    val wireCurve = Parabola(photo.wireAnnotation.points)
    val wireCurvePoints = (anchorPoints.first.x.toInt()..anchorPoints.second.x.toInt())
        .map { it.toFloat() }
        .map { x -> Offset(x, wireCurve(x)) }
    val triangle =
        SagTriangle(a = anchorPoints.first, b = anchorPoints.second, c = wireCurve.vertex)
    val estimatedWireSag = triangle.estimatedWireSag(photo.span.length)
}