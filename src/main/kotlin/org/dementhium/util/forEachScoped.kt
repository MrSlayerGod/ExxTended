package org.dementhium.util

public inline fun <T> Iterable<T>.forEachScoped(action: T.() -> Unit) {
    for (element in this) element.action()
}
