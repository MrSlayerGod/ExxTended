package org.dementhium.content.misc.skillEffect.builder

import org.dementhium.content.misc.skillEffect.*
import org.dementhium.model.player.skills.SkillId

context(SkillEffectBuilder)
fun SkillId.drainBy(calculateDrain: BoostDrainContext.() -> Int) = addEffect(drainBy(this, calculateDrain))

context(SkillEffectBuilder)
fun SkillId.boostBy(calculateDrain: BoostDrainContext.() -> Int) = addEffect(boostBy(this, calculateDrain))

context(SkillEffectBuilder)
fun SkillId.restoreBy(calculateDrain: BoostDrainContext.() -> Int) = addEffect(restoreBy(this, calculateDrain))

context(SkillEffectBuilder)
fun SkillId.boost(flat: Int = 0, percent: Int = 0) = addEffect(boost(this, flat, percent))

context(SkillEffectBuilder)
fun SkillId.drain(flat: Int = 0, percent: Int = 0) = addEffect(drain(this, flat, percent))

context(SkillEffectBuilder)
fun SkillId.restore(flat: Int = 0, percent: Int = 0) = addEffect(restore(this, flat, percent))

context(SkillEffectBuilder)
fun Collection<SkillId>.boost(flat: Int = 0, percent: Int = 0) = apply {
    forEach { it.boost(flat, percent) }
}

context(SkillEffectBuilder)
fun Collection<SkillId>.drain(flat: Int = 0, percent: Int = 0) = apply {
    forEach { it.drain(flat, percent) }
}

context(SkillEffectBuilder)
fun Collection<SkillId>.restore(flat: Int = 0, percent: Int = 0) = apply {
    forEach { it.restore(flat, percent) }
}