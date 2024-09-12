package org.dementhium.content.misc.eating

import org.dementhium.content.misc.Consumable
import org.dementhium.content.misc.ConsumableStages
import org.dementhium.content.misc.skillEffect.SkillEffect

class Food(
    consumableIds: ConsumableStages,
    skillEffect: SkillEffect
): Consumable(consumableIds, skillEffect) {
    companion object {
        fun values() = arrayOf<Food>(
            Crayfish, Shrimp, Anchovies, Karambwanji, PoisonKarambwan, Herring, Sardine, Mackerel,
            Trout, Cod, Pike, Salmon, CookedSlimyEel, Tuna, RainbowFish, Lobster, Bass, Swordfish,
            LavaEel, Monkfish, Shark, Cavefish, SeaTurtle, Karambwan, MantaRay, Rocktail,

            Banana, RedBanana, DwellBerries, BananaSlices, RedBananaSlices, Orange, OrangeChunks,
            OrangeSlices, PineappleRing, PineappleChunks, Papaya, Jangerberries, Strawberry,
            Tomato, WatermelonSlice, Lemon, LemonSlices, LemonChunks, Lime, LimeSlices, LimeChunks,
            StrangeFruit, WhiteTreeFruit,

            RawPotato, Onion, NormalCabbage, DraynorCabbage, CookedSweetcorn, EvilTurnip,

            CookedMeat, CookedUndeadMeat, CookedChicken, CookedUndeadChicken, CookedTurkey,
            CookedTurkeyDrumstick, RoastBirdMeat, ThinSnailMeat, SpiderOnStick, SpiderOnShaft,
            LeanSnailMeat, CookedCrabMeat, RoastBeastMeat, FatSnailMeat, CookedChompy, CookedJubbly,
            CookedOomlieWrap, CookedUgthanki, LocustMeat, DruggedMeat,

            RedberryPie, MeatPie, ApplePie, GardenPie, FishingPie, AdmiralPie, WildPie, SummerPie,

            Cake, ChocolateCake, CookedFishcake, MintCake, DwarvenRockCake,

            BakedPotato, ButteredPotato, ChiliPotato, CheesePotato, EggPotato, MushroomPotato, TunaPotato,

            PlainPizza, MeatPizza, AnchovyPizza, PineapplePizza,

            Cheese, Bread, Doughnut, UgthankiKebab, Kebab, PurpleSweetsS, PurpleSweets, BlueSweets, DeepBlueSweets,
            WhiteSweets, RedSweets, GreenSweets, PinkSweets
        )
    }
}