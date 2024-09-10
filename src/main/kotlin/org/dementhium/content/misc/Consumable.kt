package org.dementhium.content.misc

import org.dementhium.content.misc.skillEffect.SkillEffect
import kotlin.reflect.KClass

sealed class Consumable(
    val consumableIds: ConsumableStages,
    val skillEffect: SkillEffect
) {
    companion object {
        val consumables: MutableMap<KClass<out Consumable>, MutableSet<in Consumable>> = mutableMapOf()

        inline fun <reified T: Consumable> getConsumablesBy(): MutableSet<in T> = consumables.getOrPut(T::class) {
            mutableSetOf()
        }

        inline fun <reified T: Consumable> addConsumable(consumable: T) {
            val foundConsumables = getConsumablesBy<T>()
            foundConsumables.add(consumable)
        }
    }
}
