package org.dementhium.util.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class GetSetDelegate<T>(
    inline val get: () -> T,
    inline val set: (T) -> Unit
): ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = set(value)
}