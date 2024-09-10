package org.dementhium.content.misc.skillEffect

operator fun SkillEffect.plus(other: SkillEffect): SkillEffect {
    if (other == SkillEffect.Inert || this == SkillEffect.Inert) return this
    return MultiSkillEffect(this, other)
}
