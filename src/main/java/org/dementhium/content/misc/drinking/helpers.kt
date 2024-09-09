package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.SkillEffect
import org.dementhium.model.World
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.Skills
import org.dementhium.util.math.percentOf
import org.dementhium.util.submitTickable

class BoostDrainContext(private val skills: Skills, private val skillId: SkillId) {
    val currentLevel get() = with(skills) { skillId.currentLevel }
    val maximumLevel get() = with(skills) { skillId.maximumLevel }
}

/* 1 dose, 2 dose, 3 dose, 4 dose, etc */
@JvmInline
value class Doses(val doseIds: List<Int>): List<Int> by doseIds {
    constructor(vararg doseIds: Int): this(doseIds.map { it })
}

fun SkillId.Restore(
    flat: Int = 0,
    percent: Int = 0
) = SkillEffect {
    val boostAmount = (percent percentOf maximumLevel) + flat
    boostLevel(this@Restore, boostAmount, cap = maximumLevel)
}

fun BoostBy(skillId: SkillId, calculateBoost: BoostDrainContext.() -> Int) = SkillEffect {
    val context = BoostDrainContext(this, skillId)
    val boostAmount = context.calculateBoost()
    val cap = skillId.maximumLevel + boostAmount
    boostLevel(skillId, boostAmount, cap)
}

fun SkillId.Boost(flat: Int = 0, percent: Int = 0) =
    BoostBy(this) { (percent percentOf maximumLevel) + flat }

fun Skills.Boost(skillId: SkillId, flat: Int = 0, percent: Int = 0) =
    with(skillId.Boost(flat, percent)) { applyEffect() }

fun Skills.Restore(skillId: SkillId, flat: Int = 0, percent: Int = 0) =
    with(skillId.Restore(flat, percent)) { applyEffect() }

fun SkillId.DrainBy(calculateDrain: BoostDrainContext.() -> Int) = SkillEffect {
    val context = BoostDrainContext(this, this@DrainBy)
    val boostAmount = context.calculateDrain()
    drainLevel(this@DrainBy, boostAmount, 1)
}

fun SkillId.Drain(flat: Int = 0, percent: Int = 0) = DrainBy {
    (percent percentOf maximumLevel) + flat
}

fun Skills.Drain(skillId: SkillId, flat: Int = 0, percent: Int = 0) = with(skillId.Drain(flat, percent)) {
    applyEffect()
}

fun List<SkillId>.restoreAll(flat: Int = 0, percent: Int = 0) = SkillEffect {
    forEach { skill -> Restore(skill, flat, percent) }
}
fun List<SkillId>.boostAll(flat: Int = 0, percent: Int = 0) = SkillEffect {
    forEach { skill -> Boost(skill, flat, percent) }
}
fun List<SkillId>.drainAll(flat: Int = 0, percent: Int = 0) = SkillEffect {
    forEach { skill -> Drain(skill, flat, percent) }
}

fun Tier1Stat(skillId: SkillId) = skillId.Boost(flat = 3, percent = 10)
fun Tier2Stat(skillId: SkillId) = skillId.Boost(flat = 5, percent = 15)
fun Tier3Stat(skillId: SkillId) = skillId.Boost(flat = 5, percent = 22)

val RestoreStats = listOf(SkillId.Attack, SkillId.Defence, SkillId.Strength, SkillId.Range, SkillId.Magic)
val SuperRestoreStats = SkillId.entries.filter { it != SkillId.Hitpoints }
fun HealBy(amount: Int, overheal: Int = 0): SkillEffect = SkillEffect {
    val maxAfterOverheal = maxHitpoints + overheal
    heal(amount, maxHp = maxAfterOverheal)
}

fun HurtBy(amount: Int) = SkillEffect {
    hit(amount)
}

fun Skills.Hurt(amount: Int) = with(HurtBy(amount)) { applyEffect() }

fun WeakenDisease() = SkillEffect { /*TODO: Weaken disease*/ }
fun WeakenPoison(byAmount: Int) = SkillEffect { /*TODO: Weaken poison*/ }
fun CureDisease(durationTicks: Int = 0) = SkillEffect { /*TODO: Cure Disease*/ }
fun CurePoison(durationTicks: Int = 0) = SkillEffect { /*TODO: Cure Poison and provide immunity*/ }
fun DragonfireResist(durationTicks: Int) = SkillEffect { /*TODO: Provide Dragonfire Resist*/ }
fun DragonfireImmune(durationTicks: Int) = SkillEffect { /*TODO: Provide dragonfire immunity*/ }
fun RestoreEnergy(flat: Int = 0, percent: Int = 0) = SkillEffect { /*TODO: Restore energy*/}
fun RestorePotion() = RestoreStats.restoreAll(flat = 10, percent = 30)
fun SuperRestore() = SuperRestoreStats.restoreAll(flat = 8, percent = 25)

fun ZamorakBrew() = SkillEffect {
    Hurt((10 percentOf hitPoints) + 20)
    Boost(SkillId.Strength, flat = 2, percent = 12)
    Boost(SkillId.Attack, flat = 2, percent = 20)
    SkillId.Prayer.Restore(flat = 9)
    Drain(SkillId.Defence, flat = 2, percent = 10)
}

fun SaradominBrew() = SkillEffect {
    val healAmount = 15 percentOf maxHitpoints
    heal(healAmount, maxHitpoints + healAmount)
    Boost(SkillId.Defence, percent = 25)
    Drain(SkillId.Strength, percent = 10)
    Drain(SkillId.Range, percent = 10)
    Drain(SkillId.Attack, percent = 10)
    Drain(SkillId.Magic, percent = 10)
}

fun RecoverSpecial(percent: Int) = SkillEffect { /*TODO: Recover special energy*/ }

fun SendOverload() = SkillEffect {
    var ouchCount = 5
    if (hitPoints <= 100 * ouchCount) return@SkillEffect player.sendMessage("Too weak!! Will Die!! STOP!")
    player.sendMessage("You drink some of the foul liquid.")
    player.setAttribute("overloads", true)
    World.getWorld().submitTickable(2) {
        player.animate(3170)
        player.hit(100)
        ouchCount--
        if (ouchCount == 0) stop()
    }
}
