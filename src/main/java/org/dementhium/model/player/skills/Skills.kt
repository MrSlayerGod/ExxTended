package org.dementhium.model.player.skills

import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.model.player.deathTickable
import org.dementhium.net.ActionSender
import org.dementhium.util.TrueMap
import org.dementhium.util.direction
import org.dementhium.util.submitTickable
import kotlin.math.max

/**
 * Goal is API compatible with old skills
 */
class Skills(val player: Player) {

    val expFormula: ExperienceFormula = ExperienceFormula.Default
    private val combatLevelFormula: CombatLevelFormula = CombatLevelFormula.Default

    var hitPoints: Int = 100
    val isDead: Boolean get() = this.hitPoints <= 0
    var hasSentDead: Boolean = false
    private val skillsMap: TrueMap<SkillId, Skill> = TrueMap(SkillId.entries) { Skill(this, it) }
    val maxHitpoints: Int get() = SkillId.Hitpoints.maximumLevel * 10

    private val Int.skillId: SkillId get() = SkillId.entries[this]

    private var SkillId.currentLevel: Int
        get() = skillsMap[this].currentLevel
        set(value) { skillsMap[this].currentLevel = value }

    private var SkillId.experience: Double
        get() = skillsMap[this].experience
        set(value) { skillsMap[this].experience = value }

    private val SkillId.maximumLevel: Int
        get() = skillsMap[this].maxLevel


    private fun sendHitpoints() { ActionSender.sendConfig(player, 1240, this.hitPoints * 2) }

    private fun SkillId.sendSkillLevel() { ActionSender.sendSkillLevel(player, id) }

    fun getSkill(skillId: SkillId) = skillsMap[skillId]

    fun hit(hitValue: Int) {
        this.hitPoints = (this.hitPoints - hitValue).coerceAtLeast(0)
        if (this.hitPoints == 0) {
            sendDead()
        }
        sendHitpoints()
    }

    @JvmOverloads
    fun heal(healValue: Int, maxHp: Int = if (this.hitPoints > maxHitpoints) maxHitpoints + 100 else maxHitpoints) {
        if (isDead) return
        this.hitPoints = (this.hitPoints + healValue).coerceAtMost(maxHp)
        sendHitpoints()
    }

    private val DontNormalize = arrayOf(SkillId.Hitpoints, SkillId.Prayer, SkillId.Summoning)

    fun normalizeLevel(
        skillId: SkillId,
        byAmount: Int = 1,
        direction: () -> Boolean
    ) {
        if (skillId in DontNormalize) return
        val directioned = direction(direction())
        setCurrentLevel(skillId, skillId.currentLevel + (byAmount * directioned))
    }

    fun setCurrentLevel(
        skillId: SkillId,
        newCurrent: Int,
        floor: Int = 0,
        ceiling: Int = max(skillId.maximumLevel, skillId.currentLevel)
    ) {
        val newAmount = newCurrent.coerceIn(floor, ceiling)
        skillId.currentLevel = newAmount
        skillId.sendSkillLevel()
    }

    val combatLevel: Int get() = combatLevelFormula.calcCombatLevel(this)

    fun setXp(id: Int, newExperience: Double) {
        val skillId = id.skillId
        skillId.experience = newExperience
        skillId.sendSkillLevel()
    }

    fun addExperience(skillId: SkillId, expToAdd: Double) {
        val oldMax = skillId.maximumLevel
        val newExperience = (skillId.experience + expToAdd).coerceIn(0.0, MAXIMUM_EXP)
        skillId.experience = newExperience
        val newMax = skillId.maximumLevel
        if (newMax > oldMax) {
            val delta = newMax - oldMax
            if (skillId.currentLevel < skillId.maximumLevel) {
                skillId.currentLevel = (skillId.currentLevel + delta).coerceAtMost(skillId.maximumLevel)
            }
            if (skillId == SkillId.Hitpoints) {
                heal(10 * delta)
            }
        }
        skillId.sendSkillLevel()
    }

    fun increaseLevelToMaximum(id: Int, modification: Int) {
        val skillId = id.skillId
        setCurrentLevel(skillId, skillId.currentLevel + modification)
    }

    fun increaseLevelToMaximumModification(id: Int, modification: Int) {
        val skillId = id.skillId
        setCurrentLevel(skillId, skillId.currentLevel + modification, ceiling = skillId.maximumLevel + modification)
    }

    fun decreaseLevelToZero(id: Int, modification: Int) {
        val skillId = id.skillId
        setCurrentLevel(skillId, skillId.currentLevel - modification, floor = 0)
    }

    fun decreaseLevelOnce(id: Int, amount: Int) {
        val skillId = id.skillId
        setCurrentLevel(skillId, skillId.currentLevel - amount, floor = skillId.maximumLevel - amount)
    }

    /**
     * Yucky old funcs, still refactored tho
     */
    fun restorePray(amount: Int) = setCurrentLevel(SkillId.Prayer, SkillId.Prayer.currentLevel + amount)
    fun drainPray(amount: Int) = restorePray(-amount)
    fun addXp(id: Int, expToAdd: Double) = addExperience(id.skillId, expToAdd)
    fun set(id: Int, newLevel: Int) = setCurrentLevel(id.skillId, newLevel)
    fun sendSkillLevels() = SkillId.entries.forEach { it.sendSkillLevel() }
    fun setLevel(id: Int, newLevel: Int) = setCurrentLevel(id.skillId, newLevel)
    fun getLevel(id: Int): Int = id.skillId.currentLevel
    fun getXp(id: Int): Double = id.skillId.experience
    fun getXPForLevel(level: Int) = expFormula.calcExpForLevel(level).toInt()
    fun getLevelForXp(id: Int) = id.skillId.maximumLevel
    fun setLevelAndXP(id: Int, level: Int, xp: Double) {
        val skillId = id.skillId
        skillId.experience = xp
        skillId.currentLevel = level
    }

    fun refresh() {
        sendSkillLevels()
        sendHitpoints()
        player.mask.isApperanceUpdate = true

    }

    fun sendDead() {
        if (hasSentDead) return
        hasSentDead = true
        player.walkingQueue.reset()
        player.combatState.reset()
        val world = World.getWorld()
        world.submitTickable(1) {
            player.animate(9055)
            world.submit(deathTickable(player))
        }
    }

    fun Reset() {
        skillsMap.forEach(Skill::reset)
        refresh()
    }

    companion object {
        const val MAXIMUM_EXP: Double = 200_000_000.0;
        @JvmField val SKILL_COUNT: Int = SkillId.entries.size
        @JvmField val SKILL_NAME: Array<String> = SkillId.entries.map { it.name }.toTypedArray();
        const val ATTACK: Int = 0
        const val DEFENCE: Int = 1
        const val STRENGTH: Int = 2
        const val HITPOINTS: Int = 3
        const val RANGE: Int = 4
        const val PRAYER: Int = 5
        const val MAGIC: Int = 6
        const val COOKING: Int = 7
        const val WOODCUTTING: Int = 8
        const val FLETCHING: Int = 9
        const val FISHING: Int = 10
        const val FIREMAKING: Int = 11
        const val CRAFTING: Int = 12
        const val SMITHING: Int = 13
        const val MINING: Int = 14
        const val HERBLORE: Int = 15
        const val AGILITY: Int = 16
        const val THIEVING: Int = 17
        const val SLAYER: Int = 18
        const val FARMING: Int = 19
        const val RUNECRAFTING: Int = 20
        const val CONSTRUCTION: Int = 21
        const val HUNTER: Int = 22
        const val SUMMONING: Int = 23
        const val DUNGEONEERING: Int = 24
    }
}