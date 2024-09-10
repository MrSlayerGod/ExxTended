package org.dementhium.util.delegate

import kotlin.reflect.KMutableProperty0

fun <T> propertyDelegate(property: KMutableProperty0<T>) = GetSetDelegate(property::get, property::set)
