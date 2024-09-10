package org.dementhium.util

fun interface Modify<T> {
    fun modifyValue(value: T): T
}
