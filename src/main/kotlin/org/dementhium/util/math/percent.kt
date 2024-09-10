package org.dementhium.util.math

val Int.percent: Double get() = this / 100.0

infix fun Int.percentOf(other: Int): Int = (other * this.percent).toInt()

infix fun Int.percentOf(other: Double): Double = other * this.percent
