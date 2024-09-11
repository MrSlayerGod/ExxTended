package org.dementhium.content.misc.drinking

import org.dementhium.content.misc.skillEffect.*
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId


val GuthixRest = Tea(
    listOf(ItemId.GUTHIX_REST_1, ItemId.GUTHIX_REST_2, ItemId.GUTHIX_REST_3, ItemId.GUTHIX_REST_4)
) {
    heal(50, healType = HealType.Overheals)
    addEffect(RestoreEnergy(percent = 5))
    addEffect(WeakenPoison(10))
}

val ZamorakBrew = Vial(
    listOf(ItemId.ZAMORAK_BREW_1, ItemId.ZAMORAK_BREW_2, ItemId.ZAMORAK_BREW_3, ItemId.ZAMORAK_BREW_4)
) {
    hurt(20, percent = 10)
    Strength.boost( flat = 2, percent = 12)
    Attack.boost(flat = 2, percent = 20)
    Prayer.restore(flat = 9)
    Defence.drain(flat = 2, percent = 10)
}

val SaradominBrew = Vial(
    listOf(ItemId.SARADOMIN_BREW_1, ItemId.SARADOMIN_BREW_2, ItemId.SARADOMIN_BREW_3, ItemId.SARADOMIN_BREW_4)
) {
    heal(0, percent = 15, HealType.Overheals)
    Defence.boost(percent = 25)
    Strength.drain(percent = 10)
    Range.drain(percent = 10)
    Attack.drain(percent = 10)
    Magic.drain(percent = 10)
}

