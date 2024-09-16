package org.dementhium.content.misc.skillEffect

import org.dementhium.content.misc.skillEffect.builder.BoostDrainContext
import org.dementhium.content.misc.skillEffect.builder.HealType
import org.dementhium.model.World
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.math.percentOf
import org.dementhium.util.submitTickable
import org.dementhium.content.misc.skillEffect.builder.*

fun SkillId.Restore(flat: Int = 0, percent: Int = 0) = restore(this, flat, percent)
fun SkillId.Boost(flat: Int = 0, percent: Int = 0) = boost(this, flat, percent)

fun SkillId.DrainBy(calculateDrain: BoostDrainContext.() -> Int) = skillEffect {
    val context = BoostDrainContext(this, this@DrainBy)
    val boostAmount = context.calculateDrain()
    drainLevel(this@DrainBy, boostAmount, 1)
}

fun SkillId.Drain(flat: Int = 0, percent: Int = 0) = DrainBy {
    (percent percentOf maximumLevel) + flat
}

fun Collection<SkillId>.restoreAll(flat: Int = 0, percent: Int = 0) = buildSkillEffect {
    forEach { skill -> skill.restore(flat, percent) }
}

fun Collection<SkillId>.boostAll(flat: Int = 0, percent: Int = 0) = buildSkillEffect {
    forEach { skill -> skill.boost(flat, percent) }
}

fun Collection<SkillId>.drainAll(flat: Int = 0, percent: Int = 0) = buildSkillEffect {
    forEach { skill -> skill.drain(flat, percent) }
}

fun healBy(flat: Int, percent: Int) = buildSkillEffect {
    heal(flat, percent)
}

fun hurtBy(flat: Int, percent: Int) = buildSkillEffect {
    hurt(flat, percent)
}

fun HealBy(amount: Int, overheal: Int = 0) = buildSkillEffect {
    heal(amount, healType = if (overheal == 0) HealType.DoesntOverheal else HealType.Overheals)
}

fun WeakenDisease() = SkillEffect { /*TODO: Weaken disease*/ }
fun WeakenPoison(byAmount: Int) = SkillEffect { /*TODO: Weaken poison*/ }
fun CureDisease(durationTicks: Int = 0) = SkillEffect { /*TODO: Cure Disease*/ }
fun CurePoison(durationTicks: Int = 0) = SkillEffect { /*TODO: Cure Poison and provide immunity*/ }
fun DragonfireResist(durationTicks: Int) = SkillEffect { /*TODO: Provide Dragonfire Resist*/ }
fun DragonfireImmune(durationTicks: Int) = SkillEffect { /*TODO: Provide dragonfire immunity*/ }
fun RestoreEnergy(flat: Int = 0, percent: Int = 0) = SkillEffect { /*TODO: Restore energy*/}
fun RestorePotion() = RestoreStats.restoreAll(flat = 10, percent = 30)
fun SuperRestore() = SuperRestoreStats.restoreAll(flat = 8, percent = 25)
fun RecoverSpecial(percent: Int) = SkillEffect { /*TODO: Recover special energy*/ }

val RestoreStats = listOf(Attack, Defence, Strength, Range, Magic)
val SuperRestoreStats = SkillId.entries.filter { it != Hitpoints }

fun SendOverload() = skillEffect {
    var ouchCount = 5
    if (hitPoints <= 100 * ouchCount) return@skillEffect player.sendMessage("Too weak!! Will Die!! STOP!")
    player.sendMessage("You drink some of the foul liquid.")
    player.setAttribute("overloads", true)
    World.getWorld().submitTickable(2) {
        player.animate(3170)
        player.hit(100)
        ouchCount--
        if (ouchCount == 0) stop()
    }
}

operator fun SkillId.plus(other: SkillId): Set<SkillId> = setOf(this, other)
