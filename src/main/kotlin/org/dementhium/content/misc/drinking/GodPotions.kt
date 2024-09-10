package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.util.ItemId


data object GuthixRest: Tea(
    listOf(ItemId.GUTHIX_REST_1, ItemId.GUTHIX_REST_2, ItemId.GUTHIX_REST_3, ItemId.GUTHIX_REST_4),
    RestoreEnergy(percent = 5) + WeakenPoison(10) + HealBy(50, overheal = 50)
)

data object ZamorakBrew: Vial(
    listOf(ItemId.ZAMORAK_BREW_1, ItemId.ZAMORAK_BREW_2, ItemId.ZAMORAK_BREW_3, ItemId.ZAMORAK_BREW_4),
    ZamorakBrew()
)

data object SaradominBrew: Vial(
    listOf(ItemId.SARADOMIN_BREW_1, ItemId.SARADOMIN_BREW_2, ItemId.SARADOMIN_BREW_3, ItemId.SARADOMIN_BREW_4),
    SaradominBrew()
)
