package org.dementhium.util

import kotlin.math.roundToInt

val SecondDurationMs = 1000
val TickDurationMs = 600

val SecondToTickConverionRate = SecondDurationMs.toDouble() / TickDurationMs.toDouble()

inline fun secondsToTicks(seconds: Int): Int = (seconds * SecondToTickConverionRate).roundToInt()
inline fun minutesToTicks(minutes: Int): Int = secondsToTicks(minutes * 60)
