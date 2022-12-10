package com.example.wiresag.math

inline fun radians(angdeg: Double) = Math.toRadians(angdeg)
inline fun radians(angdeg: Int) = radians(angdeg.toDouble())
inline fun degrees(angrad: Double) = Math.toDegrees(angrad)

inline fun Double.toRadians() = radians(this)
inline fun Int.toRadians() = radians(this)
inline fun Double.toDegrees() = degrees(this)