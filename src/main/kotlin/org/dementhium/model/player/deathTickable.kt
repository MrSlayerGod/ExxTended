package org.dementhium.model.player

import org.dementhium.event.Tickable
import org.dementhium.model.World
import org.dementhium.model.player.skills.Skills.Companion.SKILL_COUNT
import org.dementhium.net.ActionSender
import org.dementhium.util.tickable

fun deathTickable(player: Player) = tickable(4) { with(player.skills) {
    for (i in 0 until SKILL_COUNT) {
        set(i, getLevelForXp(i))
    }
    player.giveKiller()
    ActionSender.sendChatMessage(player, 0, "Oh dear, you are dead.")
    player.teleport(3162, 3484, 0)
    World.getWorld().submit(object : Tickable(1) {
        override fun execute() {
            hitPoints = maxHitpoints
            sendHitpoints()
            player.setSpecialAmount(1000, true)
            player.walkingQueue.runEnergy = 100
            this.stop()
        }
    })
    player.headIcons.isSkulled = false
    player.teleblock.isTeleblocked = false
    player.resetCombat()
    player.combatState.setFrozenTime(0)
    player.setAttribute(
        "vengDelay",
        System.currentTimeMillis() - (player.getAttribute("vengDelay", 0L) as Long) + 30000
    )
    player.setAttribute("vengeance", false)
    player.clearEnemyHits()
    hasSentDead = false
    stop()
}}
