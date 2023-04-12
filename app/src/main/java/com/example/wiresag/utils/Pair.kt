package com.example.wiresag.utils

inline fun <T, R> Pair<T, T>.map(crossinline transform: (T) -> R) =
    transform(first) to transform(second)

inline fun <T> Pair<T, T>.forEach(crossinline action: (T) -> Unit) {
    action(first)
    action(second)
}