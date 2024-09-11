package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.skillEffect.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId
import org.dementhium.util.math.percentOf
import org.dementhium.util.random.SimpleWeightedTable
import kotlin.random.Random

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

private val chaliceStats = Attack + Defence + Strength
private val PoisonChaliceTable = SimpleWeightedTable(
    1 to buildSkillEffect {
        sendMessage("Wow! That was amazing! You feel really invigorated.")
        heal(percent = 30)
        chaliceStats.boost(flat = 4)
    },
    1 to buildSkillEffect {
        sendMessage("That tasted very dodgy. You feel very ill.")
        hurt(percent = 50, floor = 40)
        chaliceStats.drain(flat = Random.nextInt(1, 4))
    },
    2 to buildSkillEffect {
        sendMessage("That tasted a bit dodgy. You feel a bit ill.")
        hurt(percent = 5, floor = 40)
    },
    4 to buildSkillEffect {
        sendMessage("The poison... heals some health...?")
        heal(percent = 5)
    },
    7 to buildSkillEffect {
        sendMessage("It has a slight taste of apricot.")
    },
    8 to buildSkillEffect {
        sendMessage("You feel a little strange.")
        chaliceStats.drain(flat = 1)
        Crafting.boost(flat = 1)
    },
    9 to buildSkillEffect {
        sendMessage("You feel a lot better.")
        heal(percent = 15)
        Thieving.boost(flat = 1)
    }
)

val PoisonChalice = GnomeCocktail(
    ItemId.POISON_CHALICE,
    skillEffect { with(PoisonChaliceTable.roll()) { applyEffect() } }
)