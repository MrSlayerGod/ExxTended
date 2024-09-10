package org.dementhium.action

import org.dementhium.model.Mob
import org.dementhium.model.World
import java.util.*

class ActionManager(val mob: Mob) {

    val queuedActions: Queue<Action> = LinkedList()

    var currentAction: Action = Action.NoAction

    fun appendAction(action: Action) {
        if (currentAction != Action.NoAction) {
            queuedActions.add(action)
        } else {
            currentAction = action
            currentAction.setEntity(mob)
            World.getWorld().submit(action)
        }
    }

    fun clearActions() {
        if (currentAction != Action.NoAction) {
            currentAction.stop()
            currentAction = Action.NoAction
        }
        queuedActions.clear()
        if (mob.isPlayer) {
            if (mob.player.getAttribute<Any?>("spellQueued") != null) {
                mob.player.removeAttribute("spellQueued")
            }
        }
        mob.stopCoordinateEvent()
        mob.mask.facePosition = null
        mob.mask.interactingEntity = null
        mob.combatState.victim = null
        if (mob.mask.interactingEntity != null) {
            mob.resetTurnTo()
        }
    }

    fun poll(): Action {
        return queuedActions.poll()
    }
}