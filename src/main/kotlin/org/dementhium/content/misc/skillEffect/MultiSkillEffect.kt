package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.Skills
import org.dementhium.util.forEachScoped

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