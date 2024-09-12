package org.dementhium.content.misc

import org.dementhium.content.misc.drinking.Drink
import org.dementhium.content.misc.eating.Food
import org.dementhium.content.misc.skillEffect.SkillEffect
import org.dementhium.model.Item
import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.util.equalsItem
import org.dementhium.util.item.copy
import org.dementhium.util.submitTickable
import kotlin.reflect.KClass

abstract class Consumable(
    val consumableIds: ConsumableStages,
    val skillEffect: SkillEffect
) {
    companion object {
        val consumables: MutableMap<KClass<out Consumable>, MutableSet<in Consumable>> = mutableMapOf()

        inline fun <reified T: Consumable> getConsumablesBy(): MutableSet<in T> = consumables.getOrPut(T::class) {
            mutableSetOf()
        }

        inline fun <reified T: Consumable> addConsumable(consumable: T) {
            val foundConsumables = getConsumablesBy<T>()
            foundConsumables.add(consumable)
        }

        @JvmStatic fun forId(id: Int): Consumable? {
            val from = listOf(Drink.values(), Food.values())
            from.forEach { type ->
                type.forEach { consumable ->
                    if (id in consumable.consumableIds) return consumable
                }
            }
            return null
        }

        private val consumeAnim = 829

        private fun attributeFor(consumable: Consumable): String = when(consumable) {
            is Drink -> "consumedPotion"
            is Food -> "consumed"
            else -> "ERROR"
        }

        private fun consumableDelayFor(consumable: Consumable): Int = when(consumable) {
            is Drink -> 2
            is Food -> 3
            else -> 0
        }

        @JvmStatic
        fun consume(player: Player, consumable: Consumable, clickedSlot: Int) {

            if (player.isDead) return

            val attribute = attributeFor(consumable)

            if (attribute == "ERROR") {
                player.sendMessage("Consumable error! Please report to admin :)")
                return
            }

            val consumableDelay = consumableDelayFor(consumable)

            val attr = player.getAttribute<Boolean>(attribute)

            if (attr == true) return

            val slotItem = player.inventory[clickedSlot]
            
            val nextItem: Item = when(val nextStage = consumable.consumableIds.nextStageFromId(slotItem.id)) {
                is ConsumableStatus.IdNotContained -> return
                is ConsumableStatus.ConsumableDepleted -> when (val depletionType = nextStage.depletionType) {
                    is DepletionType.Remove -> {
                        val new = slotItem.copy()
                        new.amount -= 1
                        new
                    }
                    else -> depletionType.item
                }
                is ConsumableStatus.ConsumableNotDepleted -> nextStage.item
            }

            player.setAttribute(attribute, true)

            World.getWorld().submitTickable(consumableDelay) {
                player.removeAttribute(attribute)
                stop()
            }

            player.combatState.attackDelay += consumableDelay

            player.animate(consumeAnim)

            with(consumable.skillEffect) {
                player.skills.applyEffect()
            }
            if (!slotItem.equalsItem(nextItem)) {
                player.inventory[clickedSlot] = nextItem
            }
            player.inventory.refresh()
        }
    }

    override fun toString() = "Consumable($consumableIds, $skillEffect)"
}

