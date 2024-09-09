package org.dementhium.content.misc

import org.dementhium.content.misc.drinking.Drink
import org.dementhium.model.Item
import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.util.submitTickable

object Drinking {

    private val potionSlurp = "consumedPotion"

    private val potionDelay = 2

    @JvmStatic
    fun forId(drinkId: Int) = Drink.values().firstOrNull { drinkId in it.doses }

    fun Drink.getNextDoseId(drinkId: Int): Int {
        val nextIndex = doses.indexOf(drinkId) - 1
        return doses.getOrNull(nextIndex) ?: emptyId
    }

    @JvmStatic
    fun drink(player: Player, drink: Drink, slot: Int) {
        if (player.isDead) return
        val attr = player.getAttribute<Boolean>(potionSlurp)
        if (attr == true) return
        val slotItem = player.inventory[slot]
        if (slotItem.id !in drink.doses) return
        /**None of that donator shit**/
        player.setAttribute(potionSlurp, true)
        World.getWorld().submitTickable(potionDelay) {
            player.removeAttribute(potionSlurp)
            stop()
        }
        player.combatState.attackDelay += 2
        player.animate(829)
        val potionName = slotItem.definition.name.lowercase()
        with(drink.skillEffect) {
            player.skills.applyEffect()
        }
        val nextDoseId = drink.getNextDoseId(slotItem.id)
        val nextDoseItem = Item(nextDoseId)
        player.inventory[slot] = nextDoseItem
        player.inventory.refresh()
    }
}