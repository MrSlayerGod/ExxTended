package org.dementhium.content.misc.eating

import org.dementhium.content.misc.skillEffect.CurePoison
import org.dementhium.content.misc.skillEffect.RestoreEnergy
import org.dementhium.content.misc.skillEffect.builder.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val Banana = Single(ItemId.BANANA) {
    heal(20)
}

val RedBanana = Single(ItemId.RED_BANANA) {
    heal(50)
}

val DwellBerries = Single(ItemId.DWELLBERRIES, Banana)
val BananaSlices = Single(ItemId.SLICED_BANANA, Banana)
val RedBananaSlices = Single(ItemId.SLICED_RED_BANANA, RedBanana)
val Orange = Single(ItemId.ORANGE, Banana)
val OrangeChunks = Single(ItemId.ORANGE_CHUNKS, Orange)
val OrangeSlices = Single(ItemId.ORANGE_SLICES, Orange)
val PineappleRing = Single(ItemId.PINEAPPLE_RING, Banana)
val PineappleChunks = Single(ItemId.PINEAPPLE_CHUNKS, Banana)

val Papaya = Single(ItemId.PAPAYA_FRUIT) {
    heal(80)
    addEffect(RestoreEnergy(percent = 10))
}

val Jangerberries = Single(ItemId.JANGERBERRIES) {
    heal(20)
    Attack.boost(flat = 2)
    Strength.boost(flat = 1)
    Prayer.restore(flat = 1)
    Defence.drain(flat = 1)
}

val Strawberry = Single(ItemId.STRAWBERRY) {
    heal(percent = 7)
}

val Tomato = Single(ItemId.TOMATO, Banana)

val WatermelonSlice  = Single(ItemId.WATERMELON_SLICE) {
    heal(10, percent = 5)
}

val Lemon = Single(ItemId.LEMON, Banana)
val LemonSlices = Single(ItemId.LEMON_SLICES, Lemon)
val LemonChunks = Single(ItemId.LEMON_CHUNKS, Lemon)
val Lime = Single(ItemId.LIME, Lemon)
val LimeSlices = Single(ItemId.LIME_SLICES, Lime)
val LimeChunks = Single(ItemId.LIME_CHUNKS, Lime)

val StrangeFruit = Single(ItemId.STRANGE_FRUIT) {
    addEffect(RestoreEnergy(percent = 60))
    addEffect(CurePoison())
}

val WhiteTreeFruit = Single(ItemId.WHITE_TREE_FRUIT) {
    heal(30)
    addEffect(RestoreEnergy(17))
}