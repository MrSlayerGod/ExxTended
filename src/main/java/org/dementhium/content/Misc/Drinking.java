package org.dementhium.content.misc;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.event.Tickable;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;

/**
 *
 * @author RS2-Server team
 */
public final class Drinking {
	/**
	 * All drinkable items.
	 * NOTE: For item IDs, you must go potion dose 1, dose 2, dose 3, dose 4. EG: Attack_Potion(1)[125][2428], Attack_Potion(2), Attack_Potion(3)[121][123], Attack_Potion(4)
	 * @author Michael Bull
	 *
	 */
	public static enum Drink {

		ATTACK_POTION(new int[] { 125, 123, 121, 2428 }, new int[] { Skills.Attack}, PotionType.NORMAL_POTION),

		STRENGTH_POTION(new int[] { 119, 117, 115, 113 }, new int[] { Skills.Strength}, PotionType.NORMAL_POTION),

		DEFENCE_POTION(new int[] { 137, 135, 133, 2432 }, new int[] { Skills.Defence}, PotionType.NORMAL_POTION),

		RANGE_POTION(new int[] { 173, 171, 169, 2444 }, new int[] { Skills.Range}, PotionType.NORMAL_POTION),

		MAGIC_POTION(new int[] { 3046, 3044, 3042, 3040 }, new int[] { Skills.Magic}, PotionType.PLUS_5),

		RESTORE_POTION(new int[] { 131, 129, 127, 2430 }, new int[] { Skills.Defence, Skills.Attack, Skills.Strength, Skills.Magic, Skills.Range}, PotionType.RESTORE),

		SUPER_RESTORE_POTION(new int[] { 3030, 3028, 3026, 3024 }, new int[] { Skills.Attack, Skills.Strength, Skills.Defence, Skills.Magic, Skills.Range, Skills.Prayer, Skills.Agility, Skills.Cooking, Skills.Crafting, Skills.Farming, Skills.Firemaking, Skills.Fishing, Skills.Fletching, Skills.Herblore, Skills.Mining, Skills.Runecrafting, Skills.Slayer, Skills.Smithing, Skills.Thieving, Skills.Woodcutting}, PotionType.SUPER_RESTORE),

		PRAYER_POTION(new int[] { 143, 141, 139, 2434 }, new int[] { Skills.Prayer}, PotionType.PRAYER_POTION),

		SUPER_ATTACK_POTION(new int[] { 149, 147, 145, 2436 }, new int[] { Skills.Attack}, PotionType.SUPER_POTION),

		SUPER_STRENGTH_POTION(new int[] { 161, 159, 157, 2440 }, new int[] { Skills.Strength}, PotionType.SUPER_POTION),

		SUPER_DEFENCE_POTION(new int[] { 167, 165, 163, 2442 }, new int[] { Skills.Defence}, PotionType.SUPER_POTION),

		SARADOMIN_BREW(new int[] { 6691, 6689, 6687, 6685 }, new int[] { Skills.Attack, Skills.Defence, Skills.Strength, Skills.Magic, Skills.Range, Skills.Hitpoints}, PotionType.SARADOMIN_BREW),

		ZAMORAK_BREW(new int[] { 193, 191, 189, 2450 }, new int[] { Skills.Attack, Skills.Strength, Skills.Defence, Skills.Hitpoints, Skills.Prayer}, PotionType.ZAMORAK_BREW),

		ANTIPOISON(new int[] { 179, 177, 175, 2446 }, new int[] {  }, PotionType.ANTIPOISON),

		SUPER_ANTIPOISON(new int[] { 185, 183, 181, 2448 }, new int[] {  }, PotionType.SUPER_ANTIPOISON),

		BEER(new int[] { 1919, 1917 }, new int[] { Skills.Attack, Skills.Strength}, PotionType.BEER),

		JUG(new int[] { 1935, 1993 }, new int[] { Skills.Attack, Skills.Hitpoints}, PotionType.WINE),

		OVERLOAD(new int[] { 15335, 15334, 15333, 15332 }, new int[] { Skills.Attack, Skills.Strength, Skills.Defence, Skills.Range, Skills.Magic}, PotionType.OVERLOAD)
		;

		/**
		 * A map of drink Ids.
		 */
		private static Map<Integer, Drink> drinks = new HashMap<Integer, Drink>();

		/**
		 * Gets a drink by its ID.
		 * @param drink The drink id.
		 * @return The Drink, or <code>null</code> if the id is not a drink.
		 */
		public static Drink forId(int drink) {
			return drinks.get(drink);
		}

