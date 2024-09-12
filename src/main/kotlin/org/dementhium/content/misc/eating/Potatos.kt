package org.dementhium.content.misc.eating

import org.dementhium.util.ItemId

val BakedPotato = Single(ItemId.BAKED_POTATO) {
    heal(40)
}

val ButteredPotato = Single(ItemId.POTATO_WITH_BUTTER) {
    heal(140)
}

val ChiliPotato = Single(ItemId.CHILLI_POTATO) {
    heal(140)
}

val CheesePotato = Single(ItemId.POTATO_WITH_CHEESE) {
    heal(160)
}

val EggPotato = Single(ItemId.EGG_POTATO) {
    heal(160)
}

val MushroomPotato = Single(ItemId.MUSHROOM_POTATO) {
    heal(200)
}

val TunaPotato = Single(ItemId.TUNA_POTATO) {
    heal(220)
}
