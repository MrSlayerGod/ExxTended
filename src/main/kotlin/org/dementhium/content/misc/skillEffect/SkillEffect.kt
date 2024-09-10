package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.Skills
import org.dementhium.util.forEachScoped

fun interface SkillEffect {
    fun Skills.applyEffect()

    fun flatten(): SkillEffect = this

    companion object {
        val Inert = SkillEffect { }
    }
}
