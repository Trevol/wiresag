package com.example.wiresag.state

import androidx.compose.ui.geometry.Offset
import com.example.wiresag.math.squareDistance
import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt

// ab - это обозначеные опоры, с - нижняя точка провисания.
// Предполагается, что ab > ac и ab > bc
data class SagTriangle(val a: Offset, val b: Offset, val c: Offset) {
    init {
        if (a == b || a == c || b == c) {
            throw Exception("a == b || a == c || b == c")
        }
    }

    private val abSquared = a.squareDistance(b)
    private val acSquared = a.squareDistance(c)

    private val bcSquared = b.squareDistance(c)
    val ab = sqrt(abSquared)
    val bc = sqrt(bcSquared)

    val ac = sqrt(acSquared)
    val angB = acos((abSquared + bcSquared - acSquared) / (2 * ab * bc))
    val angA = acos((abSquared + acSquared - bcSquared) / (2 * ab * ac))

    val sagPx = bc * sin(angB)
}

fun SagTriangle(unclassifiedVertices: List<Offset>): SagTriangle {
    val sides = listOf(
        unclassifiedVertices[0] to unclassifiedVertices[1],
        unclassifiedVertices[0] to unclassifiedVertices[2],
        unclassifiedVertices[1] to unclassifiedVertices[2]
    )
    val (ab, ac, bc) = sides.sortedByDescending { it.squareDistance() }
    val (a, b) = ab
    // C is common point of AC and BC
    val c = if (ac.first == bc.first || ac.first == bc.second) ac.first else ac.second
    return SagTriangle(a = a, b = b, c = c)
}