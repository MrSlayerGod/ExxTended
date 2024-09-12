package org.dementhium.content.misc.eating

import org.dementhium.content.misc.skillEffect.RestoreEnergy
import org.dementhium.content.misc.skillEffect.builder.boost
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val RedberryPie = Pie(ItemId.HALF_A_REDBERRY_PIE, ItemId.REDBERRY_PIE) {
    heal(50)
}

val MeatPie = Pie(ItemId.HALF_A_MEAT_PIE, ItemId.MEAT_PIE) {
    heal(60)
}

val ApplePie = Pie(ItemId.HALF_AN_APPLE_PIE, ItemId.APPLE_PIE) {
    heal(70)
}

val GardenPie = Pie(ItemId.HALF_A_GARDEN_PIE, ItemId.GARDEN_PIE) {
    heal(60)
    Farming.boost(flat = 3)
}

val FishingPie = Pie(ItemId.HALF_A_FISH_PIE, ItemId.FISH_PIE) {
    heal(60)
    Fishing.boost(flat = 3)
}

val AdmiralPie = Pie(ItemId.HALF_AN_ADMIRAL_PIE, ItemId.ADMIRAL_PIE) {
    heal(80)
    Fishing.boost(flat = 5)
}

val WildPie = Pie(ItemId.HALF_A_WILD_PIE, ItemId.WILD_PIE) {
    heal(110)
    Slayer.boost(flat = 5)
    Range.boost(flat = 4)
}

val SummerPie = Pie(ItemId.HALF_A_SUMMER_PIE, ItemId.SUMMER_PIE) {
    heal(110)
    Agility.boost(flat = 3)
    addEffect(RestoreEnergy(percent = 10))
}
