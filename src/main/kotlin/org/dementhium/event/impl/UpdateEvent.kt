package org.dementhium.event.impl

import org.dementhium.event.Event
import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.task.ConsecutiveTask
import org.dementhium.task.ParallelTask
import org.dementhium.task.Task
import org.dementhium.task.impl.*

class UpdateEvent : Event(TICK_MS) {

    override fun run() {

        val world = World.getWorld()

        world.processTickables()
        val tickTasks: MutableList<Task> = ArrayList()
        val updateTasks: MutableList<Task> = ArrayList()
        val resetTasks: MutableList<Task> = ArrayList()

        fun Player.addPlayerTasks() {
            tickTasks.add(PlayerTickTask(this))
            updateTasks.add(PlayerUpdateTask(this))
            resetTasks.add(PlayerResetTask(this))
        }

        synchronized(world.npcs) {
            world.npcs.forEach { npc ->
                tickTasks.add(NPCTickTask(npc))
                resetTasks.add(NPCResetTask(npc))
            }
        }

        for (npc in world.npcs) {
            tickTasks.add(NPCTickTask(npc))
            resetTasks.add(NPCResetTask(npc))
        }

        world.players.forEach { player ->
            val isDisconnected = player.connection.isDisconnected
            val hasLastAttacker = player.combatState.lastAttacker != null
            if (isDisconnected) {
                when {
                    hasLastAttacker -> player.logoutDelay = 60
                    player.logoutDelay > 0 -> {
                        player.logoutDelay--
                        player.addPlayerTasks()
                    }
                    player.skills.hitPoints <= 0 -> {
                        player.logoutDelay = 60
                        player.skills.sendDead()
                        player.addPlayerTasks()
                    }
                    else -> {
                        world.unregister(player)
                        world.players.remove(player)
                    }
                }
            } else if (player.isOnline) {
                player.addPlayerTasks()
            }
        }


        world.lobbyPlayers.forEach { player ->
            when {
                player.connection.isDisconnected || player.isOnline -> world.lobbyPlayers.remove(player)
                player.connection.isInLobby -> tickTasks.add(PlayerTickTask(player))
            }
        }

        val tickTask: Task = ConsecutiveTask(*tickTasks.toTypedArray<Task>())
        val updateTask: Task = ParallelTask(updateTasks)
        val resetTask: Task = ParallelTask(resetTasks)

        world.submit(ConsecutiveTask(tickTask, updateTask, resetTask))
    }

    companion object {
        const val TICK_MS = 600
    }
}