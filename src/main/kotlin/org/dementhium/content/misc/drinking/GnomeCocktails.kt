package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val FruitBlast = GnomeCocktail(
    ItemId.FRUIT_BLAST,
    HealBy(90)
)
val PineapplePunch = GnomeCocktail(
    ItemId.PINEAPPLE_PUNCH,
    FruitBlast.skillEffect
)
val WizardBlizzard = GnomeCocktail(
    ItemId.WIZARD_BLIZZARD,
    HealBy(50) + Strength.Boost(flat = 1, percent = 6) + Attack.Drain(flat = 3, percent = 2)
)
val ShortGreenGuy = GnomeCocktail(
    ItemId.SHORT_GREEN_GUY,
    HealBy(50) + Strength.Boost(flat = 1, percent = 5) + Attack.Drain(flat = 3, percent = 2)
)
val DrunkDragon = GnomeCocktail(
    ItemId.DRUNK_DRAGON,
    HealBy(50) + Strength.Boost(flat = 2, percent = 5) + Attack.Drain(flat = 3, percent = 2)
)
val ChocSaturday = GnomeCocktail(
    ItemId.CHOC_SATURDAY,
    DrunkDragon.skillEffect
)
val BlurberrySpecial = GnomeCocktail(
    ItemId.BLURBERRY_SPECIAL,
    DrunkDragon.skillEffect
)
