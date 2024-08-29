package org.dementhium.event.impl

import org.dementhium.event.Tickable
import org.dementhium.model.player.Player
import org.dementhium.model.player.Skills
import org.dementhium.net.ActionSender
import org.dementhium.util.direction

object PlayerRestoration {
    const val specialTimer = 50
    const val runEnergyTimer =  2
    const val levelNormalizationTimer = 90
    const val passiveHealTimer = 10
    const val maxSpecialAmount = 1_000
    const val maxRunEnergy = 100
    const val specialRestoreMax = 100
    const val runEnergyRestoration = 1
    const val levelNormalizationAmount = 1
    const val healAmount = 1
}

class PlayerRestorationTick(private val player: Player) : Tickable(1) {

    private var specialTimer = PlayerRestoration.specialTimer
    private var runEnergyTimer = PlayerRestoration.runEnergyTimer
    private var levelNormalizationTimer = PlayerRestoration.levelNormalizationTimer
    private var passiveHealTimer = PlayerRestoration.passiveHealTimer

    override fun execute() {
        val isOnline = player.isOnline
        if (!isOnline) return stop()
        if (player.specialAmount < PlayerRestoration.maxSpecialAmount) {
            if (specialTimer-- == 0) {
                specialTimer = PlayerRestoration.specialTimer
                player.specialAmount = (player.specialAmount + PlayerRestoration.specialRestoreMax)
                    .coerceAtMost(PlayerRestoration.maxSpecialAmount)
            }
        }
        if (player.walkingQueue.runEnergy < PlayerRestoration.maxRunEnergy) {
            if (runEnergyTimer-- == 0) {
                runEnergyTimer = PlayerRestoration.runEnergyTimer
                if (!player.walkingQueue.isRunningMoving) {
                    player.walkingQueue.runEnergy += PlayerRestoration.runEnergyRestoration
                    ActionSender.sendRunEnergy(player)
                }
            }
        }

        if (levelNormalizationTimer-- == 0) {
            levelNormalizationTimer = PlayerRestoration.levelNormalizationTimer
            val differentRestorations = intArrayOf(Skills.HITPOINTS, Skills.PRAYER, Skills.SUMMONING)
            for (i in 0 until Skills.SKILL_COUNT) {
                if (i in differentRestorations) {
                    continue
                }
                var currentLevel = player.skills.getLevel(i)
                val level = player.skills.getLevelForXp(i)
                currentLevel += PlayerRestoration.levelNormalizationAmount * direction(currentLevel < level)
                player.skills.setLevel(i, currentLevel)
            }
        }

        if (passiveHealTimer-- == 0) {
            passiveHealTimer = PlayerRestoration.passiveHealTimer
            if (player.skills.hitPoints < player.skills.getLevelForXp(Skills.HITPOINTS) * 10) {
                player.skills.heal(PlayerRestoration.healAmount)
            }
        }

    }
}