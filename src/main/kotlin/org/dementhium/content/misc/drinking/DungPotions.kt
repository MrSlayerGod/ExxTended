package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId
import org.dementhium.util.minutesToTicks

private val Melee = Attack + Strength
private val Rejuv = Prayer + Summoning
private val StatRestore = SkillId.entries.filter { it != Hitpoints && it !in Rejuv }
private val Gatherers = Woodcutting + Mining + Fishing
private val Artisans = Smithing + Crafting + Fletching + Construction + Firemaking
private val Naturalists = Cooking + Farming + Herblore + Runecrafting
private val Survivalists = Agility + Hunter + Slayer + Thieving

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

val DungWeakMagic = DungeoneeringPotion(ItemId.WEAK_MAGIC_POTION, Magic.WeakBoost())
val DungWeakRanged = DungeoneeringPotion(ItemId.WEAK_RANGED_POTION, Range.WeakBoost())
val DungWeakMelee = DungeoneeringPotion(ItemId.WEAK_MELEE_POTION, Melee.boostAll(flat = 4, percent = 10))
val DungWeakDefence = DungeoneeringPotion(ItemId.WEAK_DEFENCE_POTION, Defence.WeakBoost())
val DungWeakRestore = DungeoneeringPotion(ItemId.WEAK_STAT_RESTORE_POTION,DungRestore(flat = 5, percent = 12))
val DungWeakCure = DungeoneeringPotion(ItemId.WEAK_CURE_POTION,DungCure(minutesToTicks(5)))
val DungWeakRejuv = DungeoneeringPotion(ItemId.WEAK_REJUVENATION_POTION,DungRejuv(flat = 4, percent = 8))
val DungWeakGatherer = DungeoneeringPotion(ItemId.WEAK_GATHERERS_POTION,DungGatherer(flat = 3, percent = 2))
val DungWeakArtisan = DungeoneeringPotion(ItemId.WEAK_ARTISANS_POTION,DungArtisan(flat = 3, percent = 2))
val DungWeakNaturalist = DungeoneeringPotion(ItemId.WEAK_NATURALISTS_POTION,DungNaturalist(flat = 3, percent = 2))
val DungWeakSurvivalist = DungeoneeringPotion(ItemId.WEAK_SURVIVALISTS_POTION,DungSurvivalist(flat = 3, percent = 2))

val DungMagic = DungeoneeringPotion(ItemId.MAGIC_POTION, Magic.BaseBoost())
val DungRanged = DungeoneeringPotion(ItemId.WEAK_RANGED_POTION, Range.BaseBoost())
val DungMelee = DungeoneeringPotion(ItemId.WEAK_MELEE_POTION, Melee.boostAll(flat = 5, percent = 14))
val DungDefence = DungeoneeringPotion(ItemId.WEAK_DEFENCE_POTION, Defence.BaseBoost())
val DungRestore = DungeoneeringPotion(ItemId.WEAK_STAT_RESTORE_POTION,DungRestore(flat = 7, percent = 17))
val DungCure = DungeoneeringPotion(ItemId.WEAK_CURE_POTION,DungCure(minutesToTicks(10)))
val DungRejuv = DungeoneeringPotion(ItemId.WEAK_REJUVENATION_POTION,DungRejuv(flat = 7, percent = 15))
val DungGatherer = DungeoneeringPotion(ItemId.WEAK_GATHERERS_POTION,DungGatherer(flat = 4, percent = 4))
val DungArtisan = DungeoneeringPotion(ItemId.WEAK_ARTISANS_POTION,DungArtisan(flat = 4, percent = 4))
val DungNaturalist = DungeoneeringPotion(ItemId.WEAK_NATURALISTS_POTION,DungNaturalist(flat = 4, percent = 4))
val DungSurvivalist = DungeoneeringPotion(ItemId.WEAK_SURVIVALISTS_POTION,DungSurvivalist(flat = 4, percent = 4))

val DungStrongMagic = DungeoneeringPotion(ItemId.STRONG_MAGIC_POTION, Magic.StrongBoost())
val DungStrongRanged = DungeoneeringPotion(ItemId.STRONG_RANGED_POTION, Range.BaseBoost())
val DungStrongMelee = DungeoneeringPotion(ItemId.STRONG_MELEE_POTION, Melee.boostAll(flat = 6, percent = 20))
val DungStrongDefence = DungeoneeringPotion(ItemId.STRONG_DEFENCE_POTION, Defence.StrongBoost())
val DungStrongRestore = DungeoneeringPotion(ItemId.STRONG_STAT_RESTORE_POTION,DungRestore(flat = 10, percent = 24))
val DungStrongCure = DungeoneeringPotion(ItemId.STRONG_CURE_POTION,DungCure(minutesToTicks(20)))
val DungStrongRejuv = DungeoneeringPotion(ItemId.STRONG_REJUVENATION_POTION,DungRejuv(flat = 10, percent = 22))
val DungStrongGatherer = DungeoneeringPotion(ItemId.STRONG_GATHERERS_POTION,DungGatherer(flat = 6, percent = 6))
val DungStrongArtisan = DungeoneeringPotion(ItemId.STRONG_ARTISANS_POTION,DungArtisan(flat = 6, percent = 6))
val DungStrongNaturalist = DungeoneeringPotion(ItemId.STRONG_NATURALISTS_POTION,DungNaturalist(flat = 6, percent = 6))
val DungStrongSurvivalist = DungeoneeringPotion(ItemId.STRONG_SURVIVALISTS_POTION,DungSurvivalist(flat = 6, percent = 6))
