package org.dementhium.util.random

import org.dementhium.util.map.reverse
import java.util.*

private fun <K: Comparable<K>, V> List<Pair<K, V>>.combiSort(): SortedMap<K, List<V>> {
    val combi = mutableMapOf<K, MutableList<V>>()
    forEach { (weight, item) ->
        combi.getOrPut(weight) { mutableListOf() }.add(item)
    }
    return combi.toSortedMap()
}

class SimpleWeightedTable<T>(items: List<Pair<Int, T>>) {
    constructor(vararg items: Pair<Int, T>): this(items.asList())
    constructor(items: Map<T, Int>): this(items.reverse().map(Map.Entry<Int, T>::toPair))

    private val combiSorted = items.combiSort()
    private val totalWeight = combiSorted.keys.sum()
    private val weightRange = 1 .. totalWeight

    private fun List<T>.pickItem() = if (size > 1) random() else first()

    fun weightOf(item: T): Int {
        combiSorted.forEach { (key, value) ->
            if (value == item) return key
        }
        return -1
    }

    fun chanceOf(item: T): Double {
        val weight = weightOf(item)
        val chance = weight.toDouble() / totalWeight
        return if (chance > 0) chance else 0.0
    }

    fun roll(modifier: Double = 1.0): T {
        var pickedWeight = weightRange.random() * modifier
        combiSorted.keys.forEach { weight ->
            pickedWeight -= weight
            if(pickedWeight <= 0) {
                val items = combiSorted[weight]!!
                return items.pickItem()
            }
        }
        return combiSorted[combiSorted.lastKey()]!!.pickItem()
    }
}