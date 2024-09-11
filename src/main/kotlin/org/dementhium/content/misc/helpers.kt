package org.dementhium.content.misc

import org.dementhium.util.builder.builder
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

private val consumableClassParam: KParameter =
    ConsumableBuilder::class.constructors.first().parameters[0]

fun <T: Consumable> buildConsumable(consumableClass: KClass<T>, block: ConsumableBuilder<T>.() -> Unit): T {
    val args = mapOf(consumableClassParam to consumableClass)
    return builder<T, ConsumableBuilder<T>>(args, block)
}

inline fun <reified T: Consumable> buildConsumable(
    noinline block: ConsumableBuilder<T>.() -> Unit
): T =
    buildConsumable(T::class, block).also { Consumable.addConsumable(it) }
