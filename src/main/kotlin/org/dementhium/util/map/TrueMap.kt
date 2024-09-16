package org.dementhium.util.map

import kotlin.enums.EnumEntries

/**
 * This is for enums, you will always get something back because we know it's there
 *
 * @author: nbness2
 */
class TrueMap<E: Enum<E>, V>(values: EnumEntries<E>, initializer: (E) -> V): Iterable<V> {

    private val internalMap = buildMap {
        values.forEach { enumValue ->
            val initialized = initializer(enumValue)
            put(enumValue, initialized)
        }
    }

    operator fun get(key: E): V = internalMap[key]!!

    override fun iterator(): Iterator<V> = internalMap.keys.sorted().map { internalMap[it]!! }.iterator()
}
