package org.dementhium.content.misc.eating

import org.dementhium.content.misc.skillEffect.builder.boost
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val RawPotato = Single(ItemId.POTATO) {
    heal(10)
}

val Onion = Single(ItemId.ONION, RawPotato)

val NormalCabbage = Single(ItemId.CABBAGE, RawPotato)

val DraynorCabbage = Single(ItemId.CABBAGE_1967) {
    heal(20)
    Defence.boost(flat = 1, percent = 2)
}

val CookedSweetcorn = Single(ItemId.COOKED_SWEETCORN) {
    heal(percent = 10)
}

val EvilTurnip = Food(
    listOf(ItemId.`1_3_EVIL_TURNIP`, ItemId.`2_3_EVIL_TURNIP`, ItemId.EVIL_TURNIP),
) {
    heal(60)
}

