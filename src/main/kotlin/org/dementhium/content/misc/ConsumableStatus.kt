package org.dementhium.content.misc

import org.dementhium.model.Item

sealed interface DepletionType {
    val item: Item
    @JvmInline value class Keep(override val item: Item): DepletionType
    data object Remove: DepletionType {
        override val item: Item = Item(-1)
    }
    @JvmInline
    value class Empty(override val item: Item): DepletionType
    companion object {
        fun Empty(id: Int) = Empty(Item(id))
    }
}

sealed interface ConsumableStatus {
    data class ConsumableNotDepleted(val item: Item) : ConsumableStatus
    @JvmInline
    value class ConsumableDepleted(val depletionType: DepletionType) : ConsumableStatus
    data object IdNotContained : ConsumableStatus
}

interface ConsumableStages {
    fun fullDose(): Item
    fun containsStage(id: Int): Boolean
    fun nextStageFromId(id: Int): ConsumableStatus
    operator fun contains(id: Int) = containsStage(id)
}

fun ConsumableIds(vararg ids: Int, depletionType: DepletionType): ConsumableStages = if (ids.size > 1) {
    ConsumableStagesImpl(ids.toList(), depletionType)
} else {
    SingleConsumableStage(ids.first(), depletionType)
}

fun ConsumableIds(ids: List<Int>, depletionType: DepletionType): ConsumableStages = if (ids.size == 1) {
    SingleConsumableStage(ids.first(), depletionType)
} else {
    ConsumableStagesImpl(ids, depletionType)
}

private data class ConsumableStagesImpl(val ids: List<Int>, val depletionType: DepletionType): ConsumableStages {

    override fun fullDose(): Item = Item(ids[0])

    override fun containsStage(id: Int): Boolean = id in ids

    override fun nextStageFromId(id: Int): ConsumableStatus {
        if (!containsStage(id)) return ConsumableStatus.IdNotContained
        val nextStageIndex = ids.indexOf(id) - 1
        val nextStageId = ids.getOrNull(nextStageIndex) ?: return ConsumableStatus.ConsumableDepleted(depletionType)
        return ConsumableStatus.ConsumableNotDepleted(Item(nextStageId))
    }

}

private class SingleConsumableStage(val id: Int, val depletionType: DepletionType): ConsumableStages {

    override fun fullDose(): Item = Item(id)

    override fun containsStage(id: Int): Boolean = id == this.id

    override fun nextStageFromId(id: Int): ConsumableStatus {
        if (id != this.id) return ConsumableStatus.IdNotContained
        return ConsumableStatus.ConsumableDepleted(depletionType)
    }
}
