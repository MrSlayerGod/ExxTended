package org.dementhium.model.player.skills

import kotlin.math.floor

class CombatLevelFormula(
    val baseCombatLevel: Int = 1,
    val prayerDenominator: Double = 2.0,
    val defensiveCoefficient: Double = 0.25,
    val meleeCoefficient: Double = 0.325,
    val rangedCoefficient1: Double = 1.5,
    val rangedCoefficient2: Double = 0.325,
    val magicCoefficient1: Double = 1.5,
    val magicCoefficient2: Double = 0.325,
    val summoningDenominator: Double = 8.0,
) {

    fun Skills.calcDefensive(): Int {
        val defence = getSkill(SkillId.Defence).maxLevel
        val hitpoints = getSkill(SkillId.Hitpoints).maxLevel
        val prayer = getSkill(SkillId.Prayer).maxLevel
        val defensive = (defence + hitpoints + floor(prayer / prayerDenominator)) * defensiveCoefficient
        return defensive.toInt() + baseCombatLevel
    }

    fun Skills.calcMelee(): Double {
        val attackLevel = getSkill(SkillId.Attack).maxLevel
        val strengthLevel = getSkill(SkillId.Strength).maxLevel
        val meleeContribution = (attackLevel + strengthLevel) * meleeCoefficient
        return meleeContribution
    }

    fun Skills.calcRanged(): Double {
        val rangedLevel = getSkill(SkillId.Range).maxLevel
        val rangedContribution = floor(rangedLevel * rangedCoefficient1) * rangedCoefficient2
        return rangedContribution
    }

    fun Skills.calcMagic(): Double {
        val magicLevel = getSkill(SkillId.Magic).maxLevel
        val magicContribution = floor(magicLevel * magicCoefficient1) * magicCoefficient2
        return magicContribution
    }

    fun Skills.calcSummoning(): Int {
        val summoningLevel = getSkill(SkillId.Summoning).maxLevel
        val summoningContribution = (summoningLevel / summoningDenominator)
        return summoningContribution.toInt()
    }

    fun calcCombatLevel(bySkills: Skills): Int = with(bySkills) {
        var totalCombatLevel = 0
        val defensive = calcDefensive()
        totalCombatLevel += defensive
        val melee = calcMelee()
        val ranger = calcRanged()
        val mage = calcMagic()
        totalCombatLevel += when {
            melee >= ranger && melee >= mage -> melee
            ranger >= melee && ranger >= mage -> ranger
            else -> mage
        }.toInt()
        val summoning = calcSummoning()
        totalCombatLevel += summoning
        return totalCombatLevel
    }

    companion object {
        val Default = CombatLevelFormula()
    }
}
