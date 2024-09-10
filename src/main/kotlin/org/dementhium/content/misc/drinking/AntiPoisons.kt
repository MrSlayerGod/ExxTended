package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.CureDisease
import org.dementhium.content.misc.CurePoison
import org.dementhium.content.misc.skillEffect.plus
import org.dementhium.util.ItemId
import org.dementhium.util.minutesToTicks

val AntiPoison = Vial(
    listOf(ItemId.ANTIPOISON_1, ItemId.ANTIPOISON_2, ItemId.ANTIPOISON_3, ItemId.ANTIPOISON_4),
    CurePoison(minutesToTicks(3))
)

val SuperAntipoison = Vial(
    listOf(ItemId.SUPER_ANTIPOISON_1, ItemId.SUPER_ANTIPOISON_2, ItemId.SUPER_ANTIPOISON_3, ItemId.SUPER_ANTIPOISON_4),
    CurePoison(minutesToTicks(6))
)

val AntidodeP = Vial(
    listOf(ItemId.ANTIPOISONP_1, ItemId.ANTIPOISONP_2, ItemId.ANTIPOISONP_3, ItemId.ANTIPOISONP_4),
    CurePoison(minutesToTicks(9))
)

val AntidodePP = Vial(
    listOf(ItemId.ANTIPOISONPP_1, ItemId.ANTIPOISONPP_2, ItemId.ANTIPOISONPP_3, ItemId.ANTIPOISONPP_4),
    CurePoison(minutesToTicks(12))
)

val SanfewSerum = Vial(
    listOf(ItemId.SANFEW_SERUM_1, ItemId.SANFEW_SERUM_2, ItemId.SANFEW_SERUM_3, ItemId.SANFEW_SERUM_4),
    SuperRestorePotion.skillEffect + AntiPoison.skillEffect + CureDisease()
)
