package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.ConsumableStages
import org.dementhium.content.misc.skillEffect.SkillEffect

/** Drink is generally a consumable that leaves some sort of empty container when it is depleted **/
class Drink(
    val consumableIds: ConsumableStages,
    val skillEffect: SkillEffect,
) {

    companion object {
        fun values(): Array<Drink> = arrayOf(
            AttackPotion, DefencePotion, StrengthPotion, MagicPotion, RangePotion, CombatPotion,
            RestorePotion, EnergyPotion, PrayerPotion, SummoningPotion,

            SuperAttackPotion, SuperDefencePotion, SuperStrengthPotion,
            SuperRestorePotion, SuperEnergyPotion, SuperPrayer, Overload,

            ExtremeAttack, ExtremeDefence, ExtremeStrength, ExtremeRange, ExtremeMagic,

            AntiPoison, SuperAntipoison, AntidodeP, AntidodePP, AntifirePotion, RelicymBalm, SanfewSerum,

            AgilityPotion, FishingPotion, HuntingPotion, FletchingPotion, CraftingPotion, MagicEssence,

            GuthixRest, ZamorakBrew, SaradominBrew,

            DungWeakMagic, DungWeakRanged, DungWeakMelee, DungWeakDefence, DungWeakRestore, DungWeakCure,
            DungWeakRejuv, DungWeakGatherer, DungWeakArtisan, DungWeakNaturalist, DungWeakSurvivalist,
            DungMagic, DungRanged, DungMelee, DungDefence, DungRestore, DungCure,
            DungRejuv, DungGatherer, DungArtisan, DungNaturalist, DungSurvivalist,
            DungStrongMagic, DungStrongRanged, DungStrongMelee, DungStrongDefence, DungStrongRestore, DungStrongCure,
            DungStrongRejuv, DungStrongGatherer, DungStrongArtisan, DungStrongNaturalist, DungStrongSurvivalist,

            AttackMix, AntipoisonMix, RelicymMix, StrengthMix, StatRestoreMix, EnergyMix,
            DefenceMix, AgilityMix, CombatMix, PrayerMix, SuperAttackMix, SuperAntiPoisonMix,
            FishingMix, SuperEnergyMix, HuntingMix, SuperStrengthMix, MagicEssenceMix, SuperRestoreMix,
            SuperDefenceMix, ExtraStrongAntiPoisonMix, AntifireMix, RangingMix, MagicMix, ZamorakMix,

            Beer, BeerTankard, Cider, MatureCider, DwarvenStout, MatureDwarvenStout, AsgarnianAle, MatureAsgarnianAle,
            WizardMindBomb, MatureWizardMindBomb, DragonBitter, MatureDragonBitter, MoonlightMead, MatureMoonlightMead,
            AxemansFolly, MatureAxemansFolly, ChefsDelight, MatureChefsDelight, SlayersRespite, MatureSlayersRespite,
            CiderKeg, MatureCiderKeg, StoutKeg, MatureStoutKeg, AsgarnianKeg, MatureAsgarnianKeg,
            MindBombKeg, MatureMindBombKeg, DragonBitterKeg, MatureDragonBitterKeg, MoonlightMeadKeg, MatureMoonlightMeadKeg,
            AxemansFollyKeg, MatureAxemansFollyKeg, ChefsDelightKeg, MatureChefsDelightKeg, SlayersRespiteKeg, MatureSlayersRespiteKeg,

            BottleOfWine, Brandy, BraindeathRum, Gin, HalfFullWine, JugOfWine, KarmajaRum, BeerKeg, Vodka, Whisky, CupOfTea,

            FruitBlast, PineapplePunch, WizardBlizzard, ShortGreenGuy, DrunkDragon, ChocSaturday, BlurberrySpecial,

            CwSuperAttack, CwSuperStrength, CwSuperDefence, CwSuperEnergy, CwSuperRanging, CwSuperMagic
        )
    }
}
