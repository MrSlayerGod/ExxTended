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

inline fun ConsumableIds(vararg ids: Int, depletionType: DepletionType) = if (ids.size > 1) {
    ConsumableIds(ids.toList(), depletionType)
} else {
    ConsumableId(ids.first(), depletionType)
}

data class ConsumableIds(val doseIds: List<Int>, val depletionType: DepletionType): ConsumableStages {

    override fun containsStage(id: Int): Boolean = id in doseIds

    override fun nextStageFromId(id: Int): ConsumableStatus {
        if (!containsStage(id)) return ConsumableStatus.IdNotContained
        val nextStageIndex = doseIds.indexOf(id) + 1
        val nextStageId = doseIds.getOrNull(nextStageIndex) ?: return ConsumableStatus.ConsumableDepleted(depletionType)
        return ConsumableStatus.ConsumableNotDepleted(nextStageId)
    }

}

class ConsumableId(val doseId: Int, val depletionType: DepletionType): ConsumableStages {

    override fun containsStage(id: Int): Boolean = id == doseId

    override fun nextStageFromId(id: Int): ConsumableStatus {
        if (id != doseId) return ConsumableStatus.IdNotContained
        return ConsumableStatus.ConsumableDepleted(depletionType)
    }
}