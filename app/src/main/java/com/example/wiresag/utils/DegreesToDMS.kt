package com.example.wiresag.utils

import android.location.Location
import kotlin.jvm.JvmInline
import kotlin.math.*

typealias Degrees = Float
typealias DegreesDouble = Double

@JvmInline
value class DMS(val rawDegrees: DegreesDouble) {
    val degreesOnly
        get() = DegreesOnly(rawDegrees)
    val degreesMinutes
        get() = DegreesMinutes(rawDegrees)
    val degreesSeconds
        get() = DegreesSeconds(rawDegrees)

    override fun toString() = "$degreesOnly$degreesMinutes$degreesSeconds"
}

fun DMS.prettyFormat() =
    "${degreesOnly}°${degreesMinutes}'${degreesSeconds.seconds.zeroPad(2)}.${degreesSeconds.secondsFraction}\""

data class DMSLocation(
    val longitude: DMS,
    val latitude: DMS
)

fun Location.toDMS() = DMSLocation(DMS(longitude), DMS(latitude))
fun DMSLocation.toLanLotString() = "${latitude}/${longitude}"

@JvmInline
value class DegreesOnly(private val rawDegrees: DegreesDouble) {
    val degrees: Int
        get() {
            val degrees = rawDegrees.toInt()
            //проверка на переполнение при округлении секунд
            if (rawDegrees.secondsWithSingleFraction() == 60.0 && rawDegrees.minutes() == 59) {
                return degrees + 1
            }
            return degrees
        }

    override fun toString(): String = degrees.let {
        if (it >= 0) {
            it.zeroPad(0)
        } else {
            "-${abs(it).zeroPad(2)}"
        }
    }
}

@JvmInline
value class DegreesMinutes(private val rawDegrees: DegreesDouble) {
    val minutes: Int
        get() {
            val minutes = rawDegrees.minutes()
            if (rawDegrees.secondsWithSingleFraction() == 60.0){
                return if (minutes == 59){
                    0
                } else
                    minutes + 1
            }
            return minutes
        }

    override fun toString(): String = minutes.zeroPad(2)
}

@JvmInline
value class DegreesSeconds(private val rawDegrees: DegreesDouble){
    val seconds: Int
        get() {
            val secondsWithSingleFraction = rawDegrees.secondsWithSingleFraction()
            if (secondsWithSingleFraction == 60.0){
                // округлилось с добавлением минуты. В расчетах минут д.б. учтено
                return 0
            }
            return secondsWithSingleFraction.toInt()
        }
    val secondsFraction: Int
        get() {
            val secondsWithSingleFraction = rawDegrees.secondsWithSingleFraction()
            if (secondsWithSingleFraction == 60.0){
                return 0
            }
            return round(secondsWithSingleFraction
                .fraction()*10) // fraction (0 .. 9)
                .toInt()
        }
    override fun toString(): String = "${seconds.zeroPad(2)}$secondsFraction"
}

const val minutesInDegree = 60
const val secondsInMinute = 60

private fun DegreesDouble.minutesWithFractions() = fraction() * minutesInDegree
private fun DegreesDouble.minutes() = minutesWithFractions().toInt()
private fun DegreesDouble.secondsWithAllFractions() = minutesWithFractions().fraction() * secondsInMinute
private fun DegreesDouble.secondsWithSingleFraction() = secondsWithAllFractions().toFixed(1)
