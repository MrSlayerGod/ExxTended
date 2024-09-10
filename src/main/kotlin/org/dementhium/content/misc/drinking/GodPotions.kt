package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.plus
import org.dementhium.util.ItemId


data object GuthixRest : Drink(
    Doses(ItemId.GUTHIX_REST_1, ItemId.GUTHIX_REST_2, ItemId.GUTHIX_REST_3, ItemId.GUTHIX_REST_4),
    RestoreEnergy(percent = 5) + WeakenPoison(10) + HealBy(50, overheal = 50),
    emptyId = ItemId.EMPTY_CUP
)

data object ZAMORAK_BREW : Drink(
    Doses(ItemId.ZAMORAK_BREW_1, ItemId.ZAMORAK_BREW_2, ItemId.ZAMORAK_BREW_3, ItemId.ZAMORAK_BREW_4),
    ZamorakBrew()
)

data object SARADOMIN_BREW : Drink(
    Doses(ItemId.SARADOMIN_BREW_1, ItemId.SARADOMIN_BREW_2, ItemId.SARADOMIN_BREW_3, ItemId.SARADOMIN_BREW_4),
    SaradominBrew()
)
