package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.plus
import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.ItemId
import org.dementhium.util.minutesToTicks

private val Rejuv = listOf(SkillId.Prayer, SkillId.Summoning)
private val StatRestore = SkillId.entries.filter { it != SkillId.Hitpoints && it !in Rejuv }
private val Gatherers = listOf(SkillId.Woodcutting, SkillId.Mining, SkillId.Fletching)
private val Artisans = listOf(SkillId.Smithing, SkillId.Crafting, SkillId.Fletching, SkillId.Construction, SkillId.Firemaking)
private val Naturalists = listOf(SkillId.Cooking, SkillId.Farming, SkillId.Herblore, SkillId.Runecrafting)
private val Survivalists = listOf(SkillId.Agility, SkillId.Hunter, SkillId.Thieving, SkillId.Slayer)

private fun SkillId.WeakBoost() = Boost(flat = 4, percent = 10)
private fun SkillId.BaseBoost() = Boost(flat = 5, percent = 14)
private fun SkillId.StrongBoost() = Boost(flat = 6, percent = 20)

private fun DungRestore(flat: Int, percent: Int) = StatRestore.restoreAll(flat, percent)
private fun DungRejuv(flat: Int, percent: Int) = Rejuv.restoreAll(flat, percent)
private fun DungGatherer(flat: Int, percent: Int) = Gatherers.boostAll(flat, percent)
private fun DungArtisan(flat: Int, percent: Int) = Artisans.boostAll(flat, percent)
private fun DungNaturalist(flat: Int, percent: Int) = Naturalists.boostAll(flat, percent)
private fun DungSurvivalist(flat: Int, percent: Int) = Survivalists.boostAll(flat, percent)
private fun DungCure(immunityTicks: Int) = CureDisease(immunityTicks) + CurePoison(immunityTicks) + DragonfireImmune(immunityTicks)

data object DungWeakMagic: DungeoneeringPotion(ItemId.WEAK_MAGIC_POTION, SkillId.Magic.WeakBoost())
data object DungWeakRanged: DungeoneeringPotion(ItemId.WEAK_RANGED_POTION, SkillId.Range.WeakBoost())
data object DungWeakMelee: DungeoneeringPotion(ItemId.WEAK_MELEE_POTION,SkillId.Attack.WeakBoost())
data object DungWeakDefence: DungeoneeringPotion(ItemId.WEAK_DEFENCE_POTION,SkillId.Defence.WeakBoost())
data object DungWeakRestore: DungeoneeringPotion(ItemId.WEAK_STAT_RESTORE_POTION,DungRestore(flat = 5, percent = 12))
data object DungWeakCure: DungeoneeringPotion(ItemId.WEAK_CURE_POTION,DungCure(minutesToTicks(5)))
data object DungWeakRejuv: DungeoneeringPotion(ItemId.WEAK_REJUVENATION_POTION,DungRejuv(flat = 4, percent = 8))
data object DungWeakGatherer: DungeoneeringPotion(ItemId.WEAK_GATHERERS_POTION,DungGatherer(flat = 3, percent = 2))
data object DungWeakArtisan: DungeoneeringPotion(ItemId.WEAK_ARTISANS_POTION,DungArtisan(flat = 3, percent = 2))
data object DungWeakNaturalist: DungeoneeringPotion(ItemId.WEAK_NATURALISTS_POTION,DungNaturalist(flat = 3, percent = 2))
data object DungWeakSurvivalist: DungeoneeringPotion(ItemId.WEAK_SURVIVALISTS_POTION,DungSurvivalist(flat = 3, percent = 2))

data object DungMagic: DungeoneeringPotion(ItemId.MAGIC_POTION,SkillId.Magic.BaseBoost())
data object DungRanged: DungeoneeringPotion(ItemId.WEAK_RANGED_POTION,SkillId.Range.BaseBoost())
data object DungMelee: DungeoneeringPotion(ItemId.WEAK_MELEE_POTION,SkillId.Attack.BaseBoost())
data object DungDefence: DungeoneeringPotion(ItemId.WEAK_DEFENCE_POTION,SkillId.Defence.BaseBoost())
data object DungRestore: DungeoneeringPotion(ItemId.WEAK_STAT_RESTORE_POTION,DungRestore(flat = 7, percent = 17))
data object DungCure: DungeoneeringPotion(ItemId.WEAK_CURE_POTION,DungCure(minutesToTicks(10)))
data object DungRejuv: DungeoneeringPotion(ItemId.WEAK_REJUVENATION_POTION,DungRejuv(flat = 7, percent = 15))
data object DungGatherer: DungeoneeringPotion(ItemId.WEAK_GATHERERS_POTION,DungGatherer(flat = 4, percent = 4))
data object DungArtisan: DungeoneeringPotion(ItemId.WEAK_ARTISANS_POTION,DungArtisan(flat = 4, percent = 4))
data object DungNaturalist: DungeoneeringPotion(ItemId.WEAK_NATURALISTS_POTION,DungNaturalist(flat = 4, percent = 4))
data object DungSurvivalist: DungeoneeringPotion(ItemId.WEAK_SURVIVALISTS_POTION,DungSurvivalist(flat = 4, percent = 4))

data object DungStrongMagic: DungeoneeringPotion(ItemId.STRONG_MAGIC_POTION,SkillId.Magic.StrongBoost())
data object DungStrongRanged: DungeoneeringPotion(ItemId.STRONG_RANGED_POTION,SkillId.Range.BaseBoost())
data object DungStrongMelee: DungeoneeringPotion(ItemId.STRONG_MELEE_POTION,SkillId.Attack.StrongBoost())
data object DungStrongDefence: DungeoneeringPotion(ItemId.STRONG_DEFENCE_POTION,SkillId.Defence.StrongBoost())
data object DungStrongRestore: DungeoneeringPotion(ItemId.STRONG_STAT_RESTORE_POTION,DungRestore(flat = 10, percent = 24))
data object DungStrongCure: DungeoneeringPotion(ItemId.STRONG_CURE_POTION,DungCure(minutesToTicks(20)))
data object DungStrongRejuv: DungeoneeringPotion(ItemId.STRONG_REJUVENATION_POTION,DungRejuv(flat = 10, percent = 22))
data object DungStrongGatherer: DungeoneeringPotion(ItemId.STRONG_GATHERERS_POTION,DungGatherer(flat = 6, percent = 6))
data object DungStrongArtisan: DungeoneeringPotion(ItemId.STRONG_ARTISANS_POTION,DungArtisan(flat = 6, percent = 6))
data object DungStrongNaturalist: DungeoneeringPotion(ItemId.STRONG_NATURALISTS_POTION,DungNaturalist(flat = 6, percent = 6))
data object DungStrongSurvivalist: DungeoneeringPotion(ItemId.STRONG_SURVIVALISTS_POTION,DungSurvivalist(flat = 6, percent = 6))
