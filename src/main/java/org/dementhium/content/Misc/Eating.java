package org.dementhium.content.misc;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.event.Tickable;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;

/**
 * An action for eating food and drinking
 * 
 * @author Linux
 * @author 'Mystic Flow
 */
public class Eating {

	/**
	 * Represents types of bones.
	 * 
	 * @author Linux
	 * 
	 */
	public static enum Food {
		ANCHOVIE(319, 1),
		SHRIMP(315, 3),
		CHICKEN(2140, 3),
		MEAT(2142, 3),
		CAKE(1891, 4, 1893),
		SEAWEED(403, 4),
		BREAD(2309, 5),
		HERRING(347, 5),
		TROUT(333, 7),
		COD(339, 7),
		PIKE(351, 8),
		SALMON(329, 9),
		TUNA(361, 10),
		LOBSTER(379, 12),
		BASS(365, 13),
		SWORDFISH(373, 14),
		MONKFISH(7946, 16),
		SHARK(385, 20),
		TURTLE(397, 21),
		MANTA(391, 22),
		ROCKTAIL(15272, 23),
		SUMMER_PIE_FULL(7218, 11, 7220, Effect.SUMMER_PIE),
		SUMMER_PIE_HALF(7220, 11, 2313, Effect.SUMMER_PIE);

		/**
		 * The food id
		 */
		private int id;

		/**
		 * The healing health
		 */
		private int heal;

		/**
		 * The new food id if needed
		 */
		private int newId;

		/**
		 * Our effect
		 */
		private Effect effect;

		/**
		 * A map of object ids to foods.
		 */
		private static Map<Integer, Food> foods = new HashMap<Integer, Food>();

		/**
		 * Gets a food by an object id.
		 * 
		 * @param object
		 *            The object id.
		 * @return The food, or <code>null</code> if the object is not a food.
		 */
		public static Food forId(int object) {
			return foods.get(object);
		}

		/**
		 * Populates the tree map.
		 */
		static {
			for (final Food food : Food.values()) {
				foods.put(food.id, food);
			}
		}

		/**
		 * Represents a food being eaten
		 * 
		 * @param id
		 *            The food id
		 * @param health
		 *            The healing health received
		 */
		private Food(int id, int heal) {
			this.id = id;
			this.heal = heal;
		}

		/**
		 * Represents a part of a food item being eaten (example: cake)
		 * 
		 * @param id
		 *            The food id
		 * @param heal
		 *            The heal amount
		 * @param newId
		 *            The new food id
		 */
		private Food(int id, int heal, int newId) {
			this.id = id;
			this.heal = heal;
			this.newId = newId;
		}

		private Food(int id, int heal, int newId, Effect effect) {
			this.id = id;
			this.heal = heal;
			this.newId = newId;
			this.effect = effect;
		}

		/**
		 * Gets the id.
		 * 
		 * @return The id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the exp amount.
		 * 
		 * @return The exp amount.
		 */
		public int getHeal() {
			return heal;
		}

		/**
		 * Gets the new food id
		 * 
		 * @return The new food id.
		 */
		public int getNewId() {
			return newId;
		}
	}

	public static enum Effect {
		SUMMER_PIE {

			public void effect(Player player) {

			}

		};

		public void effect(Player player) {

		}
	}

	private static final int EAT_ANIM = 829;

	public static void eat(final Player player, Food food, int clickedSlot) {
		if(player.isDead()) {
			return;
		}
		if(player.getAttribute("consumed") != Boolean.TRUE) {
			player.setAttribute("consumed", Boolean.TRUE);
			ActionSender.sendChatMessage(player, 0, "You eat the " + ItemDefinition.forId(food.getId()).getName().toLowerCase()+".");
			player.animate(EAT_ANIM);
			player.getCombatState().setAttackDelay(player.getCombatState().getAttackDelay() + 3);
			if (food.getNewId() == 0) {
				player.getInventory().getContainer().set(clickedSlot, null);
			} else {
				player.getInventory().getContainer().set(clickedSlot, null);
				player.getInventory().getContainer().set(clickedSlot, new Item(food.getNewId()));
			}
			if(food.getId() == 15272) {
				if(!player.getGroup().equalsIgnoreCase("Donator") && !player.getGroup().equalsIgnoreCase("Premium") && !player.getGroup().equalsIgnoreCase("Super") && player.getRights() < 2) {
					player.sendMessage("You need to be a donator to eat rocktails.");
				} else {
					player.getSkills().heal(food.getHeal() * 10, (player.getSkills().getLevelForXp(Skills.Hitpoints) * 10) + 100);
				}
			} else {
				player.getSkills().heal(food.getHeal() * 10);
			}
			player.getInventory().refresh();
			if(food.effect != null) {
				food.effect.effect(player);
			}
			if(food.getId() == 7218 || food.getId() == 7220) {
				World.getWorld().submit(new Tickable(1) {
					@Override
					public void execute() {
						player.removeAttribute("consumed");
						this.stop();
					}
				});
			} else {
				World.getWorld().submit(new Tickable(3) {
					@Override
					public void execute() {
						player.removeAttribute("consumed");
						this.stop();
					}
				});
			}
		}
	}
}