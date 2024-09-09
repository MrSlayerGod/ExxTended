package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.SkillEffect
import org.dementhium.content.misc.plus
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

data object FruitBlast: GnomeCocktail(
    ItemId.FRUIT_BLAST,
    HealBy(90)
)
data object PineapplePunch: GnomeCocktail(
    ItemId.PINEAPPLE_PUNCH,
    FruitBlast.skillEffect
)
data object WizardBlizzard: GnomeCocktail(
    ItemId.WIZARD_BLIZZARD,
    HealBy(50) + Strength.Boost(flat = 1, percent = 6) + Attack.Drain(flat = 3, percent = 2)
)
data object ShortGreenGuy: GnomeCocktail(
    ItemId.SHORT_GREEN_GUY,
    HealBy(50) + Strength.Boost(flat = 1, percent = 5) + Attack.Drain(flat = 3, percent = 2)
)
data object DrunkDragon: GnomeCocktail(
    ItemId.DRUNK_DRAGON,
    HealBy(50) + Strength.Boost(flat = 2, percent = 5) + Attack.Drain(flat = 3, percent = 2)
)
data object ChocSaturday: GnomeCocktail(
    ItemId.CHOC_SATURDAY,
    DrunkDragon.skillEffect
)
data object BlurberrySpecial: GnomeCocktail(
    ItemId.BLURBERRY_SPECIAL,
    DrunkDragon.skillEffect
)
