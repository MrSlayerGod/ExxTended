package org.dementhium.event

abstract class Event(var delay: Int) {
    var running: Boolean = true
    open fun stop() { this.running = false }
    open fun isRunning() = running
    abstract fun run()
}
