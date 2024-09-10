package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.content.misc.ConsumableIds
import org.dementhium.util.ItemId

sealed class Tea(
    ids: List<Int>,
    skillEffect: SkillEffect
): Drink(
    consumableIds = ConsumableIds(
        ids,
        DepletionType.EmptyId(ItemId.EMPTY_CUP)
    ),
    skillEffect = skillEffect
)

sealed class Vial(
    ids: List<Int>,
    skillEffect: SkillEffect
): Drink(
    consumableIds = ConsumableIds(
        ids,
        DepletionType.EmptyId(ItemId.VIAL)
    ),
    skillEffect = skillEffect
)

sealed class SingleDose(
    fullId: Int,
    skillEffect: SkillEffect,
    emptyId: Int
): Drink(
    consumableIds = ConsumableIds(
        fullId,
        depletionType = DepletionType.EmptyId(emptyId)
    ),
    skillEffect = skillEffect
)

sealed class Ale(
    aleId: Int,
    skillEffect: SkillEffect
): SingleDose(
    fullId = aleId,
    skillEffect = skillEffect,
    emptyId = ItemId.BEER_GLASS
)

sealed class Keg(
    oneDoseKegId: Int,
    ale: Ale
): Drink(
    consumableIds = ConsumableIds(
        oneDoseKegId, oneDoseKegId + 2, oneDoseKegId + 4, oneDoseKegId + 6,
        depletionType = DepletionType.EmptyId(ItemId.CALQUAT_KEG)
    ),
    skillEffect = ale.skillEffect
)

sealed class BarbarianMix(
    singleDoseId: Int,
    skillEffect: SkillEffect,
    heal: Int
): Vial(
    ids = listOf(singleDoseId, singleDoseId - 2),
    skillEffect = skillEffect + HealBy(heal),
)

sealed class CastleWarsPotion(
    oneDoseId: Int,
    mimics: Drink
): Drink(
    consumableIds = ConsumableIds(
        oneDoseId, oneDoseId - 1, oneDoseId - 2, oneDoseId - 3,
        depletionType = DepletionType.Remove
    ),
    skillEffect = mimics.skillEffect
)

sealed class DungeoneeringPotion(
    potionId: Int,
    skillEffect: SkillEffect
): SingleDose(
    fullId = potionId,
    skillEffect = skillEffect,
    emptyId = ItemId.VIAL_17490
)

sealed class GnomeCocktail(
    cocktailId: Int,
    skillEffect: SkillEffect
): SingleDose(
    fullId = cocktailId,
    skillEffect = skillEffect,
    emptyId = ItemId.COCKTAIL_GLASS
)
