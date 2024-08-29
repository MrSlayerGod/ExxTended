package org.dementhium.event

abstract class Tickable(var cycles: Int) {

    var attempts: Int = 0

    var running = true

    fun isRunning(): Boolean = running

    open fun stop() { running = false }

    fun run() {
        if (attempts++ >= cycles) {
            attempts = 0
            execute()
        }
    }

    abstract fun execute()
}
