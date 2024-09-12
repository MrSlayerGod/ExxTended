package org.dementhium.content.misc.eating

import org.dementhium.content.misc.Consumable
import org.dementhium.content.misc.DepletionType
import org.dementhium.content.misc.buildConsumable
import org.dementhium.content.misc.skillEffect.SkillEffect
import org.dementhium.content.misc.skillEffect.builder.SkillEffectBuilder
import org.dementhium.content.misc.skillEffect.buildSkillEffect
import org.dementhium.util.ItemId

fun Food(
    foodIds: List<Int>,
    skillEffect: SkillEffect,
    depletionType: DepletionType = DepletionType.Remove,
) = buildConsumable<Food> {
    setConsumableIds(foodIds)
    depletionType(depletionType)
    addEffect(skillEffect)
}

fun Single(
    foodId: Int,
    skillEffect: SkillEffect,
    depletionType: DepletionType = DepletionType.Remove
) = Food(listOf(foodId), skillEffect, depletionType)

fun Food(
    foodIds: List<Int>,
    depletionType: DepletionType = DepletionType.Remove,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Food(foodIds, buildSkillEffect(skillEffect), depletionType)

fun Single(
    foodId: Int,
    depletionType: DepletionType = DepletionType.Remove,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Single(foodId, buildSkillEffect(skillEffect), depletionType)

fun Single(
    foodId: Int,
    mimics: Consumable
) = Single(foodId, mimics.skillEffect)

fun Pie(
    oneDose: Int, twoDose: Int,
    skillEffect: SkillEffect
) = Food(
    listOf(oneDose, twoDose),
    skillEffect,
    depletionType = DepletionType.Empty(ItemId.PIE_DISH)
)
fun Pie(
    oneDose: Int, twoDose: Int,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Pie(oneDose, twoDose, buildSkillEffect(skillEffect))

fun Pizza(
    oneDose: Int, twoDose: Int,
    skillEffect: SkillEffect
) = Food(listOf(oneDose, twoDose), skillEffect, DepletionType.Remove)

fun Pizza(
    oneDose: Int, twoDose: Int,
    skillEffect: SkillEffectBuilder.() -> Unit
) = Pizza(oneDose, twoDose, buildSkillEffect(skillEffect))
