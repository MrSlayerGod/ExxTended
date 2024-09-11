package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.DepletionType
import org.dementhium.content.misc.skillEffect.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId
import org.dementhium.util.random.SimpleWeightedTable
import org.dementhium.content.misc.buildConsumable
import org.dementhium.model.Item
import kotlin.random.Random

private val BraindeathDrains = listOf(Attack, Prayer, Range, Magic, Agility, Herblore)

val BottleOfWine = SingleDose(ItemId.BOTTLE_OF_WINE, emptyId = ItemId.EMPTY_WINE_BOTTLE) {
    heal(140)
    Attack.drain(flat=3)
}
val Brandy = SingleDose(ItemId.BRANDY, emptyId = ItemId.SILVER_BOTTLE_1801) {
    heal(50)
    Strength.boost(flat = 1, percent = 5)
}
val BraindeathRum = SingleDose(ItemId.BRAINDEATH_RUM, emptyId = ItemId.EMPTY_WINE_BOTTLE) {
    Strength.boost(flat = 3)
    Mining.boost(flat = 3)
    Defence.drain(percent = 10)
    BraindeathDrains.drain(percent = 5)
}
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
val KarmajaRum = SingleDose(ItemId.KARAMJAN_RUM, emptyId = ItemId.EMPTY_WINE_BOTTLE) {
    heal(50)
    Attack.drainBy { (currentLevel * 4) - 3 }
    Strength.drain(flat = 1, percent = 5)
}

val BeerKeg = buildConsumable<Drink> {
    consumableId(ItemId.KEG_OF_BEER)
    depletionType(DepletionType.Keep(Item(ItemId.KEG_OF_BEER)))
    addEffect {
        heal(150)
        Strength.boost(flat = 2, percent = 10)
        Attack.drain(flat = 5, percent = 50)
    }
}

val Vodka = SingleDose(ItemId.VODKA, emptyId = ItemId.EMPTY_WINE_BOTTLE) {
    heal(50)
    Strength.boost(flat = 1, percent = 5)
    Attack.drain(flat = 3, percent = 2)
}
val Whisky = SingleDose(
    ItemId.WHISKY,
    Vodka.skillEffect,
    emptyId = ItemId.EMPTY_WINE_BOTTLE
)
val CupOfTea = Tea(listOf(ItemId.CUP_OF_TEA_1978)) {
    heal(30)
    Attack.boost(flat = 3)
}
val NettleTea = SingleDose(ItemId.NETTLE_TEA, emptyId = ItemId.BOWL) {
    heal(30)
    addEffect(RestoreEnergy(percent = 10))
}
val ChocoMilk = SingleDose(ItemId.CHOCOLATEY_MILK, emptyId = ItemId.BUCKET) {
    heal(40)
}

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