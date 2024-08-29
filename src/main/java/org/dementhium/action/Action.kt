package org.dementhium.action

import org.dementhium.event.Tickable
import org.dementhium.model.Mob
import kotlin.properties.Delegates

abstract class Action(cycles: Int) : Tickable(cycles) {

    protected var mob: Mob by Delegates.notNull()

    fun setEntity(mob: Mob) { this.mob = mob }

    override fun stop() {
        super.stop()
        mob.actionManager.appendAction(mob.actionManager.poll())
    }
}
