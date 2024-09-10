package org.dementhium.content.misc

sealed interface DepletionType {
    data object Keep: DepletionType
    data object Remove: DepletionType
    @JvmInline
    value class EmptyId(val id: Int): DepletionType
}

sealed interface ConsumableStatus {
    data class ConsumableNotDepleted(val id: Int) : ConsumableStatus
    @JvmInline
    value class ConsumableDepleted(val depletionType: DepletionType) : ConsumableStatus
    data object IdNotContained : ConsumableStatus
}

interface ConsumableStages {
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

    override fun containsStage(id: Int): Boolean = id in ids

    override fun nextStageFromId(id: Int): ConsumableStatus {
        if (!containsStage(id)) return ConsumableStatus.IdNotContained
        val nextStageIndex = ids.indexOf(id) - 1
        val nextStageId = ids.getOrNull(nextStageIndex) ?: return ConsumableStatus.ConsumableDepleted(depletionType)
        return ConsumableStatus.ConsumableNotDepleted(nextStageId)
    }

}

private class SingleConsumableStage(val id: Int, val depletionType: DepletionType): ConsumableStages {

    override fun containsStage(id: Int): Boolean = id == this.id

    override fun nextStageFromId(id: Int): ConsumableStatus {
        if (id != this.id) return ConsumableStatus.IdNotContained
        return ConsumableStatus.ConsumableDepleted(depletionType)
    }
}
