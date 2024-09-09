package org.dementhium.content.misc.drinking

import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.ItemId


data object SUPER_ATTACK_POTION : Drink(
    Doses(ItemId.SUPER_ATTACK_1, ItemId.SUPER_ATTACK_2, ItemId.SUPER_ATTACK_3, ItemId.SUPER_ATTACK_4),
    Tier2Stat(SkillId.Attack)
)

data object SUPER_DEFENCE_POTION : Drink(
    Doses(ItemId.SUPER_DEFENCE_1, ItemId.SUPER_DEFENCE_2, ItemId.SUPER_DEFENCE_3, ItemId.SUPER_DEFENCE_4),
    Tier2Stat(SkillId.Defence)
)

data object SUPER_STRENGTH_POTION : Drink(
    Doses(ItemId.SUPER_STRENGTH_1, ItemId.SUPER_STRENGTH_2, ItemId.SUPER_STRENGTH_3, ItemId.SUPER_STRENGTH_4),
    Tier2Stat(SkillId.Strength)
)

data object SUPER_RESTORE_POTION : Drink(
    Doses(ItemId.SUPER_RESTORE_1, ItemId.SUPER_RESTORE_2, ItemId.SUPER_RESTORE_3, ItemId.SUPER_RESTORE_4),
    SuperRestore()
)

data object SuperEnergyPotion : Drink(
    Doses(ItemId.SUPER_ENERGY_1, ItemId.SUPER_ENERGY_2, ItemId.SUPER_ENERGY_3, ItemId.SUPER_ENERGY_4),
    RestoreEnergy(percent = 40)
)

data object SuperPrayer : Drink(
    Doses(ItemId.SUPER_PRAYER_1, ItemId.SUPER_PRAYER_2, ItemId.SUPER_PRAYER_3, ItemId.SUPER_PRAYER_4),
    SkillId.Prayer.Restore(flat = 7, percent = 34)
)
