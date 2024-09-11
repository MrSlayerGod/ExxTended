package org.dementhium.content.misc.skillEffect.builder

import org.dementhium.content.misc.skillEffect.SkillEffect
import org.dementhium.content.misc.skillEffect.buildSkillEffect
import org.dementhium.content.misc.skillEffect.skillEffect
import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.builder.GenericBuilder
import org.dementhium.util.math.percentOf
import org.dementhium.content.misc.skillEffect.*

sealed interface HealType {
    data object Overheals: HealType
    data object DoesntOverheal: HealType
}

class SkillEffectBuilder: GenericBuilder<SkillEffect>("SkillEffectBuilder") {

    private val skillEffects: MutableList<SkillEffect> = mutableListOf()


    fun hurt(newHurt: Int = 0, percent: Int = 0, floor: Int = 0) = apply {
        val effect = skillEffect {
            val hurtValue = (percent percentOf maxHitpoints) + newHurt
            hit(hurtValue, floor)
        }
        addEffect(effect)
    }

    fun sendMessage(string: String) = addEffect { player.sendMessage(string) }

    fun heal(newHeal: Int = 0, percent: Int = 0, healType: HealType = HealType.DoesntOverheal) = addEffect {
        val healValue = (percent percentOf maxHitpoints) + newHeal
        val maxOverheal = maxHitpoints + if (healType is HealType.DoesntOverheal) {
            0
        } else {
            healValue
        }
        heal(healValue, maxOverheal)
    }

    fun addEffect(skillEffect: SkillEffect) = apply { skillEffects.add(skillEffect) }

    fun buildEffect(builder: SkillEffectBuilder.() -> Unit) = addEffect(buildSkillEffect(builder))

    override fun build(): SkillEffect {
        val toReturn = SkillEffect.from(skillEffects)
        skillEffects.clear()
        return toReturn
    }
}
