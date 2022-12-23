package com.example.wiresag.utils

fun <T> MutableList<T>.addItem(item: T) = item.also { add(it) }

fun <T : R, R> T.addTo(list: MutableList<R>) = apply { list.add(this@addTo) }