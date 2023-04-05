package com.example.wiresag

fun Any.println() = println(this)
fun Any.println(prefix: String) = "$prefix: $this".println()