package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.content.misc.ConsumableIds
import org.dementhium.content.misc.skillEffect.SkillEffect
import org.dementhium.content.misc.skillEffect.plus
import org.dementhium.util.ItemId

fun Tea(
    ids: List<Int>,
    skillEffect: SkillEffect
) = Drink(
    consumableIds = ConsumableIds(
        ids,
        DepletionType.Empty(ItemId.EMPTY_CUP)
    ),
    skillEffect = skillEffect
)

fun Vial(
    ids: List<Int>,
    skillEffect: SkillEffect
) = Drink(
    consumableIds = ConsumableIds(
        ids,
        DepletionType.Empty(ItemId.VIAL)
    ),
    skillEffect = skillEffect
)

fun SingleDose(
    fullId: Int,
    skillEffect: SkillEffect,
    emptyId: Int
) = Drink(
    consumableIds = ConsumableIds(
        fullId,
        depletionType = DepletionType.Empty(emptyId)
    ),
    skillEffect = skillEffect
)

fun Ale(
    aleId: Int,
    skillEffect: SkillEffect
) = SingleDose(
    fullId = aleId,
    skillEffect = skillEffect,
    emptyId = ItemId.BEER_GLASS
)

fun Keg(
    oneDoseKegId: Int,
    mimics: Drink
) = Drink(
    consumableIds = ConsumableIds(
        oneDoseKegId, oneDoseKegId + 2, oneDoseKegId + 4, oneDoseKegId + 6,
        depletionType = DepletionType.Empty(ItemId.CALQUAT_KEG)
    ),
    skillEffect = mimics.skillEffect
)

fun BarbarianMix(
    singleDoseId: Int,
    skillEffect: SkillEffect,
    heal: Int
) = Vial(
    ids = listOf(singleDoseId, singleDoseId - 2),
    skillEffect = skillEffect + HealBy(heal),
)

fun CastleWarsPotion(
    oneDoseId: Int,
    mimics: Drink
) = Drink(
    consumableIds = ConsumableIds(
        oneDoseId, oneDoseId - 1, oneDoseId - 2, oneDoseId - 3,
        depletionType = DepletionType.Remove
    ),
    skillEffect = mimics.skillEffect
)

fun DungeoneeringPotion(
    potionId: Int,
    skillEffect: SkillEffect
) = SingleDose(
    fullId = potionId,
    skillEffect = skillEffect,
    emptyId = ItemId.VIAL_17490
)

fun GnomeCocktail(
    cocktailId: Int,
    skillEffect: SkillEffect
) = SingleDose(
    fullId = cocktailId,
    skillEffect = skillEffect,
    emptyId = ItemId.COCKTAIL_GLASS
)
