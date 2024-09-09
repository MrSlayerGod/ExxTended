package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.plus
import org.dementhium.util.ItemId
import org.dementhium.util.minutesToTicks


data object ANTIPOISON : Drink(
    Doses(ItemId.ANTIPOISON_1, ItemId.ANTIPOISON_2, ItemId.ANTIPOISON_3, ItemId.ANTIPOISON_4),
    CurePoison(minutesToTicks(3))
)

data object SuperAntipoison : Drink(
    Doses(ItemId.SUPER_ANTIPOISON_1, ItemId.SUPER_ANTIPOISON_2, ItemId.SUPER_ANTIPOISON_3, ItemId.SUPER_ANTIPOISON_4),
    CurePoison(minutesToTicks(6))
)

data object AntidodeP : Drink(
    Doses(ItemId.ANTIPOISONP_1, ItemId.ANTIPOISONP_2, ItemId.ANTIPOISONP_3, ItemId.ANTIPOISONP_4),
    CurePoison(minutesToTicks(9))
)

data object AntidodePP : Drink(
    Doses(ItemId.ANTIPOISONPP_1, ItemId.ANTIPOISONPP_2, ItemId.ANTIPOISONPP_3, ItemId.ANTIPOISONPP_4),
    CurePoison(minutesToTicks(12))
)

data object SanfewSerum : Drink(
    Doses(ItemId.SANFEW_SERUM_1, ItemId.SANFEW_SERUM_2, ItemId.SANFEW_SERUM_3, ItemId.SANFEW_SERUM_4),
    SUPER_RESTORE_POTION.skillEffect + ANTIPOISON.skillEffect + CureDisease()
)
