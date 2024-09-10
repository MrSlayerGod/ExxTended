package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.plus
import org.dementhium.model.player.skills.SkillId
import org.dementhium.util.ItemId

data object ExtremeAttack : Drink(
    Doses(ItemId.EXTREME_ATTACK_1, ItemId.EXTREME_ATTACK_2, ItemId.EXTREME_ATTACK_3, ItemId.EXTREME_ATTACK_4),
    Tier3Stat(SkillId.Attack)
)

data object ExtremeDefence : Drink(
    Doses(ItemId.EXTREME_DEFENCE_1, ItemId.EXTREME_DEFENCE_2, ItemId.EXTREME_DEFENCE_3, ItemId.EXTREME_DEFENCE_4),
    Tier3Stat(SkillId.Defence)
)

data object ExtremeStrength : Drink(
    Doses(ItemId.EXTREME_STRENGTH_1, ItemId.EXTREME_STRENGTH_2, ItemId.EXTREME_STRENGTH_3, ItemId.EXTREME_STRENGTH_4),
    Tier3Stat(SkillId.Strength)
)

data object ExtremeMagic : Drink(
    Doses(ItemId.EXTREME_MAGIC_1, ItemId.EXTREME_MAGIC_2, ItemId.EXTREME_MAGIC_3, ItemId.EXTREME_MAGIC_4),
    SkillId.Magic.Boost(flat = 7)
)

data object ExtremeRange : Drink(
    Doses(ItemId.EXTREME_RANGING_1, ItemId.EXTREME_RANGING_2, ItemId.EXTREME_RANGING_3, ItemId.EXTREME_RANGING_4),
    SkillId.Range.Boost(flat = 4, percent = 19)
)


data object OVERLOAD : Drink(
    Doses(ItemId.OVERLOAD_1, ItemId.OVERLOAD_2, ItemId.OVERLOAD_3, ItemId.OVERLOAD_4),
    ExtremeAttack.skillEffect + ExtremeDefence.skillEffect + ExtremeStrength.skillEffect + ExtremeMagic.skillEffect + ExtremeRange.skillEffect + SendOverload()
)
