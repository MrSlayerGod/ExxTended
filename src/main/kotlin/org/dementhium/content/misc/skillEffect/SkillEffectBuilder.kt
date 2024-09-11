package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.builder.GenericBuilder
import org.dementhium.util.math.percentOf

sealed interface HealType {
    data object Overheals: HealType
    data object DoesntOverheal: HealType
}

class SkillEffectBuilder: GenericBuilder<SkillEffect>("SkillEffectBuilder") {

    private val skillEffects: MutableList<SkillEffect> = mutableListOf()


    fun hurt(newHurt: Int, percent: Int = 0) = apply {
        val effect = skillEffect {
            val hurtValue = (percent percentOf maxHitpoints) + newHurt
            hit(hurtValue)
        }
        addEffect(effect)
    }

    fun heal(newHeal: Int, percent: Int = 0, healType: HealType = HealType.DoesntOverheal) = apply {
        val effect = skillEffect {
            val healValue = (percent percentOf maxHitpoints) + newHeal
            heal(
                healValue,
                maxHp = maxHitpoints + if (healType is HealType.DoesntOverheal) {
                    0
                } else {
                    healValue
                }
            )
        }
        addEffect(effect)
    }

    fun overheal(newHeal: Int, percent: Int = 0) = heal(newHeal, percent, HealType.Overheals)

    fun addEffect(skillEffect: SkillEffect) = apply { skillEffects.add(skillEffect) }

    fun addEffect(builder: SkillEffectBuilder.() -> Unit) = addEffect(buildSkillEffect(builder))

    fun SkillId.boost(flat: Int = 0, percent: Int = 0) = addEffect(boost(this, flat, percent))
    fun SkillId.drain(flat: Int = 0, percent: Int = 0) = addEffect(drain(this, flat, percent))
    fun SkillId.restore(flat: Int = 0, percent: Int = 0) = addEffect(restore(this, flat, percent))

    override fun build(): SkillEffect {
        val toReturn = SkillEffect.from(skillEffects)
        skillEffects.clear()
        return toReturn
    }
}
