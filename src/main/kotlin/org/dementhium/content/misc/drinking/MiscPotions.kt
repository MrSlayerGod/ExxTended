package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.skillEffect.Boost
import org.dementhium.content.misc.skillEffect.DragonfireResist
import org.dementhium.content.misc.skillEffect.RecoverSpecial
import org.dementhium.content.misc.skillEffect.WeakenDisease
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId
import org.dementhium.util.minutesToTicks


val MagicEssence = Vial(
    listOf(ItemId.MAGIC_ESSENCE_1, ItemId.MAGIC_ESSENCE_2, ItemId.MAGIC_ESSENCE_3, ItemId.MAGIC_ESSENCE_4),
    Magic.Boost(flat = 3)
)

val AntifirePotion = Vial(
    listOf(ItemId.ANTIFIRE_1, ItemId.ANTIFIRE_2, ItemId.ANTIFIRE_3, ItemId.ANTIFIRE_4),
    DragonfireResist(minutesToTicks(6))
)


val RelicymBalm = Vial(
    listOf(ItemId.RELICYMS_BALM_1, ItemId.RELICYMS_BALM_2, ItemId.RELICYMS_BALM_3, ItemId.RELICYMS_BALM_4),
    WeakenDisease()
)

val SpecialRestore = Vial(
    listOf(ItemId.RECOVER_SPECIAL_1, ItemId.RECOVER_SPECIAL_2, ItemId.RECOVER_SPECIAL_3, ItemId.RECOVER_SPECIAL_4),
    RecoverSpecial(25)
)
