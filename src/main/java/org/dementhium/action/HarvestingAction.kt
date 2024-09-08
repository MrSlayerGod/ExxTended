package org.dementhium.action

import org.dementhium.model.Item
import org.dementhium.model.mask.Animation
import org.dementhium.model.player.Player
import org.dementhium.model.player.skills.SkillId

abstract class HarvestingAction : Action(0) {

    interface HarvestTool {
        val animation: Animation get() = Animation.RESET
        val requiredLevel: Int
        val requiredSkill: Int
        val toolId: Int

        fun playerMeetsRequirements(player: Player): Boolean {
            val playerHasTool = player.inventory.contains(toolId) || player.equipment.contains(toolId)
            val playerHasLevel = player.skills.getLevel(requiredSkill) >= requiredLevel
            return playerHasTool && playerHasLevel
        }
    }

    interface HarvestObject {
        val requiredLevel: Int
        val experience: Double
    }

    var cyclesToHarvest: Int = 0

    var animationCycles = 0

    var started = false

    override fun execute() {
        val player = mob.player
        val skill = player.skills.getSkill(skillId)
        if (++animationCycles >= 3) {
            player.animate(tool.animation)
        }
        if (cyclesToHarvest-- > 0) {
            return
        }
        if (harvestObject.requiredLevel > skill.currentLevel) {
            player.sendMessage(getMessage(OBJECT_LEVEL))
            stop()
            return
        }
        val playerHasTool = tool.playerMeetsRequirements(player)
        if (playerHasTool) {
            if (tool.requiredLevel > skill.currentLevel) {
                player.sendMessage(getMessage(TOOL_LEVEL))
                stop()
                return
            }
            cyclesToHarvest = cycleTime
            if (!started) {
                started = true
                player.sendMessage(startMessage)
                player.animate(tool.animation)
                return
            }
            if (!player.inventory.addItem(harvestReward.id, harvestReward.amount)) {
                stop()
                return
            }
            skill.experience += harvestObject.experience
            player.sendMessage(getMessage(HARVESTED_ITEM))
        } else {
            player.sendMessage(getMessage(NO_TOOL))
            stop()
            return
        }
    }

    abstract val harvestReward: Item

    abstract val startMessage: String

    abstract fun getMessage(type: Int): String

    abstract val skill: Int

    val skillId: SkillId
        get() = SkillId.byId(skill)

    abstract val cycleTime: Int

    abstract val tool: HarvestTool

    abstract val harvestObject: HarvestObject

    companion object {
        const val TOOL_LEVEL: Int = 0
        const val NO_TOOL: Int = 1
        const val OBJECT_LEVEL: Int = 2
        const val HARVESTED_ITEM: Int = 3
    }
}