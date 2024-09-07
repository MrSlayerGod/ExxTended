package org.dementhium.model.player.skills

enum class SkillId(val defaultLevel: Int = 1, val maxAllowedLevel: Int = 99) {
    Attack, Defence, Strength, Hitpoints(defaultLevel = 10), Range,
    Prayer, Magic, Cooking, Woodcutting, Fletching,
    Fishing, Firemaking, Crafting, Smithing, Mining,
    Herblore, Agility, Thieving, Slayer, Farming,
    Runecrafting, Construction, Hunter, Summoning, Dungeoneering(maxAllowedLevel = 120);

    val id by this::ordinal
}
