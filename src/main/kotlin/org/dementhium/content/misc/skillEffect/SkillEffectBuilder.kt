package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.Skills
import org.dementhium.util.Modify
import org.dementhium.util.builder.GenericBuilder
import org.dementhium.util.math.percentOf

class BoostDrainContext(private val skills: Skills, private val skillId: SkillId) {
    val currentLevel get() = with(skills) { skillId.currentLevel }
    val maximumLevel get() = with(skills) { skillId.maximumLevel }
}

fun boost(skillId: SkillId, calculateBoost: BoostDrainContext.() -> Int) = SkillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateBoost()
    val cap = skillId.maximumLevel + boostAmount
    boostLevel(skillId, boostAmount, cap)
}

fun boost(valueToBoost: Int, flat: Int = 0, percent: Int = 0): Int = (percent percentOf valueToBoost) + flat

sealed class ModifyInt(val flat: Int, val percent: Int, inline val operation: (Int, Int) -> Int): Modify<Int> {
    final override fun modifyValue(value: Int): Int = operation(percent percentOf value, flat)
}

private class Boost(flat: Int, percent: Int): ModifyInt(flat, percent, Int::plus)
private class Drain(flat: Int, percent: Int): ModifyInt(flat, percent, Int::minus)
private class Restore(flat: Int, percent: Int): ModifyInt(flat, percent, Int::plus)

class SkillEffectBuilder: GenericBuilder<SkillEffect>("SkillEffectBuilder") {

    private class Intermediate(val modify: Modify<Int>) {
        operator fun invoke(flat: Int, percent: Int) =
    }

    private val skillModifications: MutableMap<SkillId, Modify<Int>> = mutableMapOf()

    sealed interface HealType {
        data object Overheals: HealType
        data object DoesntOverheal: HealType
    }

    var heal: Int = 0
    var healType: HealType = HealType.DoesntOverheal

    fun heal(newHeal: Int, newType: HealType = HealType.DoesntOverheal) = apply {
        heal = newHeal
        healType = newType
    }

    fun overheal(newHeal: Int) = heal(newHeal, HealType.Overheals)

    fun SkillId.boost(flat: Int = 0, percent: Int = 0) = apply {
        skillModifications[this] = Boost(flat, percent)
    }
    fun SkillId.drain(flat: Int = 0, percent: Int = 0) = apply {
        skillModifications[this] = Drain(flat, percent)
    }
    fun SkillId.restore(flat: Int = 0, percent: Int = 0) = apply {
        skillModifications[this] = Restore(flat, percent)
    }

    operator fun SkillId.unaryPlus

    override fun build(): SkillEffect {
        if ()
    }
}
