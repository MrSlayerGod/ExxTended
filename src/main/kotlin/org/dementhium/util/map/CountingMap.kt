package org.dementhium.util.map

class CountingMap<T>: MutableMap<T, Int> by mutableMapOf() {
    fun inc(key: T) {
        set(key, getOrDefault(key, 0) + 1)
    }
}
