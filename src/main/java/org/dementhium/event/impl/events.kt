package org.dementhium.event.impl

import org.dementhium.event.Event
import org.dementhium.model.World
import org.dementhium.task.impl.ClanSaveTask

class AutoSaveEvent: Event(600) {
    override fun run() {
        val world = World.getWorld()
        world.executor.submitWork {
            synchronized(world.players) {
                world.players.forEach { player -> world.playerLoader.save(player) }
            }
        }
    }
}

class ClanSaveEvent : Event(6000) {
    override fun run() { World.getWorld().submit(ClanSaveTask()) }
}
