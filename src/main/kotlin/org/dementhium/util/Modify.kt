package org.dementhium.util

import org.dementhium.util.math.percentOf

fun interface Modify<T> {
    fun modifyValue(value: T): T
}

@JvmInline
value class DoubleFlat(val flat: Int): Modify<Double> {
    override fun modifyValue(value: Double): Double = flat + value
}

@JvmInline
value class DoublePercent(val percent: Int): Modify<Double> {
    override fun modifyValue(value: Double): Double = percent percentOf value
}

@JvmInline
value class IntFlat(val flat: Int): Modify<Int> {
    override fun modifyValue(value: Int): Int = flat + value
}

@JvmInline
value class IntPercent(val percent: Int): Modify<Int> {
    override fun modifyValue(value: Int): Int = percent percentOf value
}

