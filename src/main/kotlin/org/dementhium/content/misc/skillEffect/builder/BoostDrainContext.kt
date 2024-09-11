package org.dementhium.content.misc.skillEffect.builder

import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.Skills


class BoostDrainContext(private val skills: Skills, private val skillId: SkillId) {
    val currentLevel get() = with(skills) { skillId.currentLevel }
    val maximumLevel get() = with(skills) { skillId.maximumLevel }
}
