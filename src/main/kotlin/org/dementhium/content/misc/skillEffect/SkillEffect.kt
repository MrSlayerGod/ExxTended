package org.dementhium.content.misc.skillEffect

import org.dementhium.model.player.skills.Skills

fun interface SkillEffect {

    fun Skills.applyEffect()

    fun flatten(): SkillEffect = this

    companion object {
        val Inert = SkillEffect { }
        fun from(byCollection: Collection<SkillEffect>) = when {
            byCollection.isEmpty() -> SkillEffect.Inert
            byCollection.size == 1 -> byCollection.first()
            else -> MultiSkillEffect(byCollection)
        }
    }
}
