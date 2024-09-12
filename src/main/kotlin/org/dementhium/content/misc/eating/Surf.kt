package org.dementhium.content.misc.eating

import org.dementhium.content.misc.skillEffect.builder.HealType
import org.dementhium.content.misc.skillEffect.builder.boost
import org.dementhium.content.misc.skillEffect.plus
import org.dementhium.model.player.skills.SkillId.*
import org.dementhium.util.ItemId

val Crayfish = Single(ItemId.CRAYFISH) {
    heal(20)
}

val Shrimp = Single(ItemId.SHRIMPS) {
    heal(30)
}

val Anchovies = Single(ItemId.ANCHOVIES, Shrimp)
val Karambwanji = Single(ItemId.KARAMBWANJI, Shrimp)
val Sardine = Single(ItemId.SARDINE, Shrimp)

val PoisonKarambwan = Single(ItemId.POISON_KARAMBWAN) {
    hurt(50)
}

val Herring = Single(ItemId.HERRING) {
    heal(50)
}

val Mackerel = Single(ItemId.MACKEREL) {
    heal(60)
}

val Trout = Single(ItemId.TROUT) {
    heal(70)
}

val Cod = Single(ItemId.COD, Trout)

val Pike = Single(ItemId.PIKE) {
    heal(80)
}

val Salmon = Single(ItemId.SALMON) {
    heal(90)
}

val CookedSlimyEel = Single(ItemId.COOKED_SLIMY_EEL) {
    heal(80, percent = 3)
}

val Tuna = Single(ItemId.TUNA) {
    heal(100)
}

val RainbowFish = Single(ItemId.RAINBOW_FISH) {
    heal(110)
}

val Lobster = Single(ItemId.LOBSTER) {
    heal(120)
}

val Bass = Single(ItemId.BASS) {
    heal(130)
}

val Swordfish = Single(ItemId.SWORDFISH) {
    heal(140)
}

val LavaEel = Single(ItemId.LAVA_EEL) {
    heal(90, percent = 3)
}

val Monkfish = Single(ItemId.MONKFISH) {
    heal(160)
}

val Shark = Single(ItemId.SHARK) {
    heal(200)
}

private val CavefishOptions = Attack + Strength + Defence + Range + Magic
val Cavefish = Single(ItemId.CAVEFISH) {
    heal(200)
    CavefishOptions.random().boost(flat = 2)
}

val SeaTurtle = Single(ItemId.SEA_TURTLE) {
    heal(210)
}

val Karambwan = Single(ItemId.COOKED_KARAMBWAN) {
    heal(180)
}

val MantaRay = Single(ItemId.MANTA_RAY) {
    heal(220)
}

val Rocktail = Single(ItemId.ROCKTAIL) {
    heal(230, healType = HealType.Overheals)
}