package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.content.misc.skillEffect.*
import org.dementhium.content.misc.skillEffect.builder.SkillEffectBuilder
import org.dementhium.util.ItemId

fun Tea(
    ids: List<Int>,
    skillEffect: SkillEffect
) = buildConsumable<Drink> {
    setConsumableIds(ids)
    depletionType(DepletionType.Empty(ItemId.EMPTY_CUP))
    addEffect(skillEffect)
}

fun Tea(
    ids: List<Int>,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Tea(ids, buildSkillEffect(skillEffect))

fun Vial(
    ids: List<Int>,
    skillEffect: SkillEffect
) = buildConsumable<Drink> {
    setConsumableIds(ids)
    depletionType(DepletionType.Empty(ItemId.VIAL))
    addEffect(skillEffect)
}

fun Vial(
    ids: List<Int>,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Vial(ids, buildSkillEffect(skillEffect))


fun SingleDose(
    fullId: Int,
    skillEffect: SkillEffect,
    emptyId: Int
) = buildConsumable<Drink> {
    consumableId(fullId)
    depletionType(DepletionType.Empty(emptyId))
    addEffect(skillEffect)
}

fun SingleDose(
    fullId: Int,
    emptyId: Int,
    skillEffect: SkillEffectBuilder.() -> Unit
) = SingleDose(fullId, buildSkillEffect(skillEffect), emptyId)

fun Ale(
    aleId: Int,
    skillEffect: SkillEffect
) = buildConsumable<Drink> {
    consumableId(aleId)
    depletionType(DepletionType.Empty(ItemId.BEER_GLASS))
    addEffect(skillEffect)
}

fun Ale(
    aleId: Int,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Ale(aleId, buildSkillEffect(skillEffect))

fun Keg(
    oneDoseKegId: Int,
    mimics: Consumable
) = buildConsumable<Drink> {
    setConsumableIds(oneDoseKegId, oneDoseKegId + 2, oneDoseKegId + 4, oneDoseKegId + 6)
    depletionType(DepletionType.Empty(ItemId.CALQUAT_KEG))
    addEffect(mimics.skillEffect)
}

fun BarbarianMix(
    singleDoseId: Int,
    skillEffect: SkillEffect,
    heal: Int
) = Vial(
    ids = listOf(singleDoseId, singleDoseId - 2),
    skillEffect = skillEffect + HealBy(heal),
)
fun BarbarianMix(
    singleDoseId: Int,
    mimics: Consumable,
    heal: Int
) = Vial(
    ids = listOf(singleDoseId, singleDoseId - 2),
    skillEffect = mimics.skillEffect + HealBy(heal),
)

fun CastleWarsPotion(
    oneDoseId: Int,
    mimics: Drink
) = buildConsumable<Drink>{
    setConsumableIds(oneDoseId, oneDoseId - 1, oneDoseId - 2, oneDoseId - 3)
    depletionType(DepletionType.Remove)
    addEffect(mimics.skillEffect)
}

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

fun GnomeCocktail(
    cocktailId: Int,
    builder: SkillEffectBuilder.() -> Unit
) = SingleDose(
    fullId = cocktailId,
    skillEffect = buildSkillEffect(builder),
    emptyId = ItemId.COCKTAIL_GLASS
)
