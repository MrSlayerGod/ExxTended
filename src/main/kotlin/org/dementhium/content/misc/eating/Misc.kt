package org.dementhium.content.misc.eating

import org.dementhium.content.misc.skillEffect.RestoreEnergy
import org.dementhium.content.misc.skillEffect.buildSkillEffect
import org.dementhium.content.misc.skillEffect.builder.boost
import org.dementhium.content.misc.skillEffect.plus
import org.dementhium.content.misc.skillEffect.skillEffect
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId
import org.dementhium.util.random.SimpleWeightedTable

val Cheese = Single(ItemId.CHEESE) {
    heal(20)
}

val Bread = Single(ItemId.BREAD) {
    heal(20)
}

val Doughnut = Single(ItemId.DOUGHNUT) {
    heal(20)
}

val UgthankiKebab = Single(ItemId.UGTHANKI_KEBAB) {
    heal(190)
}

val PurpleSweetsS = Single(ItemId.PURPLE_SWEETS_10476) {
    heal(listOf(10, 20, 30).random())
    addEffect(RestoreEnergy(percent = 20))
}

val PurpleSweets = Single(ItemId.PURPLE_SWEETS, PurpleSweetsS)
val BlueSweets = Single(ItemId.BLUE_SWEETS, PurpleSweets)
val DeepBlueSweets = Single(ItemId.DEEP_BLUE_SWEETS, PurpleSweets)
val WhiteSweets = Single(ItemId.WHITE_SWEETS, PurpleSweets)
val RedSweets = Single(ItemId.RED_SWEETS, PurpleSweets)
val GreenSweets = Single(ItemId.GREEN_SWEETS, PurpleSweets)
val PinkSweets = Single(ItemId.PINK_SWEETS, PurpleSweets)


private val KebabTable = SimpleWeightedTable(
    2 to buildSkillEffect {
        heal(300)
        (Attack + Strength + Defence).boost(flat = 2, percent = 2)
    },
    5 to skillEffect {
        // womp
    },
    12 to buildSkillEffect {
        heal(101, percent = 10)
    },
    34 to buildSkillEffect {
        heal(percent = 10)
    }
)

val Kebab = Single(
    ItemId.KEBAB,
    skillEffect {
        with(KebabTable.roll()) { applyEffect() }
    }
)
