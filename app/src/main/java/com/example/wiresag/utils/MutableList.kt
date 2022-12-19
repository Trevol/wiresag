package com.example.wiresag.utils

fun <T> MutableList<T>.addItem(item: T) = item.also { add(it) }