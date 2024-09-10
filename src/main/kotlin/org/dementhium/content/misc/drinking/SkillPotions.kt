package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.Boost
import org.dementhium.content.misc.ConsumableIds
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val AgilityPotion = Vial(
    listOf(ItemId.AGILITY_POTION_1, ItemId.AGILITY_POTION_2, ItemId.AGILITY_POTION_3, ItemId.AGILITY_POTION_4),
    Agility.Boost(flat = 3)
)

val FishingPotion = Vial(
    listOf(ItemId.FISHING_POTION_1, ItemId.FISHING_POTION_2, ItemId.FISHING_POTION_3, ItemId.FISHING_POTION_4),
    Fishing.Boost(flat = 3)
)

val HuntingPotion = Vial(
    listOf(ItemId.HUNTER_POTION_1, ItemId.HUNTER_POTION_2, ItemId.HUNTER_POTION_3, ItemId.HUNTER_POTION_4),
    Hunter.Boost(flat = 3)
)

val FletchingPotion = Vial(
    listOf(ItemId.FLETCHING_POTION_1, ItemId.FLETCHING_POTION_2, ItemId.FLETCHING_POTION_3, ItemId.FLETCHING_POTION_4),
    Fletching.Boost(flat = 3)
)

val CraftingPotion = Vial(
    listOf(ItemId.CRAFTING_POTION_1, ItemId.CRAFTING_POTION_2, ItemId.CRAFTING_POTION_3, ItemId.CRAFTING_POTION_4),
    Crafting.Boost(flat = 3)
)
