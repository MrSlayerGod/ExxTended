package org.dementhium.content.misc.eating

import org.dementhium.content.misc.DepletionType
import org.dementhium.content.misc.buildConsumable
import org.dementhium.content.misc.skillEffect.RestoreEnergy
import org.dementhium.model.Item
import org.dementhium.util.ItemId

val Cake = Food(
    listOf(ItemId.SLICE_OF_CAKE, ItemId.`2_3_CAKE`, ItemId.CAKE)
) {
    heal(40)
}

val ChocolateCake = Food(
    listOf(ItemId.CHOCOLATE_SLICE, ItemId.`2_3_CHOCOLATE_CAKE`, ItemId.CHOCOLATE_CAKE)
) {
    heal(50)
}

val CookedFishcake = Single(ItemId.COOKED_FISHCAKE) {
    heal(110)
}

val MintCake = Single(ItemId.MINT_CAKE) {
    addEffect(RestoreEnergy(percent = 100))
}

val DwarvenRockCake = buildConsumable<Food> {
    consumableId(ItemId.DWARVEN_ROCK_CAKE)
    depletionType(DepletionType.Keep)
    addEffect {
        hurt(30)
    }
}


