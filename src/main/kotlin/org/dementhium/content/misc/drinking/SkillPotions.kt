package org.dementhium.content.misc.drinking

import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.ItemId

data object AgilityPotion: Drink(
    Doses(ItemId.AGILITY_POTION_1, ItemId.AGILITY_POTION_2, ItemId.AGILITY_POTION_3, ItemId.AGILITY_POTION_4),
    SkillId.Agility.Boost(flat = 3)
)

data object FishingPotion: Drink(
    Doses(ItemId.FISHING_POTION_1, ItemId.FISHING_POTION_2, ItemId.FISHING_POTION_3, ItemId.FISHING_POTION_4),
    SkillId.Fishing.Boost(flat = 3)
)

data object HuntingPotion: Drink(
    Doses(ItemId.HUNTER_POTION_1, ItemId.HUNTER_POTION_2, ItemId.HUNTER_POTION_3, ItemId.HUNTER_POTION_4),
    SkillId.Hunter.Boost(flat = 3)
)

data object FletchingPotion: Drink(
    Doses(ItemId.FLETCHING_POTION_1, ItemId.FLETCHING_POTION_2, ItemId.FLETCHING_POTION_3, ItemId.FLETCHING_POTION_4),
    SkillId.Fletching.Boost(flat = 3)
)

data object CraftingPotion: Drink(
    Doses(ItemId.CRAFTING_POTION_1, ItemId.CRAFTING_POTION_2, ItemId.CRAFTING_POTION_3, ItemId.CRAFTING_POTION_4),
    SkillId.Crafting.Boost(flat = 3)
)
