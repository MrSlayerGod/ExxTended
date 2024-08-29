package org.dementhium.model.combat

sealed class Amount {
    abstract val value: Int
}

class Flat(override val value: Int): Amount()
class Percentage(val percentage: Int, inline val ofValue: () -> Int): Amount() {
    override val value: Int get() = (ofValue() * (percentage / 100.0)).toInt()
}

data class SpecialEnergyConfigs(
    val MaxSpecialEnergy: Int = 1000,
    val SpecialRestorationTimer: Int = 50,
    val SpecialRestorationAmount: Amount = Percentage(10) { MaxSpecialEnergy },
)

data class CombatConfigs(
    val MaxSpecialEnergy: Int = 1,
)

fun main() {
    val c = SpecialEnergyConfigs()
    println(c.MaxSpecialEnergy)
    println(c.SpecialRestorationAmount.value)
}