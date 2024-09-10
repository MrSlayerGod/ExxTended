package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.builder.GenericBuilder

class SkillEffectBuilder: GenericBuilder<SkillEffect>("SkillEffectBuilder") {

    private val skillEffects: MutableList<SkillEffect> = mutableListOf()

    sealed interface HealType {
        data object Overheals: HealType
        data object DoesntOverheal: HealType
    }

    var hurt: Int = 0
    fun hurt(newHurt: Int) = apply { hurt = newHurt }

    var heal: Int = 0
    var healType: HealType = HealType.DoesntOverheal

    fun heal(newHeal: Int, newType: HealType = HealType.DoesntOverheal) = apply {
        heal = newHeal
        healType = newType
    }

    fun overheal(newHeal: Int) = heal(newHeal, HealType.Overheals)

    fun addEffect(skillEffect: SkillEffect) = apply { skillEffects.add(skillEffect) }
    fun SkillId.boost(flat: Int = 0, percent: Int = 0) = addEffect(boost(this, flat, percent))
    fun SkillId.drain(flat: Int = 0, percent: Int = 0) = addEffect(drain(this, flat, percent))
    fun SkillId.restore(flat: Int = 0, percent: Int = 0) = addEffect(restore(this, flat, percent))

    override fun build(): SkillEffect {
        if (heal != 0) {
            skillEffects += SkillEffect {
                heal(
                    heal,
                    maxHp = if (healType is HealType.Overheals) {
                        heal + maxHitpoints
                    } else {
                        heal
                    }
                )
            }
        }
        if (hurt != 0) {
            skillEffects += SkillEffect { hit(hurt) }
        }
        val toReturn = when {
            skillEffects.isEmpty() -> SkillEffect.Inert
            skillEffects.size == 1 -> skillEffects[0]
            else -> MultiSkillEffect(skillEffects.toList())
        }
        skillEffects.clear()
        return toReturn
    }
}
