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

data object AttackMix: BarbarianMix(
    ItemId.ATTACK_MIX_1,
    AttackPotion.skillEffect, RoeHeal
)
data object AntipoisonMix: BarbarianMix(
    ItemId.ANTIPOISON_MIX_1,
    AntiPoison.skillEffect, RoeHeal
)
data object RelicymMix: BarbarianMix(
    ItemId.RELICYMS_MIX_1,
    RelicymBalm.skillEffect, RoeHeal
)
data object StrengthMix: BarbarianMix(
    ItemId.STRENGTH_MIX_1,
    StrengthPotion.skillEffect, RoeHeal
)
data object StatRestoreMix: BarbarianMix(
    ItemId.RESTORE_MIX_1,
    RestoreMix.restoreAll(flat = 10, percent = 30), RoeHeal
)
data object EnergyMix: BarbarianMix(
    ItemId.ENERGY_MIX_1,
    RestoreEnergy(percent = 10), CaviarHeal
)
data object DefenceMix: BarbarianMix(
    ItemId.DEFENCE_MIX_1,
    DefencePotion.skillEffect, CaviarHeal
)
data object AgilityMix: BarbarianMix(
    ItemId.AGILITY_MIX_1,
    AgilityPotion.skillEffect, CaviarHeal
)
data object CombatMix: BarbarianMix(
    ItemId.COMBAT_MIX_1,
    CombatPotion.skillEffect, CaviarHeal
)
data object PrayerMix: BarbarianMix(
    ItemId.PRAYER_MIX_1,
    PrayerPotion.skillEffect, CaviarHeal
)
data object SuperAttackMix: BarbarianMix(
    ItemId.SUPERATTACK_MIX_1,
    SuperAttackPotion.skillEffect, CaviarHeal
)
data object SuperAntiPoisonMix: BarbarianMix(
    ItemId.ANTI_P_SUPERMIX_1,
    SuperAntipoison.skillEffect, CaviarHeal
)
data object FishingMix: BarbarianMix(
    ItemId.FISHING_MIX_1,
    FishingPotion.skillEffect, CaviarHeal
)
data object SuperEnergyMix: BarbarianMix(
    ItemId.SUP_ENERGY_MIX_1,
    EnergyPotion.skillEffect, CaviarHeal
)
data object HuntingMix: BarbarianMix(
    ItemId.HUNTING_MIX_1,
    HuntingPotion.skillEffect, CaviarHeal
)
data object SuperStrengthMix: BarbarianMix(
    ItemId.SUP_STR_MIX_1,
    SuperStrengthPotion.skillEffect, CaviarHeal
)
data object MagicEssenceMix: BarbarianMix(
    ItemId.MAGIC_ESS_MIX_1,
    MagicEssence.skillEffect, CaviarHeal
)
data object SuperRestoreMix: BarbarianMix(
    ItemId.SUP_RESTORE_MIX_1,
    SuperRestorePotion.skillEffect, CaviarHeal
)
data object SuperDefenceMix: BarbarianMix(
    ItemId.SUP_DEF_MIX_1,
    SuperDefencePotion.skillEffect, CaviarHeal
)
data object ExtraStrongAntiPoisonMix: BarbarianMix(
    ItemId.ANTIDOTEP_MIX_1,
    SuperAntipoison.skillEffect, CaviarHeal
)
data object AntifireMix: BarbarianMix(
    ItemId.ANTIFIRE_MIX_1,
    AntifirePotion.skillEffect, CaviarHeal
)
data object RangingMix: BarbarianMix(
    ItemId.RANGING_MIX_1,
    RangePotion.skillEffect, CaviarHeal
)
data object MagicMix: BarbarianMix(
    ItemId.MAGIC_MIX_1,
    MagicPotion.skillEffect, CaviarHeal
)
data object ZamorakMix: BarbarianMix(
    ItemId.ZAMORAK_MIX_1,
    ZamorakBrew.skillEffect, CaviarHeal
)