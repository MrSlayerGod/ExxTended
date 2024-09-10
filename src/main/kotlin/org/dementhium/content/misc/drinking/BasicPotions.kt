package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.content.misc.skillEffect.plus
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val AttackPotion = Vial(
    listOf(ItemId.ATTACK_POTION_1, ItemId.ATTACK_POTION_2, ItemId.ATTACK_POTION_3, ItemId.ATTACK_POTION_4),
    Tier1Stat(Attack)
)

val DefencePotion = Vial(
    listOf(ItemId.DEFENCE_POTION_1, ItemId.DEFENCE_POTION_2, ItemId.DEFENCE_POTION_3, ItemId.DEFENCE_POTION_4),
    Tier1Stat(Defence)
)

val StrengthPotion = Vial(
    listOf(ItemId.STRENGTH_POTION_1, ItemId.STRENGTH_POTION_2, ItemId.STRENGTH_POTION_3, ItemId.STRENGTH_POTION_4),
    Tier1Stat(Strength)
)

val MagicPotion = Vial(
    listOf(ItemId.MAGIC_POTION_1, ItemId.MAGIC_POTION_2, ItemId.MAGIC_POTION_3, ItemId.MAGIC_POTION_4),
    Magic.Boost(flat = 5)
)

val RangePotion = Vial(
    listOf(ItemId.RANGING_POTION_1, ItemId.RANGING_POTION_2, ItemId.RANGING_POTION_3, ItemId.RANGING_POTION_4),
    Range.Boost(flat = 4, percent = 10)
)

val CombatPotion = Vial(
    listOf(ItemId.COMBAT_POTION_1, ItemId.COMBAT_POTION_2, ItemId.COMBAT_POTION_3, ItemId.COMBAT_POTION_4),
    AttackPotion.skillEffect + StrengthPotion.skillEffect
)

val PrayerPotion = Vial(
    listOf(ItemId.PRAYER_POTION_1, ItemId.PRAYER_POTION_2, ItemId.PRAYER_POTION_3, ItemId.PRAYER_POTION_4),
    Prayer.Restore(flat = 7, percent = 25)
)

val RestorePotion = Vial(
    listOf(ItemId.RESTORE_POTION_1, ItemId.RESTORE_POTION_2, ItemId.RESTORE_POTION_3, ItemId.RESTORE_POTION_4),
    RestorePotion()
)

val EnergyPotion = Vial(
    listOf(ItemId.ENERGY_POTION_1, ItemId.ENERGY_POTION_2, ItemId.ENERGY_POTION_3, ItemId.ENERGY_POTION_4),
    RestoreEnergy(percent = 20)
)

val SummoningPotion = Vial(
    listOf(ItemId.SUMMONING_POTION_1, ItemId.SUMMONING_POTION_2, ItemId.SUMMONING_POTION_3, ItemId.SUMMONING_POTION_4),
    Summoning.Restore(flat = 7, percent = 25)
)