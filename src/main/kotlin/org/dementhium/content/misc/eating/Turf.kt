package org.dementhium.content.misc.eating

import org.dementhium.content.misc.DepletionType
import org.dementhium.content.misc.buildConsumable
import org.dementhium.content.misc.skillEffect.builder.HealType
import org.dementhium.util.ItemId
import kotlin.random.Random

val CookedMeat = Single(ItemId.COOKED_MEAT) {
    heal(30)
}

val CookedUndeadMeat = Single(ItemId.COOKED_MEAT_4293) {
    heal(10)
    sendMessage("It heals some health although it tastes absolutely disgusting")
}

val CookedUgthanki = Single(ItemId.UGTHANKI_MEAT, CookedMeat)
val CookedChicken = Single(ItemId.COOKED_CHICKEN, CookedMeat)
val CookedUndeadChicken = Single(ItemId.COOKED_CHICKEN_4291, CookedUndeadMeat)
val CookedTurkey = Single(ItemId.COOKED_TURKEY, CookedChicken)

val CookedTurkeyDrumstick = Single(ItemId.COOKED_TURKEY_DRUMSTICK) {
    heal(20)
}

val RoastBirdMeat = Single(ItemId.ROAST_BIRD_MEAT) {
    heal(60)
}

val ThinSnailMeat = Single(ItemId.THIN_SNAIL_MEAT) {
    heal(70)
}

val SpiderOnStick = buildConsumable<Food> {
    consumableId(ItemId.SPIDER_ON_STICK_6297)
    depletionType(DepletionType.Empty(ItemId.SKEWER_STICK))
    addEffect {
        heal(100)
    }
}

val SpiderOnShaft = Single(ItemId.SPIDER_ON_SHAFT) {
    val healAmount = Random.nextInt(70, 101)
    heal(healAmount)
}

val LeanSnailMeat = Single(ItemId.LEAN_SNAIL_MEAT) {
    heal(60, percent = 3)
}

val CookedCrabMeat = buildConsumable<Food> {
    setConsumableIds(
        ItemId.COOKED_CRAB_MEAT, ItemId.COOKED_CRAB_MEAT_7523, ItemId.COOKED_CRAB_MEAT_7524,
        ItemId.COOKED_CRAB_MEAT_7525, ItemId.COOKED_CRAB_MEAT_7526
    )
    depletionType(DepletionType.Remove)
}

val RoastBeastMeat = Single(ItemId.ROAST_BEAST_MEAT) {
    heal(80)
}

val FatSnailMeat = Single(ItemId.FAT_SNAIL_MEAT) {
    heal(70, percent = 3)
}

val CookedChompy = Single(ItemId.COOKED_CHOMPY) {
    heal(100)
}

val CookedJubbly = Single(ItemId.COOKED_JUBBLY) {
    heal(150)
}

val CookedOomlieWrap = Single(ItemId.COOKED_OOMLIE_WRAP) {
    heal(140)
}

val LocustMeat = Single(ItemId.LOCUST_MEAT, CookedMeat)

val DruggedMeat = Single(ItemId.DRUGGED_MEAT) {
    val chance = Random.nextInt()
    if (chance > 1_000_000_000 )
    heal(percent = 100, healType = HealType.Overheals)
}
