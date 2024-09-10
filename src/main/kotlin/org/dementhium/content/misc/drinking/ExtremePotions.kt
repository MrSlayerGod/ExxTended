package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.*
import org.dementhium.model.player.skills.SkillId
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

data object ExtremeAttack: Vial(
    listOf(ItemId.EXTREME_ATTACK_1, ItemId.EXTREME_ATTACK_2, ItemId.EXTREME_ATTACK_3, ItemId.EXTREME_ATTACK_4),
    Tier3Stat(Attack)
)

data object ExtremeDefence: Vial(
    listOf(ItemId.EXTREME_DEFENCE_1, ItemId.EXTREME_DEFENCE_2, ItemId.EXTREME_DEFENCE_3, ItemId.EXTREME_DEFENCE_4),
    Tier3Stat(Defence)
)

data object ExtremeStrength: Vial(
    listOf(ItemId.EXTREME_STRENGTH_1, ItemId.EXTREME_STRENGTH_2, ItemId.EXTREME_STRENGTH_3, ItemId.EXTREME_STRENGTH_4),
    Tier3Stat(Strength)
)

data object ExtremeMagic: Vial(
    listOf(ItemId.EXTREME_MAGIC_1, ItemId.EXTREME_MAGIC_2, ItemId.EXTREME_MAGIC_3, ItemId.EXTREME_MAGIC_4),
    Magic.Boost(flat = 7)
)

data object ExtremeRange: Vial(
    listOf(ItemId.EXTREME_RANGING_1, ItemId.EXTREME_RANGING_2, ItemId.EXTREME_RANGING_3, ItemId.EXTREME_RANGING_4),
    Range.Boost(flat = 4, percent = 19)
)


data object Overload: Vial(
    listOf(ItemId.OVERLOAD_1, ItemId.OVERLOAD_2, ItemId.OVERLOAD_3, ItemId.OVERLOAD_4),
    ExtremeAttack.skillEffect + ExtremeDefence.skillEffect + ExtremeStrength.skillEffect + ExtremeMagic.skillEffect + ExtremeRange.skillEffect + SendOverload()
)
