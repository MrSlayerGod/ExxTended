package org.dementhium.util.random

fun <T> List<Pair<Int, List<T>>>.weightedTable(): SimpleWeightedTable<T> {
    val weightedItemList = mutableListOf<Pair<Int, T>>()
    forEach { (weight, items) ->
        items.forEach { item ->
            weightedItemList.add(weight to item)
        }
    }
    return SimpleWeightedTable(weightedItemList)
}

class SimpleWeightedTable<T>(items: List<Pair<Int, T>>) {

    constructor(vararg items: Pair<Int, T>): this(items.asList())

    private val combiSorted = buildMap<Int, MutableList<T>> {
        items.forEach { (weight, item) ->
            getOrPut(weight) { mutableListOf() }.add(item)
        }
    }.toSortedMap()
    private val totalWeight = combiSorted.keys.sum()
    private val weightRange = 1 .. totalWeight

    private fun List<T>.pickItem() = random()

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
