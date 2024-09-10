package org.dementhium.model.player.skills

import kotlin.math.floor
import kotlin.math.pow

data class ExperienceFormula(
    val coefficient: Double = 300.0,
    val base: Double = 2.0,
    val expDenominator: Double = 7.0,
    val outputDenominator: Double = 4.0,
) {

    val Int.delta: Double get() = floor(this + (coefficient * base.pow(this / expDenominator)))

    fun calcExpForLevel(forLevel: Int): Double {
        assert(forLevel > -1) { "Cannot calculate experience for a level below 0 ($forLevel)s" }
        val isZero = forLevel == 0
        var total = 0.0
        val minimumLevel = if (isZero) 0 else 1
        val expDeduction = if (isZero) 0.0 else levelZero
        val levelRange = minimumLevel ..< forLevel
        levelRange.forEach { level ->
            val delta = levelDeltas.getOrElse(level) { level.delta }
            total += delta
        }
        val final = floor(total / outputDenominator) - expDeduction
        return final
    }

    fun calcLevelForExp(experience: Double, skillId: SkillId): Int {
        if (experience < 0) return 1
        experienceThresholds.forEachIndexed { level, threshold ->
            if (level > skillId.maxAllowedLevel) return skillId.maxAllowedLevel
            val nextThreshold = experienceThresholds[level + 1]
            if (experience in threshold .. nextThreshold) {
                return level + 1
            }
        }
        return skillId.maxAllowedLevel
    }

    private val levelZero: Double by lazy {
        calcExpForLevel(0)
    }

    private val levelDeltas: List<Double> = buildList {
        levelRange.forEach { level ->
            add(level.delta)
        }
    }

    private val experienceThresholds: List<Double> = buildList {
        levelRange.forEach { level ->
            add(calcExpForLevel(level))
        }
    }

    companion object {
        val levelRange = 0 .. 127
        val Default = ExperienceFormula()
    }
}

fun main() {
    val formula = ExperienceFormula.Default
    val skillId = SkillId.Hitpoints
    val level = 43
    val expForLevel = formula.calcExpForLevel(level)
    println(expForLevel)
    println(formula.calcLevelForExp(expForLevel, skillId))
}
