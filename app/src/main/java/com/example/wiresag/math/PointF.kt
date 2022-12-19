package com.example.wiresag.math

import android.graphics.PointF
import kotlin.math.sqrt

inline fun PointF.squareDistance(other: PointF) = pow2(x - other.x) + pow2(y - other.y)
fun PointF.distance(other: PointF) = sqrt(squareDistance(other))