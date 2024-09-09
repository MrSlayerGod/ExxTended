package org.dementhium.content.misc.drinking

import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.ItemId

private val RestoreMix = SkillId.entries.filter {
    it != SkillId.Hitpoints && it != SkillId.Agility && it != SkillId.Summoning && it != SkillId.Prayer
}
private val RoeHeal = 30
private val CaviarHeal = 60

data object AttackMix: BarbarianMix(
    Doses(ItemId.ATTACK_MIX_1, ItemId.ATTACK_MIX_2),
    ATTACK_POTION.skillEffect, RoeHeal
)
data object AntipoisonMix: BarbarianMix(
    Doses(ItemId.ANTIPOISON_MIX_1, ItemId.ANTIPOISON_MIX_2),
    ANTIPOISON.skillEffect, RoeHeal
)
data object RelicymMix: BarbarianMix(
    Doses(ItemId.RELICYMS_MIX_1, ItemId.RELICYMS_MIX_2),
    RelicymBalm.skillEffect, RoeHeal
)
data object StrengthMix: BarbarianMix(
    Doses(ItemId.STRENGTH_MIX_1, ItemId.STRENGTH_MIX_2),
    STRENGTH_POTION.skillEffect, RoeHeal
)
data object StatRestoreMix: BarbarianMix(
    Doses(ItemId.RESTORE_MIX_1, ItemId.RESTORE_MIX_2),
    RestoreMix.restoreAll(flat = 10, percent = 30), RoeHeal
)
data object EnergyMix: BarbarianMix(
    Doses(ItemId.ENERGY_MIX_1, ItemId.ENERGY_MIX_2),
    RestoreEnergy(percent = 10), CaviarHeal
)
data object DefenceMix: BarbarianMix(
    Doses(ItemId.DEFENCE_MIX_1, ItemId.DEFENCE_MIX_2),
    DefencePotion.skillEffect, CaviarHeal
)
data object AgilityMix: BarbarianMix(
    Doses(ItemId.AGILITY_MIX_1, ItemId.AGILITY_MIX_2),
    AgilityPotion.skillEffect, CaviarHeal
)
data object CombatMix: BarbarianMix(
    Doses(ItemId.COMBAT_MIX_1, ItemId.COMBAT_MIX_2),
    CombatPotion.skillEffect, CaviarHeal
)
data object PrayerMix: BarbarianMix(
    Doses(ItemId.PRAYER_MIX_1, ItemId.PRAYER_MIX_2),
    PRAYER_POTION.skillEffect, CaviarHeal
)
data object SuperAttackMix: BarbarianMix(
    Doses(ItemId.SUPERATTACK_MIX_1, ItemId.SUPERATTACK_MIX_2),
    SUPER_ATTACK_POTION.skillEffect, CaviarHeal
)
data object SuperAntiPoisonMix: BarbarianMix(
    Doses(ItemId.ANTI_P_SUPERMIX_1, ItemId.ANTI_P_SUPERMIX_2),
    SuperAntipoison.skillEffect, CaviarHeal
)
data object FishingMix: BarbarianMix(
    Doses(ItemId.FISHING_MIX_1, ItemId.FISHING_MIX_2),
    FishingPotion.skillEffect, CaviarHeal
)
data object SuperEnergyMix: BarbarianMix(
    Doses(ItemId.SUP_ENERGY_MIX_1, ItemId.SUP_ENERGY_MIX_2),
    EnergyPotion.skillEffect, CaviarHeal
)
data object HuntingMix: BarbarianMix(
    Doses(ItemId.HUNTING_MIX_1, ItemId.HUNTING_MIX_2),
    HuntingPotion.skillEffect, CaviarHeal
)
data object SuperStrengthMix: BarbarianMix(
    Doses(ItemId.SUP_STR_MIX_1, ItemId.SUP_STR_MIX_2),
    SUPER_STRENGTH_POTION.skillEffect, CaviarHeal
)
data object MagicEssenceMix: BarbarianMix(
    Doses(ItemId.MAGIC_ESS_MIX_1, ItemId.MAGIC_ESS_MIX_2),
    MagicEssence.skillEffect, CaviarHeal
)
data object SuperRestoreMix: BarbarianMix(
    Doses(ItemId.SUP_RESTORE_MIX_1, ItemId.SUP_RESTORE_MIX_2),
    SUPER_RESTORE_POTION.skillEffect, CaviarHeal
)
data object SuperDefenceMix: BarbarianMix(
    Doses(ItemId.SUP_DEF_MIX_1, ItemId.SUP_DEF_MIX_2),
    SUPER_DEFENCE_POTION.skillEffect, CaviarHeal
)
data object ExtraStrongAntiPoisonMix: BarbarianMix(
    Doses(ItemId.ANTIDOTEP_MIX_1, ItemId.ANTIDOTEP_MIX_2),
    SuperAntipoison.skillEffect, CaviarHeal
)
data object AntifireMix: BarbarianMix(
    Doses(ItemId.ANTIFIRE_MIX_1, ItemId.ANTIFIRE_MIX_2),
    AntifirePotion.skillEffect, CaviarHeal
)
data object RangingMix: BarbarianMix(
    Doses(ItemId.RANGING_MIX_1, ItemId.RANGING_MIX_2),
    RANGE_POTION.skillEffect, CaviarHeal
)
data object MagicMix: BarbarianMix(
    Doses(ItemId.MAGIC_MIX_1, ItemId.MAGIC_MIX_2),
    MAGIC_POTION.skillEffect, CaviarHeal
)
data object ZamorakMix: BarbarianMix(
    Doses(ItemId.ZAMORAK_MIX_1, ItemId.ZAMORAK_MIX_2),
    ZAMORAK_BREW.skillEffect, CaviarHeal
)