package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.skillEffect.RestoreEnergy
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val SpringSqirkJuice = Ale(ItemId.SPRING_SQIRKJUICE) {
    Thieving.boost(flat = 1)
    addEffect(RestoreEnergy(percent = 20))
}
val SummerSqirkJuice = Ale(ItemId.SUMMER_SQIRKJUICE) {
    Thieving.boost(flat = 3)
    addEffect(RestoreEnergy(percent = 40))
}
val AutumnSqirkJuice = Ale(ItemId.AUTUMN_SQIRKJUICE) {
    Thieving.boost(flat = 2)
    addEffect(RestoreEnergy(percent = 30))
}
val WinterSqirkJuice = Ale(ItemId.WINTER_SQIRKJUICE) {
    addEffect(RestoreEnergy(percent = 10))
}
