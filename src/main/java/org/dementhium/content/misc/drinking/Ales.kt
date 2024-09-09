package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.plus
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

private val ciderDrains = listOf(Attack, Strength)
private val stoutBoosts = listOf(Mining, Smithing)
private val stoutDrains = ciderDrains + Defence

data object Beer: Ale(
    ItemId.BEER,
    HealBy(10) + Strength.Boost(flat = 1, percent = 2) + Attack.Drain(flat = 1, percent = 6)
)
data object BeerTankard: SingleDose(
    ItemId.BEER_3803,
    HealBy(40) + Strength.Boost(flat = 2, percent = 4) + Attack.Drain(flat = 1, percent = 2),
    emptyId = ItemId.TANKARD
)

data object Cider: Ale(
    ItemId.CIDER,
    HealBy(20) + Farming.Boost(flat = 1) + ciderDrains.drainAll(flat = 2)
)
data object MatureCider: Ale(
    ItemId.MATURE_CIDER,
    HealBy(20) + Farming.Boost(flat = 2) + ciderDrains.drainAll(flat = 3)
)
data object DwarvenStout: Ale(
    ItemId.DWARVEN_STOUT,
    HealBy(10) + stoutBoosts.boostAll(flat = 1) + stoutDrains.drainAll(flat = 1)
)
data object MatureDwarvenStout: Ale(
    ItemId.DWARVEN_STOUT_M,
    HealBy(10) + stoutBoosts.boostAll(flat = 2) + stoutDrains.drainAll(flat = 2)
)
data object AsgarnianAle: Ale(
    ItemId.ASGARNIAN_ALE,
    HealBy(20) + Strength.Boost(flat = 2) + Attack.Drain(flat = 4)
)
data object MatureAsgarnianAle: Ale(
    ItemId.ASGARNIAN_ALE_M,
    HealBy(20) + Strength.Boost(flat = 3) + Attack.Drain(flat = 5)
)
data object WizardMindBomb: Ale(
    ItemId.WIZARDS_MIND_BOMB,
    HealBy(10) + Magic.Boost(flat = 2, percent = 2) + Attack.Drain(flat = 4) + Strength.Drain(flat = 3) + Defence.Drain(flat = 3)
)
data object MatureWizardMindBomb: Ale(
    ItemId.MATURE_WMB,
    HealBy(10) + Magic.Boost(flat = 3, percent = 3) + Attack.Drain(flat = 5) + Strength.Drain(flat = 4) + Defence.Drain(flat = 4)
)
data object DragonBitter: Ale(
    ItemId.DRAGON_BITTER,
    HealBy(10) + Strength.Boost(2) + Attack.Drain(1)
)
data object MatureDragonBitter: Ale(
    ItemId.DRAGON_BITTER_M,
    HealBy(10) + Strength.Boost(3) + Attack.Drain(2)
)
data object MoonlightMead: Ale(
    ItemId.MOONLIGHT_MEAD,
    HealBy(50)
)
data object MatureMoonlightMead: Ale(
    ItemId.MOONLIGHT_MEAD_M,
    HealBy(60)
)
data object AxemansFolly: Ale(
    ItemId.AXEMANS_FOLLY,
    Woodcutting.Boost(flat=1) + Strength.Drain(flat = 3)
)
data object MatureAxemansFolly: Ale(
    ItemId.AXEMANS_FOLLY,
    Woodcutting.Boost(flat=2) + Strength.Drain(flat = 4)
)
data object ChefsDelight: Ale(
    ItemId.CHEFS_DELIGHT,
    Cooking.Boost(flat = 1, percent = 5) + ciderDrains.drainAll(flat =  2)
)
data object MatureChefsDelight: Ale(
    ItemId.CHEFS_DELIGHT_M,
    Cooking.Boost(flat = 2, percent = 5) + ciderDrains.drainAll(flat =  3)
)
data object SlayersRespite: Ale(
    ItemId.SLAYERS_RESPITE,
    Slayer.Boost(flat = 1) + ciderDrains.drainAll(flat = 2)
)
data object MatureSlayersRespite: Ale(
    ItemId.SLAYERS_RESPITE,
    Slayer.Boost(flat = 2) + ciderDrains.drainAll(flat = 3)
)
data object CiderKeg: Keg(ItemId.CIDER_1, Cider)
data object MatureCiderKeg: Keg(ItemId.CIDER_M1, MatureCider)
data object StoutKeg: Keg(ItemId.DWARVEN_STOUT_1, DwarvenStout)
data object MatureStoutKeg: Keg(ItemId.DWARVEN_STOUT_M1, MatureDwarvenStout)
data object AsgarnianKeg: Keg(ItemId.ASGARNIAN_ALE_1, AsgarnianAle)
data object MatureAsgarnianKeg: Keg(ItemId.ASGARNIAN_ALE_M1, MatureAsgarnianAle)
data object MindBombKeg: Keg(ItemId.MIND_BOMB_1, WizardMindBomb)
data object MatureMindBombKeg: Keg(ItemId.MIND_BOMB_M1, MatureWizardMindBomb)
data object DragonBitterKeg: Keg(ItemId.DRAGON_BITTER_1, DragonBitter)
data object MatureDragonBitterKeg: Keg(ItemId.DRAGON_BITTER_M1, MatureDragonBitter)
data object MoonlightMeadKeg: Keg(ItemId.MOONLIGHT_MEAD_1, MoonlightMead)
data object MatureMoonlightMeadKeg: Keg(ItemId.MLIGHT_MEAD_M1, MatureMoonlightMead)
data object AxemansFollyKeg: Keg(ItemId.AXEMANS_FOLLY_1, AxemansFolly)
data object MatureAxemansFollyKeg: Keg(ItemId.AXEMANS_FOLLY_M1, MatureAxemansFolly)
data object ChefsDelightKeg: Keg(ItemId.CHEFS_DELIGHT_1, ChefsDelight)
data object MatureChefsDelightKeg: Keg(ItemId.CHEFS_DELIGHT_M1, MatureChefsDelight)
data object SlayersRespiteKeg: Keg(ItemId.SLAYERS_RESPITE_1, SlayersRespite)
data object MatureSlayersRespiteKeg: Keg(ItemId.SLAYER_RESPITE_M1, MatureSlayersRespite)
