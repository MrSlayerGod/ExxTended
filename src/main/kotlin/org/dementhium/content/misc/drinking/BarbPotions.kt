package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.RestoreEnergy
import org.dementhium.content.misc.restoreAll
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
    AttackPotion.skillEffect, RoeHeal
)
val AntipoisonMix = BarbarianMix(
    ItemId.ANTIPOISON_MIX_1,
    AntiPoison.skillEffect, RoeHeal
)
val RelicymMix = BarbarianMix(
    ItemId.RELICYMS_MIX_1,
    RelicymBalm.skillEffect, RoeHeal
)
val StrengthMix = BarbarianMix(
    ItemId.STRENGTH_MIX_1,
    StrengthPotion.skillEffect, RoeHeal
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
    DefencePotion.skillEffect, CaviarHeal
)
val AgilityMix = BarbarianMix(
    ItemId.AGILITY_MIX_1,
    AgilityPotion.skillEffect, CaviarHeal
)
val CombatMix = BarbarianMix(
    ItemId.COMBAT_MIX_1,
    CombatPotion.skillEffect, CaviarHeal
)
val PrayerMix = BarbarianMix(
    ItemId.PRAYER_MIX_1,
    PrayerPotion.skillEffect, CaviarHeal
)
val SuperAttackMix = BarbarianMix(
    ItemId.SUPERATTACK_MIX_1,
    SuperAttackPotion.skillEffect, CaviarHeal
)
val SuperAntiPoisonMix = BarbarianMix(
    ItemId.ANTI_P_SUPERMIX_1,
    SuperAntipoison.skillEffect, CaviarHeal
)
val FishingMix = BarbarianMix(
    ItemId.FISHING_MIX_1,
    FishingPotion.skillEffect, CaviarHeal
)
val SuperEnergyMix = BarbarianMix(
    ItemId.SUP_ENERGY_MIX_1,
    EnergyPotion.skillEffect, CaviarHeal
)
val HuntingMix = BarbarianMix(
    ItemId.HUNTING_MIX_1,
    HuntingPotion.skillEffect, CaviarHeal
)
val SuperStrengthMix = BarbarianMix(
    ItemId.SUP_STR_MIX_1,
    SuperStrengthPotion.skillEffect, CaviarHeal
)
val MagicEssenceMix = BarbarianMix(
    ItemId.MAGIC_ESS_MIX_1,
    MagicEssence.skillEffect, CaviarHeal
)
val SuperRestoreMix = BarbarianMix(
    ItemId.SUP_RESTORE_MIX_1,
    SuperRestorePotion.skillEffect, CaviarHeal
)
val SuperDefenceMix = BarbarianMix(
    ItemId.SUP_DEF_MIX_1,
    SuperDefencePotion.skillEffect, CaviarHeal
)
val ExtraStrongAntiPoisonMix = BarbarianMix(
    ItemId.ANTIDOTEP_MIX_1,
    SuperAntipoison.skillEffect, CaviarHeal
)
val AntifireMix = BarbarianMix(
    ItemId.ANTIFIRE_MIX_1,
    AntifirePotion.skillEffect, CaviarHeal
)
val RangingMix = BarbarianMix(
    ItemId.RANGING_MIX_1,
    RangePotion.skillEffect, CaviarHeal
)
val MagicMix = BarbarianMix(
    ItemId.MAGIC_MIX_1,
    MagicPotion.skillEffect, CaviarHeal
)
val ZamorakMix = BarbarianMix(
    ItemId.ZAMORAK_MIX_1,
    ZamorakBrew.skillEffect, CaviarHeal
)