package org.dementhium.util.random

import java.util.*

private fun <K: Comparable<K>, V> List<Pair<K, V>>.combiSort(): SortedMap<K, List<V>> {
    val combi = mutableMapOf<K, MutableList<V>>()
    forEach { (weight, item) ->
        combi.getOrPut(weight) { mutableListOf() }.add(item)
    }
    return combi.toSortedMap()
}

class SimpleWeightedTable<T>(
    vararg items: Pair<Int, T>
) {
    private val combiSorted = items.asList().combiSort()
    private val totalWeight = combiSorted.keys.sum()
    private val weightRange = 1 .. totalWeight

    private fun List<T>.pickItem() = if (size > 1) random() else first()

    fun roll(modifier: Double = 1.0): T {
        var pickedWeight = weightRange.random()
        combiSorted.keys.forEachIndexed { index, weight ->
            pickedWeight -= weight
            if(pickedWeight <= 0) {
                val items = combiSorted[weight]!!
                return items.pickItem()
            }
        }
        return combiSorted[combiSorted.lastKey()]!!.pickItem()
    }
}