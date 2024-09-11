package org.dementhium.content.misc

import org.dementhium.content.misc.skillEffect.SkillEffect
import org.dementhium.content.misc.skillEffect.SkillEffectBuilder
import org.dementhium.util.builder.GenericBuilder
import org.dementhium.util.findConstructorThatMatches
import kotlin.reflect.*

/**
 * Make sure you're adding ids in order from the lowest dosage to the highest dosage
 *  e.g. add(SaraBrew(1)), add(SaraBrew(2)), etc.
 */
class ConsumableBuilder<T: Consumable>(
    val consumableClass: KClass<T>
): GenericBuilder<T>("ConsumableBuilder") {

    var consumableIds: MutableList<Int> = mutableListOf()

    var depletionType: DepletionType = DepletionType.Remove

    private val skillEffects: MutableList<SkillEffect> = mutableListOf()

    fun setConsumableIds(ids: List<Int>) = apply {
        consumableIds = ids.toMutableList()
    }

    fun setConsumableIds(vararg ids: Int) = apply {
        consumableIds = ids.toMutableList()
    }

    fun addConsumableIds(vararg ids: Int) = apply {
        ids.forEach(consumableIds::add)
    }

    fun addEffect(effect: SkillEffect) = apply {
        skillEffects.add(effect)
    }

    fun addEffect(builder: SkillEffectBuilder.() -> Unit) = apply {
        val built = build<SkillEffect, SkillEffectBuilder>(builder)
        addEffect(built)
    }

    fun addEffects(effects: List<SkillEffect>) = apply {
        effects.forEach(::addEffect)
    }

    fun depletionType(type: DepletionType) = apply {
        depletionType = type
    }

    override fun build(): T {
        val consumableStages = ConsumableIds(consumableIds, depletionType)
        val skillEffect = SkillEffect.from(skillEffects)
        skillEffects.clear()
        val constructor: KFunction<T> = findConstructorThatMatches(
            consumableClass,
            listOf(typeOf<ConsumableStages>(), typeOf<SkillEffect>())
        )
        return constructor.call(consumableStages, skillEffect)
    }
}
