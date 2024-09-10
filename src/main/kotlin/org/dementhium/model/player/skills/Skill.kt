package org.dementhium.model.player.skills

open class Skill(
    val skills: Skills,
    val skillId: SkillId,
    var currentLevel: Int,
    var experience: Double
) {

    constructor(skills: Skills, skillId: SkillId): this(
        skills = skills,
        skillId = skillId,
        currentLevel = skillId.defaultLevel,
        experience = skills.expFormula.calcExpForLevel(skillId.defaultLevel)
    )

    val maxLevel: Int get() = skills.expFormula.calcLevelForExp(experience, skillId)

    fun reset() {
        currentLevel = skillId.defaultLevel
        experience = skills.expFormula.calcExpForLevel(skillId.defaultLevel)
    }
}
