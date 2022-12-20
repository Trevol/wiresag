package com.example.wiresag.math

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

inline fun Offset.squareDistance(other: Offset) = pow2(x - other.x) + pow2(y - other.y)
fun Offset.distance(other: Offset) = sqrt(squareDistance(other))

inline fun Pair<Offset, Offset>.squareDistance() = first.squareDistance(second)
fun Pair<Offset, Offset>.distance() = sqrt(squareDistance())

