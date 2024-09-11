package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.skillEffect.RestoreEnergy
import org.dementhium.content.misc.skillEffect.restoreAll
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

private val RestoreMix = SkillId.entries.filter {
    it != Hitpoints && it != Agility && it != Summoning && it != Prayer
}
private val RoeHeal = 30
private val CaviarHeal = 60

val AttackMix = BarbarianMix(
    ItemId.ATTACK_MIX_1,
    AttackPotion, RoeHeal
)
val AntipoisonMix = BarbarianMix(
    ItemId.ANTIPOISON_MIX_1,
    AntiPoison, RoeHeal
)
val RelicymMix = BarbarianMix(
    ItemId.RELICYMS_MIX_1,
    RelicymBalm, RoeHeal
)
val StrengthMix = BarbarianMix(
    ItemId.STRENGTH_MIX_1,
    StrengthPotion, RoeHeal
)
val StatRestoreMix = BarbarianMix(
    ItemId.RESTORE_MIX_1,
    RestoreMix.restoreAll(flat = 10, percent = 30), RoeHeal
)
val EnergyMix = BarbarianMix(
    ItemId.ENERGY_MIX_1,
    RestoreEnergy(percent = 10), CaviarHeal
)
val DefenceMix = BarbarianMix(
    ItemId.DEFENCE_MIX_1,
    DefencePotion, CaviarHeal
)
val AgilityMix = BarbarianMix(
    ItemId.AGILITY_MIX_1,
    AgilityPotion, CaviarHeal
)
val CombatMix = BarbarianMix(
    ItemId.COMBAT_MIX_1,
    CombatPotion, CaviarHeal
)
val PrayerMix = BarbarianMix(
    ItemId.PRAYER_MIX_1,
    PrayerPotion, CaviarHeal
)
val SuperAttackMix = BarbarianMix(
    ItemId.SUPERATTACK_MIX_1,
    SuperAttackPotion, CaviarHeal
)
val SuperAntiPoisonMix = BarbarianMix(
    ItemId.ANTI_P_SUPERMIX_1,
    SuperAntipoison, CaviarHeal
)
val FishingMix = BarbarianMix(
    ItemId.FISHING_MIX_1,
    FishingPotion, CaviarHeal
)
val SuperEnergyMix = BarbarianMix(
    ItemId.SUP_ENERGY_MIX_1,
    EnergyPotion, CaviarHeal
)
val HuntingMix = BarbarianMix(
    ItemId.HUNTING_MIX_1,
    HuntingPotion, CaviarHeal
)
val SuperStrengthMix = BarbarianMix(
    ItemId.SUP_STR_MIX_1,
    SuperStrengthPotion, CaviarHeal
)
val MagicEssenceMix = BarbarianMix(
    ItemId.MAGIC_ESS_MIX_1,
    MagicEssence, CaviarHeal
)
val SuperRestoreMix = BarbarianMix(
    ItemId.SUP_RESTORE_MIX_1,
    SuperRestorePotion, CaviarHeal
)
val SuperDefenceMix = BarbarianMix(
    ItemId.SUP_DEF_MIX_1,
    SuperDefencePotion, CaviarHeal
)
val ExtraStrongAntiPoisonMix = BarbarianMix(
    ItemId.ANTIDOTEP_MIX_1,
    SuperAntipoison, CaviarHeal
)
val AntifireMix = BarbarianMix(
    ItemId.ANTIFIRE_MIX_1,
    AntifirePotion, CaviarHeal
)
val RangingMix = BarbarianMix(
    ItemId.RANGING_MIX_1,
    RangePotion, CaviarHeal
)
val MagicMix = BarbarianMix(
    ItemId.MAGIC_MIX_1,
    MagicPotion, CaviarHeal
)
val ZamorakMix = BarbarianMix(
    ItemId.ZAMORAK_MIX_1,
    ZamorakBrew, CaviarHeal
)