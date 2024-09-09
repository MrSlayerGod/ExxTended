package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.plus
import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.ItemId

data object ATTACK_POTION : Drink(
    Doses(ItemId.ATTACK_POTION_1, ItemId.ATTACK_POTION_2, ItemId.ATTACK_POTION_3, ItemId.ATTACK_POTION_4),
    Tier1Stat(SkillId.Attack)
)

data object DefencePotion : Drink(
    Doses(ItemId.DEFENCE_POTION_1, ItemId.DEFENCE_POTION_2, ItemId.DEFENCE_POTION_3, ItemId.DEFENCE_POTION_4),
    Tier1Stat(SkillId.Defence)
)

data object STRENGTH_POTION : Drink(
    Doses(ItemId.STRENGTH_POTION_1, ItemId.STRENGTH_POTION_2, ItemId.STRENGTH_POTION_3, ItemId.STRENGTH_POTION_4),
    Tier1Stat(SkillId.Strength)
)

data object MAGIC_POTION : Drink(
    Doses(ItemId.MAGIC_POTION_1, ItemId.MAGIC_POTION_2, ItemId.MAGIC_POTION_3, ItemId.MAGIC_POTION_4),
    SkillId.Magic.Boost(flat = 5)
)

data object RANGE_POTION : Drink(
    Doses(ItemId.RANGING_POTION_1, ItemId.RANGING_POTION_2, ItemId.RANGING_POTION_3, ItemId.RANGING_POTION_4),
    SkillId.Range.Boost(flat = 4, percent = 10)
)

data object CombatPotion : Drink(
    Doses(ItemId.COMBAT_POTION_1, ItemId.COMBAT_POTION_2, ItemId.COMBAT_POTION_3, ItemId.COMBAT_POTION_4),
    ATTACK_POTION.skillEffect + STRENGTH_POTION.skillEffect
)

data object PRAYER_POTION : Drink(
    Doses(ItemId.PRAYER_POTION_1, ItemId.PRAYER_POTION_2, ItemId.PRAYER_POTION_3, ItemId.PRAYER_POTION_4),
    SkillId.Prayer.Restore(flat = 7, percent = 25)
)

data object RESTORE_POTION : Drink(
    Doses(ItemId.RESTORE_POTION_1, ItemId.RESTORE_POTION_2, ItemId.RESTORE_POTION_3, ItemId.RESTORE_POTION_4),
    RestorePotion()
)

data object EnergyPotion : Drink(
    Doses(ItemId.ENERGY_POTION_1, ItemId.ENERGY_POTION_2, ItemId.ENERGY_POTION_3, ItemId.ENERGY_POTION_4),
    RestoreEnergy(percent = 20)
)

data object SummoningPotion : Drink(
    Doses(ItemId.SUMMONING_POTION_1, ItemId.SUMMONING_POTION_2, ItemId.SUMMONING_POTION_3, ItemId.SUMMONING_POTION_4),
    SkillId.Summoning.Restore(flat = 7, percent = 25)
)