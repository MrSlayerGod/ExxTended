package org.dementhium.content.misc

import org.dementhium.model.player.skills.Skills
import org.dementhium.util.forEachScoped

fun interface SkillEffect {
    fun Skills.applyEffect()

    fun flatten(): SkillEffect = this

    companion object {
        val Inert = SkillEffect { }
    }
}

class MultiSkillEffect(
    val skillEffects: List<SkillEffect>
): SkillEffect {

    constructor(vararg effects: SkillEffect): this(effects.map(SkillEffect::flatten))

    override fun Skills.applyEffect() {
        skillEffects.forEachScoped { applyEffect() }
    }

    override fun flatten(): SkillEffect {
        val flattened = buildList {
            skillEffects.forEach { skillEffect -> add(skillEffect.flatten()) }
        }
        return MultiSkillEffect(flattened)
    }
}

operator fun SkillEffect.plus(other: SkillEffect): SkillEffect {
    if (other == SkillEffect.Inert || this == SkillEffect.Inert) return this
    return MultiSkillEffect(this, other)
}
