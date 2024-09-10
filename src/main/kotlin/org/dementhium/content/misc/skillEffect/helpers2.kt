package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.math.percentOf

fun boostBy(skillId: SkillId, calculateBoost: BoostDrainContext.() -> Int) = SkillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateBoost()
    val cap = skillId.maximumLevel + boostAmount
    boostLevel(skillId, boostAmount, cap)
}

fun boost(skillId: SkillId, flat: Int = 0, percent: Int = 0) = boostBy(skillId) {
    flat + (percent percentOf maximumLevel)
}

fun drainBy(skillId: SkillId, calculateDrain: BoostDrainContext.() -> Int) = SkillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateDrain()
    drainLevel(skillId, boostAmount, 1)
}

fun drain(skillId: SkillId, flat: Int = 0, percent: Int = 0) = drainBy(skillId) {
    flat + (percent percentOf maximumLevel)
}

fun restoreBy(skillId: SkillId, calculateBoost: BoostDrainContext.() -> Int) = SkillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateBoost()
    boostLevel(skillId, boostAmount, cap = skillId.maximumLevel)
}

fun restore(skillId: SkillId, flat: Int = 0, percent: Int = 0) = restoreBy(skillId) {
    flat + (percent percentOf maximumLevel)
}