		/**
		 * Populates the drink map.
		 */
		static {
			for(Drink drink : Drink.values()) {
				for(int i = 0; i < drink.id.length; i++) {
					drinks.put(drink.id[i], drink);
				}
			}
		}

		/**
		 * The drink item id.
		 */
		private int[] id;

		/**
		 * The skill to boost.
		 */
		private int skill[];

		/**
		 * The potion type.
		 */
		private PotionType potionType;

		/**
		 * Creates the drink.
		 * @param id The drink item id.
		 */
		private Drink(int id[], int[] skill, PotionType potionType) {
			this.id = id;
			this.skill = skill;
			this.potionType = potionType;
		}

		/**
		 * Gets the drink item id.
		 * @return The drink item id.
		 */
		public int getId(int index) {
			return id[index];
		}

		/**
		 * Gets the drink item id.
		 * @return The drink item id.
		 */
		public int[] getIds() {
			return id;
		}

		/**
		 * Gets the boosted skill.
		 * @return The boosted skill.
		 */
		public int[] getSkills() {
			return skill;
		}

		/**
		 * Gets the boosted skill.
		 * @return The boosted skill.
		 */
		public int getSkill(int index) {
			return skill[index];
		}

		/**
		 * Gets the potion type.
		 * @return The potion type.
		 */
		public PotionType getPotionType() {
			return potionType;
		}
	}

	public static enum PotionType {
		NORMAL_POTION(0),
		SUPER_POTION(1),
		SARADOMIN_BREW(2),
		ZAMORAK_BREW(3),
		PLUS_5(4),
		RESTORE(5),
		SUPER_RESTORE(6),
		PRAYER_POTION(7),
		ANTIPOISON(8),
		SUPER_ANTIPOISON(9),
		BEER(10),
		WINE(11),
		OVERLOAD(12)
		;

		/**
		 * A map of PotionType Ids.
		 */
		private static Map<Integer, PotionType> potionTypes = new HashMap<Integer, PotionType>();

		/**
		 * Gets a PotionType by its ID.
		 * @param potionType The PotionType id.
		 * @return The PotionType, or <code>null</code> if the id is not a PotionType.
		 */
		public static PotionType forId(int potionType) {
			return potionTypes.get(potionType);
		}

		/**
		 * Populates the potion type map.
		 */
		static {
			for(PotionType potionType : PotionType.values()) {
				potionTypes.put(potionType.id, potionType);
			}
		}

		/**
		 * The potion type id.
		 */
		private int id;

		/**
		 * Creates the potion type.
		 * @param id The potion type id.
		 */
		private PotionType(int id) {
			this.id = id;
		}

		/**
		 * Gets the potion type id.
		 * @return The potion type id.
		 */
		public int getId() {
			return id;
		}
	}

