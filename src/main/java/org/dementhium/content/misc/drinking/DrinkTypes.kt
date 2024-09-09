package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.SkillEffect
import org.dementhium.content.misc.plus
import org.dementhium.util.ItemId

sealed class SingleDose(
    fullId: Int,
    skillEffect: SkillEffect,
    emptyId: Int
): Drink(Doses(fullId), skillEffect, emptyId)

sealed class Ale(aleId: Int, skillEffect: SkillEffect): SingleDose(aleId, skillEffect, emptyId = ItemId.BEER_GLASS)

sealed class Keg(oneDoseKegId: Int, ale: Ale): Drink(
    Doses(oneDoseKegId, oneDoseKegId + 2, oneDoseKegId + 4, oneDoseKegId + 6),
    skillEffect = ale.skillEffect,
    emptyId = ItemId.CALQUAT_KEG
)

sealed class BarbarianMix(doses: Doses, skillEffect: SkillEffect, heal: Int): Drink(
    doses, skillEffect + HealBy(heal),
)

sealed class CastleWarsPotion(
    oneDoseId: Int,
    mimics: Drink
): Drink(Doses(oneDoseId, oneDoseId - 1, oneDoseId - 2, oneDoseId - 3), mimics.skillEffect)

sealed class DungeoneeringPotion(potionId: Int, skillEffect: SkillEffect): SingleDose(potionId, skillEffect, emptyId = ItemId.VIAL_17490)

sealed class GnomeCocktail(
    cocktailId: Int,
    skillEffect: SkillEffect
): SingleDose(cocktailId, skillEffect, ItemId.COCKTAIL_GLASS)
