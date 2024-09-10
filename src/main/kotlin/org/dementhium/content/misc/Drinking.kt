package org.dementhium.content.misc

import org.dementhium.content.misc.drinking.Drink
import org.dementhium.model.Item
import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.util.equalsItem
import org.dementhium.util.submitTickable

object Drinking {

    private val potionSlurp = "consumedPotion"

    private val potionDelay = 2

    @JvmStatic
    fun forId(drinkId: Int) = Drink.values().firstOrNull { drinkId in it.consumableIds }

    @JvmStatic
    fun drink(player: Player, drink: Drink, slot: Int) {
        if (player.isDead) return
        val attr = player.getAttribute<Boolean>(potionSlurp)
        if (attr == true) return
        val slotItem = player.inventory[slot]
        val nextItem: Item = when(val nextStage = drink.consumableIds.nextStageFromId(slotItem.id)) {
            is ConsumableStatus.IdNotContained -> return
            is ConsumableStatus.ConsumableDepleted -> nextStage.depletionType.item
            is ConsumableStatus.ConsumableNotDepleted -> nextStage.item
        }
        player.setAttribute(potionSlurp, true)
        World.getWorld().submitTickable(potionDelay) {
            player.removeAttribute(potionSlurp)
            stop()
        }
        player.combatState.attackDelay += potionDelay
        player.animate(829)
        val potionName = slotItem.definition.name.lowercase()
        with(drink.skillEffect) {
            player.skills.applyEffect()
        }
        if (!slotItem.equalsItem(nextItem)) {
            player.inventory[slot] = nextItem
        }
        player.inventory.refresh()
    }
}