package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

private val BraindeathDrains = listOf(Attack, Prayer, Range, Magic, Agility, Herblore)

val BottleOfWine = SingleDose(
    ItemId.BOTTLE_OF_WINE,
    HealBy(140) + Attack.Drain(flat=3),
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val Brandy = SingleDose(
    ItemId.BRANDY,
    HealBy(50) + Strength.Boost(flat = 1, percent = 5),
    emptyId = ItemId.SILVER_BOTTLE_1801
)
val BraindeathRum = SingleDose(
    ItemId.BRAINDEATH_RUM,
    Strength.Boost(flat =  3) + Mining.Boost(flat = 3) + Defence.Drain(percent = 10) + BraindeathDrains.drainAll(percent = 5),
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val Gin = SingleDose(
    ItemId.GIN,
    Brandy.skillEffect + Attack.Drain(flat = 3, percent = 2),
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val HalfFullWine = SingleDose(
    ItemId.HALF_FULL_WINE_JUG,
    HealBy(70) + Attack.Drain(flat = 2),
    emptyId = ItemId.EMPTY_JUG
)
val JugOfWine = SingleDose(
    ItemId.JUG_OF_WINE,
    HealBy(120) + Attack.Drain(2),
    emptyId = ItemId.EMPTY_JUG
)
val KarmajaRum = SingleDose(
    ItemId.KARAMJAN_RUM,
    HealBy(50) + Attack.DrainBy { (currentLevel * 4) - 3 } + Strength.Drain(flat = 1, percent = 5),
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val BeerKeg = SingleDose(
    ItemId.KEG_OF_BEER_3801,
    HealBy(150) + Strength.Boost(flat = 2, percent = 10) + Attack.Drain(flat = 5, percent = 50),
    emptyId = ItemId.KEG_OF_BEER_3801
)
val Vodka = SingleDose(
    ItemId.VODKA,
    HealBy(50) + Strength.Boost(flat = 1, percent = 5) + Attack.Drain(flat =  3, percent = 2),
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val Whisky = SingleDose(
    ItemId.WHISKY,
    Vodka.skillEffect,
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val CupOfTea = Tea(
    listOf(ItemId.CUP_OF_TEA_1978),
    HealBy(30) + Attack.Boost(flat = 3)
)