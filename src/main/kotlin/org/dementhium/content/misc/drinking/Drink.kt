package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.SkillEffect
import org.dementhium.util.*

sealed class Drink(
    val doses: Doses,
    val skillEffect: SkillEffect,
    open val emptyId: Int = ItemId.VIAL,
) {

    companion object {
        fun values(): Array<Drink> = arrayOf(
            ATTACK_POTION, DefencePotion, STRENGTH_POTION, MAGIC_POTION, RANGE_POTION, CombatPotion,
            RESTORE_POTION, EnergyPotion, PRAYER_POTION, SummoningPotion,

            SUPER_ATTACK_POTION, SUPER_DEFENCE_POTION, SUPER_STRENGTH_POTION,
            SUPER_RESTORE_POTION, SuperEnergyPotion, SuperPrayer, OVERLOAD,

            ExtremeAttack, ExtremeDefence, ExtremeStrength, ExtremeRange, ExtremeMagic,

            ANTIPOISON, SuperAntipoison, AntidodeP, AntidodePP, AntifirePotion, RelicymBalm, SanfewSerum,

            AgilityPotion, FishingPotion, HuntingPotion, FletchingPotion, CraftingPotion, MagicEssence,

            GuthixRest, ZAMORAK_BREW, SARADOMIN_BREW,

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

            BottleOfWine, Brandy, BraindeathRum, Gin, HalfFullWine, JugOfWine, KarmajaRum, BeerKeg, Vodka, Whisky,

            FruitBlast, PineapplePunch, WizardBlizzard, ShortGreenGuy, DrunkDragon, ChocSaturday, BlurberrySpecial,

            CwSuperAttack, CwSuperStrength, CwSuperDefence, CwSuperEnergy, CwSuperRanging, CwSuperMagic
        )


        fun valueOf(value: String): Drink = when(value) {
            "ATTACK_POTION" -> ATTACK_POTION
            "DefencePotion" -> DefencePotion
            "STRENGTH_POTION" -> STRENGTH_POTION
            "MAGIC_POTION" -> MAGIC_POTION
            "RANGE_POTION" -> RANGE_POTION
            "CombatPotion" -> CombatPotion
            "RESTORE_POTION" -> RESTORE_POTION
            "EnergyPotion" -> EnergyPotion
            "PRAYER_POTION" -> PRAYER_POTION
            "SummoningPotion" -> SummoningPotion
            "SUPER_ATTACK_POTION" -> SUPER_ATTACK_POTION
            "SUPER_DEFENCE_POTION" -> SUPER_DEFENCE_POTION
            "SUPER_STRENGTH_POTION" -> SUPER_STRENGTH_POTION
            "SUPER_RESTORE_POTION" -> SUPER_RESTORE_POTION
            "SuperEnergyPotion" -> SuperEnergyPotion
            "SuperPrayer" -> SuperPrayer
            "OVERLOAD" -> OVERLOAD
            "ExtremeAttack" -> ExtremeAttack
            "ExtremeDefence" -> ExtremeDefence
            "ExtremeStrength" -> ExtremeStrength
            "ExtremeRange" -> ExtremeRange
            "ExtremeMagic" -> ExtremeMagic
            "ANTIPOISON" -> ANTIPOISON
            "SuperAntipoison" -> SuperAntipoison
            "AntidodeP" -> AntidodeP
            "AntidodePP" -> AntidodePP
            "AntifirePotion" -> AntifirePotion
            "RelicymBalm" -> RelicymBalm
            "SanfewSerum" -> SanfewSerum
            "AgilityPotion" -> AgilityPotion
            "FishingPotion" -> FishingPotion
            "HuntingPotion" -> HuntingPotion
            "FletchingPotion" -> FletchingPotion
            "CraftingPotion" -> CraftingPotion
            "MagicEssence" -> MagicEssence
            "GuthixRest" -> GuthixRest
            "ZAMORAK_BREW" -> ZAMORAK_BREW
            "SARADOMIN_BREW" -> SARADOMIN_BREW
            "DungWeakMagic" -> DungWeakMagic
            "DungWeakRanged" -> DungWeakRanged
            "DungWeakMelee" -> DungWeakMelee
            "DungWeakDefence" -> DungWeakDefence
            "DungWeakRestore" -> DungWeakRestore
            "DungWeakCure" -> DungWeakCure
            "DungWeakRejuv" -> DungWeakRejuv
            "DungWeakGatherer" -> DungWeakGatherer
            "DungWeakArtisan" -> DungWeakArtisan
            "DungWeakNaturalist" -> DungWeakNaturalist
            "DungWeakSurvivalist" -> DungWeakSurvivalist
            "DungMagic" -> DungMagic
            "DungRanged" -> DungRanged
            "DungMelee" -> DungMelee
            "DungDefence" -> DungDefence
            "DungRestore" -> DungRestore
            "DungCure" -> DungCure
            "DungRejuv" -> DungRejuv
            "DungGatherer" -> DungGatherer
            "DungArtisan" -> DungArtisan
            "DungNaturalist" -> DungNaturalist
            "DungSurvivalist" -> DungSurvivalist
            "DungStrongMagic" -> DungStrongMagic
            "DungStrongRanged" -> DungStrongRanged
            "DungStrongMelee" -> DungStrongMelee
            "DungStrongDefence" -> DungStrongDefence
            "DungStrongRestore" -> DungStrongRestore
            "DungStrongCure" -> DungStrongCure
            "DungStrongRejuv" -> DungStrongRejuv
            "DungStrongGatherer" -> DungStrongGatherer
            "DungStrongArtisan" -> DungStrongArtisan
            "DungStrongNaturalist" -> DungStrongNaturalist
            "DungStrongSurvivalist" -> DungStrongSurvivalist
            "AttackMix" -> AttackMix
            "AntipoisonMix" -> AntipoisonMix
            "RelicymMix" -> RelicymMix
            "StrengthMix" -> StrengthMix
            "StatRestoreMix" -> StatRestoreMix
            "EnergyMix" -> EnergyMix
            "DefenceMix" -> DefenceMix
            "AgilityMix" -> AgilityMix
            "CombatMix" -> CombatMix
            "PrayerMix" -> PrayerMix
            "SuperAttackMix" -> SuperAttackMix
            "SuperAntiPoisonMix" -> SuperAntiPoisonMix
            "FishingMix" -> FishingMix
            "SuperEnergyMix" -> SuperEnergyMix
            "HuntingMix" -> HuntingMix
            "SuperStrengthMix" -> SuperStrengthMix
            "MagicEssenceMix" -> MagicEssenceMix
            "SuperRestoreMix" -> SuperRestoreMix
            "SuperDefenceMix" -> SuperDefenceMix
            "ExtraStrongAntiPoisonMix" -> ExtraStrongAntiPoisonMix
            "AntifireMix" -> AntifireMix
            "RangingMix" -> RangingMix
            "MagicMix" -> MagicMix
            "ZamorakMix" -> ZamorakMix
            "Beer" -> Beer
            "BeerTankard" -> BeerTankard
            "Cider" -> Cider
            "MatureCider" -> MatureCider
            "DwarvenStout" -> DwarvenStout
            "MatureDwarvenStout" -> MatureDwarvenStout
            "AsgarnianAle" -> AsgarnianAle
            "MatureAsgarnianAle" -> MatureAsgarnianAle
            "WizardMindBomb" -> WizardMindBomb
            "MatureWizardMindBomb" -> MatureWizardMindBomb
            "DragonBitter" -> DragonBitter
            "MatureDragonBitter" -> MatureDragonBitter
            "MoonlightMead" -> MoonlightMead
            "MatureMoonlightMead" -> MatureMoonlightMead
            "AxemansFolly" -> AxemansFolly
            "MatureAxemansFolly" -> MatureAxemansFolly
            "ChefsDelight" -> ChefsDelight
            "MatureChefsDelight" -> MatureChefsDelight
            "SlayersRespite" -> SlayersRespite
            "MatureSlayersRespite" -> MatureSlayersRespite
            "CiderKeg" -> CiderKeg
            "MatureCiderKeg" -> MatureCiderKeg
            "StoutKeg" -> StoutKeg
            "MatureStoutKeg" -> MatureStoutKeg
            "AsgarnianKeg" -> AsgarnianKeg
            "MindBombKeg" -> MindBombKeg
            "MatureMindBombKeg" -> MatureMindBombKeg
            "DragonBitterKeg" -> DragonBitterKeg
            "MatureDragonBitterKeg" -> MatureDragonBitterKeg
            "MoonlightMeadKeg" -> MoonlightMeadKeg
            "MatureMoonlightMeadKeg" -> MatureMoonlightMeadKeg
            "AxemansFollyKeg" -> AxemansFollyKeg
            "MatureAxemansFollyKeg" -> MatureAxemansFollyKeg
            "ChefsDelightKeg" -> ChefsDelightKeg
            "MatureChefsDelightKeg" -> MatureChefsDelightKeg
            "SlayersRespiteKeg" -> SlayersRespiteKeg
            "MatureSlayersRespiteKeg" -> MatureSlayersRespiteKeg
            "BottleOfWine" -> BottleOfWine
            "Brandy" -> Brandy
            "BraindeathRum" -> BraindeathRum
            "Gin" -> Gin
            "HalfFullWine" -> HalfFullWine
            "JugOfWine" -> JugOfWine
            "KarmajaRum" -> KarmajaRum
            "BeerKeg" -> BeerKeg
            "Vodka" -> Vodka
            "Whisky" -> Whisky
            "FruitBlast" -> FruitBlast
            "PineapplePunch" -> PineapplePunch
            "WizardBlizzard" -> WizardBlizzard
            "ShortGreenGuy" -> ShortGreenGuy
            "DrunkDragon" -> DrunkDragon
            "ChocSaturday" -> ChocSaturday
            "BlurberrySpecial" -> BlurberrySpecial
            "CwSuperAttack" -> CwSuperAttack
            "CwSuperStrength" -> CwSuperStrength
            "CwSuperDefence" -> CwSuperDefence
            "CwSuperEnergy" -> CwSuperEnergy
            "CwSuperRanging" -> CwSuperRanging
            "CwSuperMagic" -> CwSuperMagic
            else -> throw IllegalArgumentException("No object org.dementhium.content.misc.Drink.$value")
        }
    }
}
