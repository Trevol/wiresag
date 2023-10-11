package com.example.wiresag.utils

import kotlin.math.min

fun <T> List<T>.pairwise(): Sequence<Pair<T, T>> {
    return sequence {
        sublistSafe(0, lastIndex - 1).forEachIndexed { i, it1 ->
            sublistSafe(i + 1, lastIndex).forEach { it2 ->
                yield(it1 to it2)
            }
        }
    }
}

private fun <T> List<T>.sublistSafe(fromIndex: Int, toIndex: Int): List<T> {
    return subList(fromIndex, min(size, toIndex + 1))
}