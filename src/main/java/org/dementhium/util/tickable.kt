package org.dementhium.util

import org.dementhium.event.Tickable
import org.dementhium.model.World

fun tickable(ticks: Int = 1, block: Tickable.() -> Unit) = object: Tickable(ticks) {
    override fun execute() = block()
}

fun World.submitTickable(ticks: Int, block: Tickable.() -> Unit) = submit(tickable(ticks, block))