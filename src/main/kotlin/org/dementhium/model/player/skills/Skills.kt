package org.dementhium.model.player.skills

import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.model.player.deathTickable
import org.dementhium.net.ActionSender
import org.dementhium.util.TrueMap
import org.dementhium.util.direction
import org.dementhium.util.submitTickable
import kotlin.enums.EnumEntries
import kotlin.math.max
import kotlin.math.min

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
    val skills: EnumEntries<SkillId> by lazy { SkillId.entries }
    val maxHitpoints: Int get() = SkillId.Hitpoints.maximumLevel * 10

    private val Int.skillId: SkillId get() = SkillId.byId(this)

    var SkillId.currentLevel: Int
        get() = skillsMap[this].currentLevel
        set(value) { skillsMap[this].currentLevel = value }

    var SkillId.experience: Double
        get() = skillsMap[this].experience
        set(value) { skillsMap[this].experience = value }

    val SkillId.maximumLevel: Int
        get() = skillsMap[this].maxLevel


    private fun sendHitpoints() { ActionSender.sendConfig(player, 1240, this.hitPoints * 2) }

    private fun SkillId.sendSkillLevel() { ActionSender.sendSkillLevel(player, id) }

    fun getSkill(skillId: SkillId): Skill = skillsMap[skillId]

    fun getXp(skillId: SkillId): Double = skillId.experience

    fun hit(hitValue: Int, floor: Int) {
        val trueFloor = min(maxHitpoints, floor)
        this.hitPoints = (this.hitPoints - hitValue).coerceAtLeast(trueFloor)
        if (this.hitPoints == 0) {
            sendDead()
        }
        sendHitpoints()
    }

    fun hit(hitValue: Int) = hit(hitValue, 0)

    @JvmOverloads
    fun heal(healValue: Int, maxHp: Int = maxHitpoints) {
        if (isDead) return
        val realMaxHp = max(maxHp, hitPoints)
        this.hitPoints = (this.hitPoints + healValue).coerceAtMost(realMaxHp)
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
        ceiling: Int = skillId.maximumLevel
    ) {
        val realMax = max(ceiling, skillId.currentLevel)
        val newAmount = newCurrent.coerceIn(floor, realMax)
        skillId.currentLevel = newAmount
        skillId.sendSkillLevel()
    }

    fun boostLevel(skillId: SkillId, byAmount: Int, cap: Int = skillId.maximumLevel) {
        setCurrentLevel(skillId, skillId.currentLevel + byAmount, ceiling = cap)
    }

    fun drainLevel(skillId: SkillId, byAmount: Int, floor: Int = 1) {
        setCurrentLevel(skillId, skillId.currentLevel - byAmount, floor = floor)
    }

    val combatLevel: Int get() = combatLevelFormula.calcCombatLevel(this)

    fun setXp(skillId: SkillId, newExperience: Double) {
        if (newExperience < skillId.experience) {
            skillId.experience = newExperience
        } else {
            val delta = newExperience - skillId.experience
            addExperience(skillId, delta)
        }
        skillId.sendSkillLevel()
    }

    fun setXp(id: Int, newExperience: Double) = setXp(id.skillId, newExperience)

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

    fun getLevel(skillId: SkillId): Int = skillId.currentLevel

    /**
     * Yucky old funcs, still refactored tho
     */
    fun restorePray(amount: Int) = setCurrentLevel(SkillId.Prayer, SkillId.Prayer.currentLevel + amount)
    fun drainPray(amount: Int) = restorePray(-amount)
    fun addXp(id: Int, expToAdd: Double) = addExperience(id.skillId, expToAdd)
    fun set(id: Int, newLevel: Int) = setCurrentLevel(id.skillId, newLevel)
    fun sendSkillLevels() = SkillId.entries.forEach { it.sendSkillLevel() }
    fun setLevel(id: Int, newLevel: Int) = setCurrentLevel(id.skillId, newLevel)
    fun getLevel(id: Int): Int = getLevel(id.skillId)
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
        const val Attack: Int = 0
        const val Defence: Int = 1
        const val Strength: Int = 2
        const val Hitpoints: Int = 3
        const val Range: Int = 4
        const val Prayer: Int = 5
        const val Magic: Int = 6
        const val Cooking: Int = 7
        const val Woodcutting: Int = 8
        const val Fletching: Int = 9
        const val Fishing: Int = 10
        const val Firemaking: Int = 11
        const val Crafting: Int = 12
        const val Smithing: Int = 13
        const val Mining: Int = 14
        const val Herblore: Int = 15
        const val Agility: Int = 16
        const val Thieving: Int = 17
        const val Slayer: Int = 18
        const val Farming: Int = 19
        const val Runecrafting: Int = 20
        const val Construction: Int = 21
        const val Hunter: Int = 22
        const val Summoning: Int = 23
        const val Dungeoneering: Int = 24
    }
}