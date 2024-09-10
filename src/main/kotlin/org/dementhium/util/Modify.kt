package org.dementhium.util

import org.dementhium.util.math.percentOf

fun interface Modify<T> {
    fun modifyValue(value: T): T
}
