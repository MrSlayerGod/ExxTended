package org.dementhium.util

import org.dementhium.event.Tickable

fun Tickable(ticks: Int = 1, block: Tickable.() -> Unit) = object: Tickable(ticks) {
    override fun execute() = block()
}
