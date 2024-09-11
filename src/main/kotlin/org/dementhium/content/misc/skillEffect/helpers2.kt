package org.dementhium.content.misc.skillEffect

import org.dementhium.content.misc.skillEffect.builder.BoostDrainContext
import org.dementhium.content.misc.skillEffect.builder.SkillEffectBuilder
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.Skills
import org.dementhium.util.builder.builder
import org.dementhium.util.math.percentOf

fun skillEffect(block: Skills.() -> Unit) = SkillEffect(block)

fun boostBy(skillId: SkillId, calculateBoost: BoostDrainContext.() -> Int) = skillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateBoost()
    val cap = skillId.maximumLevel + boostAmount
    boostLevel(skillId, boostAmount, cap)
}

fun boost(skillId: SkillId, flat: Int = 0, percent: Int = 0) = boostBy(skillId) {
    flat + (percent percentOf maximumLevel)
}

fun drainBy(skillId: SkillId, calculateDrain: BoostDrainContext.() -> Int) = skillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateDrain()
    drainLevel(skillId, boostAmount, 1)
}

fun drain(skillId: SkillId, flat: Int = 0, percent: Int = 0) = drainBy(skillId) {
    flat + (percent percentOf maximumLevel)
}

fun restoreBy(skillId: SkillId, calculateBoost: BoostDrainContext.() -> Int) = skillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateBoost()
    boostLevel(skillId, boostAmount, cap = skillId.maximumLevel)
}

fun restore(skillId: SkillId, flat: Int = 0, percent: Int = 0) = restoreBy(skillId) {
    flat + (percent percentOf maximumLevel)
}

fun buildSkillEffect(builder: SkillEffectBuilder.() -> Unit) = builder(builder)