	@SuppressWarnings("all")
	public static void drink(final Player player, Drink drink, int slot) {
		if(player.isDead()) {
			return;
		}
		if(player.getAttribute("consumedPotion") == Boolean.TRUE) {
			return;
		}
		Item item = player.getInventory().get(slot);
		if(item.getId() == 229) {
			return;
		}
		for (int i = 0; i < player.donorItems.length; i++) {
			if (item.getId() == player.donorItems[i] && player.getRights() < 2 && !player.getGroup().equalsIgnoreCase("Donator") && !player.getGroup().equalsIgnoreCase("Premium") && !player.getGroup().equalsIgnoreCase("Super")) {
				player.sendMessage("You need to be a donator to use this item.");
				return;
			}
		}
		for (int i = 0; i < player.premItems.length; i++) {
			if (item.getId() == player.premItems[i] && player.getRights() < 2 && !player.getGroup().equalsIgnoreCase("Premium") && !player.getGroup().equalsIgnoreCase("Super")) {
				player.sendMessage("You need to be atleast a premium donator to use this item.");
				return;
			}
		}
		for (int i = 0; i < player.superItems.length; i++) {
			if (item.getId() == player.superItems[i] && player.getRights() < 2 && !player.getGroup().equalsIgnoreCase("Super")) {
				player.sendMessage("You need to be atleast a super donator to use this item.");
				return;
			}
		}
		player.setAttribute("consumedPotion", Boolean.TRUE);
		World.getWorld().submit(new Tickable(2) {
			@Override
			public void execute() {
				player.removeAttribute("consumedPotion");
				this.stop();
			}
		});
		player.getCombatState().setAttackDelay(player.getCombatState().getAttackDelay() + 2);
		player.animate(829);
		player.getInventory().set(slot, null);
		String potionName = item.getDefinition().getName().toLowerCase().substring(0, item.getDefinition().getName().length() - 3).replaceAll("  potion", "");
		potionName = potionName.replaceAll(" potion", "");
		potionName = potionName.replaceAll("potion", "");
		switch(drink.getPotionType()) {
			case NORMAL_POTION:
				ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + "potion.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					int modification = (int) Math.floor((drink == Drink.RANGE_POTION ? 4 : 3) + (player.getSkills().getLevelForXp(skill) * 0.1));
					player.getSkills().increaseLevelToMaximumModification(skill, modification);
				}
				break;
			case SUPER_POTION:
				ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + "potion.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					int modification = (int) Math.floor(5 + (player.getSkills().getLevelForXp(skill) * 0.15));
					player.getSkills().increaseLevelToMaximumModification(skill, modification);
				}
				break;
			case PRAYER_POTION:
				ActionSender.sendChatMessage(player, 0, "You drink some of your restore prayer potion.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					int modification = (int) Math.floor(7 + (player.getSkills().getLevelForXp(skill) * 0.25));
					/**
					 * Holy wrench increases prayer restoration.
					 */
					if(skill == Skills.Prayer) {
						if(player.getInventory().contains(6714)) {
							modification++;
							if(player.getSkills().getLevelForXp(Skills.Prayer) >= 40) {
								modification++;
							}
							if(player.getSkills().getLevelForXp(Skills.Prayer) >= 70) {
								modification++;
							}
						}
						player.getSkills().restorePray(modification);
					} else {
						player.getSkills().increaseLevelToMaximum(skill, modification);
					}
				}
				break;
			case RESTORE:
			case SUPER_RESTORE:
				ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + "potion.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					int modification = (int) (player.getSkills().getLevelForXp(skill) / 3);
					/**
					 * Holy wrench increases prayer restoration.
					 */
					if(skill == Skills.Prayer) {
						if(player.getInventory().contains(6714)) {
							modification++;
							if(player.getSkills().getLevelForXp(Skills.Prayer) >= 40) {
								modification++;
							}
							if(player.getSkills().getLevelForXp(Skills.Prayer) >= 70) {
								modification++;
							}
						}
						player.getSkills().restorePray(modification);
					} else {
						player.getSkills().increaseLevelToMaximum(skill, modification);
					}
				}
				break;
			case PLUS_5:
				ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + "potion.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					int modification = 5;
					player.getSkills().increaseLevelToMaximumModification(skill, modification);
				}
				break;
			case SARADOMIN_BREW:
				ActionSender.sendChatMessage(player, 0, "You drink some of the foul liquid.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					if(skill == Skills.Hitpoints) {
						int hitpointsModification = (int) (player.getSkills().getMaxHitpoints() * 0.15);
						player.getSkills().heal(hitpointsModification, player.getSkills().getMaxHitpoints() + hitpointsModification);
					} else if(skill == Skills.Defence) {
						int defenceModification = (int) (player.getSkills().getLevelForXp(Skills.Defence) * 0.25);
						player.getSkills().increaseLevelToMaximumModification(skill, defenceModification);
					} else {
						int modification = (int) (player.getSkills().getLevelForXp(skill) * 0.10);
						player.getSkills().decreaseLevelOnce(skill, modification);
					}
				}
				break;
			case ZAMORAK_BREW:
				ActionSender.sendChatMessage(player, 0, "You drink some of the foul liquid.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					if(skill == Skills.Attack) {
						int attackModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Attack)) * 0.20);
						player.getSkills().increaseLevelToMaximumModification(skill, attackModification);
					} else if(skill == Skills.Strength) {
						int strengthModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Strength) * 0.12));
						player.getSkills().increaseLevelToMaximumModification(skill, strengthModification);
					} else if(skill == Skills.Prayer) {
						int prayerModification = (int) Math.floor(player.getSkills().getLevelForXp(Skills.Strength) * 0.10);
						player.getSkills().increaseLevelToMaximum(skill, prayerModification);
					} else if(skill == Skills.Defence) {
						int defenceModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Defence) * 0.10));
						player.getSkills().decreaseLevelToZero(skill, defenceModification);
					} else if(skill == Skills.Hitpoints) {
						World.getWorld().submit(new Tickable(3) {
							@Override
							public void execute() {
								int hitpointsModification = (int) Math.floor(2 + (player.getSkills().getLevel(Skills.Hitpoints) * 0.10));
								if(player.getSkills().getLevel(Skills.Hitpoints) - hitpointsModification < 0) {
									hitpointsModification = player.getSkills().getLevel(Skills.Hitpoints);
								}
								player.hit(hitpointsModification);
								this.stop();
							}
						});
					}
				}
				break;
			case ANTIPOISON:
			case SUPER_ANTIPOISON:
				ActionSender.sendChatMessage(player, 0, "You drink some of your " + item.getDefinition().getName().toLowerCase().substring(0, item.getDefinition().getName().length() - 3) + ".");
			/*if(player.getCombatState().canBePoisoned()) {
				player.getCombatState().setCanBePoisoned(false);
				World.getWorld().submit(new Tickable(drink.getPotionType() == PotionType.ANTIPOISON ? 150 : 1000) {
					public void execute() {
						player.getCombatState().setCanBePoisoned(true);
						this.stop();
					}
				});
			}*/
				//TODO
				break;
			case BEER:
				ActionSender.sendChatMessage(player, 0, "You drink the beer. You feel slightly reinvigorated...");
				ActionSender.sendChatMessage(player, 0, "...and slightly dizzy too.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					if(skill == Skills.Attack) {
						int attackModification = (int) (player.getSkills().getLevelForXp(Skills.Strength) * 0.07);
						player.getSkills().decreaseLevelToZero(Skills.Attack, attackModification);
					} else if(skill == Skills.Strength) {
						int strengthModification = (int) (player.getSkills().getLevelForXp(Skills.Strength) * 0.04);
						player.getSkills().increaseLevelToMaximumModification(Skills.Strength, strengthModification);
					}
				}
				break;
			case WINE:
				ActionSender.sendChatMessage(player, 0, "You drink the wine. You feel slightly reinvigorated...");
				ActionSender.sendChatMessage(player, 0, "...and slightly dizzy too.");
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					if(skill == Skills.Attack) {
						int attackModification = 2;
						player.getSkills().decreaseLevelToZero(Skills.Attack, attackModification);
					} else if(skill == Skills.Hitpoints) {
						player.getSkills().increaseLevelToMaximum(Skills.Hitpoints, 11);
					}
				}
				break;
			case OVERLOAD:
				ActionSender.sendMessage(player, "You drink some of the foul liquid.");
				player.setAttribute("overloads", Boolean.TRUE);
				World.getWorld().submit(new Tickable(2) {
					int count;
					@Override
					public void execute() {
						if (count < 5 && !player.isDead()) {
							player.animate(3170);
							player.hit(100);
							count++;
						} else {
							this.stop();
						}
					}

				});
				for(int i = 0; i < drink.getSkills().length; i++) {
					int skill = drink.getSkill(i);
					if(skill == Skills.Attack) {
						int attackModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Attack)) * 0.25);
						player.getSkills().increaseLevelToMaximumModification(skill, attackModification);
						continue;
					}
					if (skill == Skills.Defence) {
						int defenceModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Defence)) * 0.25);
						player.getSkills().increaseLevelToMaximumModification(skill, defenceModification);
						continue;
					}
					if (skill == Skills.Strength) {
						int strengthModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Strength)) * 0.25);
						player.getSkills().increaseLevelToMaximumModification(skill, strengthModification);
						continue;
					}
					if (skill == Skills.Range) {
						int rangeModification = (int) Math.floor(2 + (player.getSkills().getLevelForXp(Skills.Range)) * 0.22);
						player.getSkills().increaseLevelToMaximumModification(skill, rangeModification);
						continue;
					}
					if (skill == Skills.Magic) {
						player.getSkills().increaseLevelToMaximumModification(skill, 7);
						continue;
					}
				}

				World.getWorld().submit(new Tickable(300) {
					@Override
					public void execute() {
						player.setAttribute("overloads", Boolean.FALSE);
						this.stop();
					}
				});
				break;
		}

		int currentPotionDose = 0;
		for(int i = 0; i < drink.getIds().length; i++) {
			if(item.getId() == drink.getId(i)) {
				currentPotionDose = i + 1;
				break;
			}
		}
		if(drink.getPotionType() != PotionType.BEER && drink.getPotionType() != PotionType.WINE) {
			ActionSender.sendChatMessage(player, 0, currentPotionDose > 1 ? ("You have " + (currentPotionDose - 1) + " dose" + (currentPotionDose > 2 ? "s" : "") + " of potion left.") : "You have finished your potion.");
		}
		int newPotion = 229;
		if(currentPotionDose > 1) {
			newPotion = drink.getId(currentPotionDose - 2);
		}
		player.getInventory().set(slot, new Item(newPotion));
		player.getInventory().refresh();
	}

}
