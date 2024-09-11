package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.skillEffect.*
import org.dementhium.content.misc.skillEffect.builder.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

private val ciderDrains = listOf(Attack, Strength)
private val stoutBoosts = listOf(Mining, Smithing)
private val stoutDrains = ciderDrains + Defence

val Beer = Ale(ItemId.BEER) {
    heal(10)
    Strength.boost(flat = 1, percent = 2)
    Attack.drain(flat = 1, percent = 6)
}

val Grog = Ale(ItemId.GROG) {
    heal(30)
    Strength.boost(flat = 3)
    Attack.drain(flat = 6)
}

val BanditsBrew = Ale(ItemId.BANDITS_BREW) {
    Thieving.boost(flat = 1)
    Attack.boost(flat = 1)
    Strength.drain(flat = 1)
    Defence.drain(flat = 6)
}

val BeerTankard = SingleDose(ItemId.BEER_3803, emptyId = ItemId.TANKARD) {
    heal(40)
    Strength.boost(flat = 2, percent = 4)
    Attack.drain(flat = 1, percent = 2)
}

val Cider = Ale(ItemId.CIDER) {
    heal(20)
    Farming.boost(flat = 1)
    ciderDrains.drain(flat = 2)
}
val MatureCider = Ale(ItemId.MATURE_CIDER) {
    heal(20)
    Farming.boost(flat = 2)
    ciderDrains.drain(flat = 3)
}

val DwarvenStout = Ale(ItemId.DWARVEN_STOUT) {
    heal(10)
    stoutBoosts.boost(flat = 1)
    stoutDrains.drain(flat = 1)
}
val MatureDwarvenStout = Ale(ItemId.DWARVEN_STOUT_M) {
    heal(10)
    stoutBoosts.boost(flat = 2)
    stoutDrains.drain(flat = 2)
}

val AsgarnianAle = Ale(ItemId.ASGARNIAN_ALE) {
    heal(20)
    Strength.boost(flat = 2)
    Attack.drain(flat = 4)
}
val MatureAsgarnianAle = Ale(ItemId.ASGARNIAN_ALE_M) {
    heal(20)
    Strength.boost(flat = 3)
    Attack.drain(flat = 5)
}

val WizardMindBomb = Ale(ItemId.WIZARDS_MIND_BOMB) {
    heal(10)
    Magic.boost(flat = 2, percent = 2)
    Attack.drain(flat = 4)
    Strength.drain(flat = 3)
    Defence.drain(flat = 3)
}
val MatureWizardMindBomb = Ale(ItemId.MATURE_WMB) {
    heal(10)
    Magic.boost(flat = 3, percent = 3)
    Attack.drain(flat = 5)
    Strength.drain(flat = 4)
    Defence.drain(flat = 4)
}

val DragonBitter = Ale(ItemId.DRAGON_BITTER) {
    heal(10)
    Strength.boost(2)
    Attack.drain(1)
}
val MatureDragonBitter = Ale(ItemId.DRAGON_BITTER_M) {
    heal(10)
    Strength.boost(3)
    Attack.drain(2)
}

val MoonlightMead = Ale(
    ItemId.MOONLIGHT_MEAD,
    HealBy(50)
)
val MatureMoonlightMead = Ale(
    ItemId.MOONLIGHT_MEAD_M,
    HealBy(60)
)

val AxemansFolly = Ale(ItemId.AXEMANS_FOLLY) {
    Woodcutting.boost(flat = 1)
    Strength.boost(flat = 3)
}
val MatureAxemansFolly = Ale(ItemId.AXEMANS_FOLLY) {
    Woodcutting.boost(flat = 2)
    Strength.drain(flat = 4)
}

val ChefsDelight = Ale(ItemId.CHEFS_DELIGHT) {
    Cooking.boost(flat = 1, percent = 5)
    ciderDrains.drain(flat = 2)
}
val MatureChefsDelight = Ale(ItemId.CHEFS_DELIGHT_M) {
    Cooking.boost(flat = 2, percent = 5)
    ciderDrains.drain(flat = 3)
}

val SlayersRespite = Ale(ItemId.SLAYERS_RESPITE) {
    Slayer.boost(flat = 1)
    ciderDrains.drain(flat = 2)
}
val MatureSlayersRespite = Ale(ItemId.SLAYERS_RESPITE) {
    Slayer.boost(flat = 2)
    ciderDrains.drain(flat = 3)
}

val CiderKeg = Keg(ItemId.CIDER_1, Cider)
val MatureCiderKeg = Keg(ItemId.CIDER_M1, MatureCider)
val StoutKeg = Keg(ItemId.DWARVEN_STOUT_1, DwarvenStout)
val MatureStoutKeg = Keg(ItemId.DWARVEN_STOUT_M1, MatureDwarvenStout)
val AsgarnianKeg = Keg(ItemId.ASGARNIAN_ALE_1, AsgarnianAle)
val MatureAsgarnianKeg = Keg(ItemId.ASGARNIAN_ALE_M1, MatureAsgarnianAle)
val MindBombKeg = Keg(ItemId.MIND_BOMB_1, WizardMindBomb)
val MatureMindBombKeg = Keg(ItemId.MIND_BOMB_M1, MatureWizardMindBomb)
val DragonBitterKeg = Keg(ItemId.DRAGON_BITTER_1, DragonBitter)
val MatureDragonBitterKeg = Keg(ItemId.DRAGON_BITTER_M1, MatureDragonBitter)
val MoonlightMeadKeg = Keg(ItemId.MOONLIGHT_MEAD_1, MoonlightMead)
val MatureMoonlightMeadKeg = Keg(ItemId.MLIGHT_MEAD_M1, MatureMoonlightMead)
val AxemansFollyKeg = Keg(ItemId.AXEMANS_FOLLY_1, AxemansFolly)
val MatureAxemansFollyKeg = Keg(ItemId.AXEMANS_FOLLY_M1, MatureAxemansFolly)
val ChefsDelightKeg = Keg(ItemId.CHEFS_DELIGHT_1, ChefsDelight)
val MatureChefsDelightKeg = Keg(ItemId.CHEFS_DELIGHT_M1, MatureChefsDelight)
val SlayersRespiteKeg = Keg(ItemId.SLAYERS_RESPITE_1, SlayersRespite)
val MatureSlayersRespiteKeg = Keg(ItemId.SLAYER_RESPITE_M1, MatureSlayersRespite)
