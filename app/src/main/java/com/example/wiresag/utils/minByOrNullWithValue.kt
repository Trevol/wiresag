package com.example.wiresag.utils

inline fun <T, R : Comparable<R>> Iterable<T>.minByOrNullWithValue(selector: (T) -> R): Pair<T?, R?> {
    val iterator = iterator()
    if (!iterator.hasNext()) return null to null
    var minElem = iterator.next()
    if (!iterator.hasNext()) return minElem to null
    var minValue = selector(minElem)
    do {
        val e = iterator.next()
        val v = selector(e)
        if (minValue > v) {
            minElem = e
            minValue = v
        }
    } while (iterator.hasNext())
    return minElem to minValue
}