package org.dementhium.event.impl

import org.dementhium.event.Tickable
import org.dementhium.model.player.Player
import org.dementhium.model.player.skills.Skills
import org.dementhium.model.player.skills.SkillId
import org.dementhium.net.ActionSender

data class PlayerRestorationConfig(
    val specialTimer: Int = 50,
    val runEnergyTimer: Int =  2,
    val levelNormalizationTimer: Int = 90,
    val passiveHealTimer: Int = 10,
    val maxSpecialAmount: Int = 1_000,
    val maxRunEnergy: Int = 100,
    val specialRestoreMax: Int = 100,
    val runEnergyRestoration: Int = 1,
    val levelNormalizationAmount: Int = 1,
    val healAmount: Int = 1,
) {
    companion object {
        val Default = PlayerRestorationConfig()
    }
}

class PlayerRestorationTick(
    private val player: Player,
    private val restorationConfig: PlayerRestorationConfig
) : Tickable(1) {

    constructor(player: Player): this(player, PlayerRestorationConfig.Default)

    private var specialTimer = restorationConfig.specialTimer
    private var runEnergyTimer = restorationConfig.runEnergyTimer
    private var levelNormalizationTimer = restorationConfig.levelNormalizationTimer
    private var passiveHealTimer = restorationConfig.passiveHealTimer

    override fun execute() {
        val isOnline = player.isOnline
        if (!isOnline) return stop()
        if (player.specialAmount < restorationConfig.maxSpecialAmount) {
            if (specialTimer-- == 0) {
                specialTimer = restorationConfig.specialTimer
                player.specialAmount = (player.specialAmount + restorationConfig.specialRestoreMax)
                    .coerceAtMost(restorationConfig.maxSpecialAmount)
            }
        }
        if (player.walkingQueue.runEnergy < restorationConfig.maxRunEnergy) {
            if (runEnergyTimer-- == 0) {
                runEnergyTimer = restorationConfig.runEnergyTimer
                if (!player.walkingQueue.isRunningMoving) {
                    player.walkingQueue.runEnergy += restorationConfig.runEnergyRestoration
                    ActionSender.sendRunEnergy(player)
                }
            }
        }

        if (levelNormalizationTimer-- == 0) {
            levelNormalizationTimer = restorationConfig.levelNormalizationTimer
            for (i in 0 until Skills.SKILL_COUNT) {
                val skillId = SkillId.entries[i]
                val skill = player.skills.getSkill(skillId)
                if (skill.currentLevel == skill.maxLevel) continue
                player.skills.normalizeLevel(skillId, restorationConfig.levelNormalizationAmount) {
                    skill.currentLevel < skill.maxLevel
                }
            }
        }

        if (passiveHealTimer-- == 0) {
            passiveHealTimer = restorationConfig.passiveHealTimer
            if (player.skills.hitPoints < player.skills.maxHitpoints) {
                player.skills.heal(restorationConfig.healAmount)
            }
        }

    }
}