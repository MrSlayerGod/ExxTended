package org.dementhium.model.player;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.combat.Combat.CombatStyle;
import org.dementhium.model.combat.Combat.CombatType;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.definition.WeaponInterface.WeaponButton;
import org.dementhium.net.ActionSender;
import org.dementhium.event.Tickable;
import org.dementhium.model.World;

public class Equipment {

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
	SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
	SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13;

	public static final int SIZE = 14;

	private final Container equipment = new Container(15, false);
	private final Player player;

	public Equipment(Player player) {
		this.player = player;
	}

	public Container getEquipment() {
		return equipment;
	}

	public boolean contains(int item) {
		return equipment.containsOne(new Item(item));
	}

	public void deleteItem(int item, int amount) {
		equipment.remove(new Item(item, amount));
		refresh();
	}

	public Item get(int slot) {
		return equipment.get(slot);
	}

	public void set(int slot, Item item) {
		equipment.set(slot, item);
		refresh();
	}

	public void deleteAll(int item) {
		equipment.removeAll(new Item(item));
		refresh();
	}

	public void toggleStyle(Player p, int buttonId) {
		if (p.getAttribute("autoCastSpell") != null) {
			p.setAttribute("autoCastSpell", null);
			ActionSender.sendConfig(p, 108, -1);
		}
		WeaponInterface weaponInterface = WeaponInterface.forId(get(SLOT_WEAPON) == null ? -1 : get(SLOT_WEAPON).getId());
		int select = buttonId - 11;
		ActionSender.sendConfig(player, 43, select);
		if(weaponInterface != null) {
			WeaponButton button = weaponInterface.getButtons().get(select);
			player.getSettings().setCombatType(button.getCombatType());
			player.getSettings().setCombatStyle(button.getCombatStyle());
		} else {
			player.getSettings().setCombatType(CombatType.NONE);
			player.getSettings().setCombatStyle(CombatStyle.NONE);
		}
		player.getSettings().setCurrentWeaponInterface(weaponInterface);
	}

	public void clear() {
		equipment.clear();
		refresh();
	}

	public void refresh() {
		player.getMask().setApperanceUpdate(true);
		ActionSender.sendItems(player, 94, equipment, false);
		player.getBonuses().calculate();
	}

	private static String[] FULL_BODY = { "Investigator's coat", "armour",
		"hauberk", "top", "shirt", "platebody", "Ahrims robetop",
		"Karils leathertop", "brassard", "Robe top", "robetop",
		"platebody (t)", "platebody (g)", "chestplate", "torso",
		"Morrigan's", "leather body", "chainbody", "robe top", "Pernix body", "Torva platebody" };
	private static String[] FULL_HAT = { "Pernix cowl", "sallet", "med helm", "coif",
		"Dharok's helm", "hood", "Initiate helm", "Coif",
	"Helm of neitiznot" };
	private static String[] FULL_MASK = { "Slayer helmet", "Pernix cowl", "Christmas ghost hood",
		"Dragon full helm (or)", "sallet", "full helm", "mask",
		"Veracs helm", "Guthans helm", "Torags helm", "Karils coif",
		"full helm (t)", "full helm (g)", "mask" };

	public static int getItemType(int wearId) {
		if(wearId == -1) {
			return -1;
		}
		return ItemDefinition.forId(wearId).getEquipmentSlot();
	}

	public static boolean isFullBody(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_BODY.length; i++) {
			if (weapon.contains(FULL_BODY[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullHat(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_HAT.length; i++) {
			if (weapon.endsWith(FULL_HAT[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullMask(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_MASK.length; i++) {
			if (weapon.endsWith(FULL_MASK[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTwoHanded(ItemDefinition def) {
		String wepEquiped = def.getName();
		int itemId = def.getId();
		if (itemId == 4212)
			return true;
		else if (itemId == 17039)
			return true;
		else if (itemId == 13902)
			return true;
		else if (itemId == 16425)
			return true;
		else if (itemId == 4214)
			return true;
		else if (itemId == 18353) 
			return true;
		else if (itemId == 20171) 
			return true;
		else if (itemId == 11716) 
			return true;
		else if (wepEquiped.endsWith("claws"))
			return true;
		else if (wepEquiped.endsWith("anchor"))
			return true;
		else if (wepEquiped.endsWith("2h sword"))
			return true;
		else if (wepEquiped.endsWith("longbow"))
			return true;
		else if (wepEquiped.equals("Seercull"))
			return true;
		else if (wepEquiped.endsWith("shortbow"))
			return true;
		else if (wepEquiped.endsWith("Longbow"))
			return true;
		else if (wepEquiped.endsWith("Shortbow"))
			return true;
		else if (wepEquiped.endsWith("bow full"))
			return true;
		else if (wepEquiped.equals("Dark bow"))
			return true;
		else if (wepEquiped.endsWith("halberd"))
			return true;
		else if (wepEquiped.equals("Granite maul"))
			return true;
		else if (wepEquiped.equals("Karils crossbow"))
			return true;
		else if (wepEquiped.equals("Torag's hammers"))
			return true;
		else if (wepEquiped.equals("Verac's flail"))
			return true;
		else if (wepEquiped.equals("Dharok's greataxe"))
			return true;
		else if (wepEquiped.equals("Guthan's warspear"))
			return true;
		else if (wepEquiped.equals("Tzhaar-ket-om"))
			return true;
		else if (wepEquiped.endsWith("godsword"))
			return true;
		else if (wepEquiped.equals("Saradomin sword"))
			return true;
		else if (wepEquiped.equals("Hand Cannon"))
			return true;
		else if (wepEquiped.equals("salamander"))
			return true;
		else
			return false;
	}

	public int getRenderAnim() {
		if (get(3) == null)
			return 1426;
		int renderEmote = get(3).getDefinition().getRenderId();
		if (renderEmote != 0)
			return renderEmote;
		return 1426;
	}

	public void unEquip(Player p, int itemId, int interfaceId, int slot) {
		if(p.getAttribute("spellQueued") != null) {
			p.removeAttribute("spellQueued");
		}
		if (slot < 0 || itemId < 0) {
			return;
		}
		if (p.getInventory().getFreeSlots() <= 0) {
			p.sendMessage("You do not have enough inventory space to complete this action.");
			return;
		}
		if (slot <= 15 && p.getEquipment().get(slot) != null) {
			if(p.getInventory().getContainer().add(p.getEquipment().get(slot))) {
				p.getEquipment().set(slot, null);
				p.getInventory().refresh();
			}
		}
	}

	public void equip(Player p, int buttonId, int buttonId2, int buttonId3) {
		if(p.getAttribute("spellQueued") != null)
			p.removeAttribute("spellQueued");
		if (buttonId2 < 0 || buttonId2 >= Inventory.SIZE)
			return;
		Item item = p.getInventory().getContainer().get(buttonId2);
		if (item == null)
			return;
		/*for (int i = 0; i < p.donorItems.length; i++) {
			if (buttonId3 == p.donorItems[i] && p.getRights() < 2 && !p.getGroup().equalsIgnoreCase("Donator") && !p.getGroup().equalsIgnoreCase("Premium") && !p.getGroup().equalsIgnoreCase("Super")) {
				p.sendMessage("You need to be a donator to wear this item.");
				return;
			}
		}
		for (int i = 0; i < p.premItems.length; i++) {
			if (buttonId3 == p.premItems[i] && p.getRights() < 2 && !p.getGroup().equalsIgnoreCase("Premium") && !p.getGroup().equalsIgnoreCase("Super")) {
				p.sendMessage("You need to be atleast a premium donator to wear this item.");
				return;
			}
		}*/
		for (int i = 0; i < p.superItems.length; i++) {
			if (buttonId3 == p.superItems[i] && p.getRights() < 2 && !p.getGroup().equalsIgnoreCase("Super")) {
				p.sendMessage("You need to be a super donator to wear this item.");
				return;
			}
		}
		int targetSlot = Equipment.getItemType(buttonId3);
		if (targetSlot == -1) {
			return;
		}
		if (targetSlot == 3) {
			player.dontRetaliate = true;
			World.getWorld().submit(new Tickable(5) {
				public void execute() {
					player.dontRetaliate = false;
					this.stop();
				}
			});
			player.getSettings().setUsingSpecial(false);
			ActionSender.sendConfig(player, 301, 0);
			if (item != null)
				player.resetCombat();
			if (p.getAttribute("autoCastSpell") != null) {
				p.setAttribute("autoCastSpell", null);
				ActionSender.sendConfig(p, 108, -1);
			}
		}
		if (Equipment.isTwoHanded(item.getDefinition()) && p.getInventory().getFreeSlots() < 1 && p.getEquipment().get(5) != null) {
			ActionSender.sendChatMessage(p, (byte) 0, "Not enough free space in your inventory.");
			return;
		}
		if (Equipment.isTwoHanded(item.getDefinition()) && p.getInventory().getFreeSlots() < 1 && p.getEquipment().get(5) != null) {
			ActionSender.sendChatMessage(p, (byte) 0,
			"Not enough free space in your inventory.");
			return;
		}
		boolean hasReq = true;
		if (item.getDefinition().getSkillRequirementId() != null) {
			for (int skillIndex = 0; skillIndex < item.getDefinition().getSkillRequirementId().size(); skillIndex++) {
				int reqId = item.getDefinition().getSkillRequirementId().get(skillIndex);
				int reqLvl = -1;
				if (item.getDefinition().getSkillRequirementLvl().size() > skillIndex)
					reqLvl = item.getDefinition().getSkillRequirementLvl().get(skillIndex);
				if (reqId > 25 || reqId < 0 || reqLvl < 0 || reqLvl > 120)
					continue;
				if (p.getSkills().getLevelForXp(reqId) < reqLvl) {
					if (hasReq)
						ActionSender.sendChatMessage(p, (byte) 0, "You are not high enough level to use this item.");
					ActionSender.sendChatMessage(p, (byte) 0, "You need to have a " + (Skills.SKILL_NAME[reqId].toLowerCase()) + " level of " + reqLvl + ".");
					hasReq = false;
				}
			}
		}
		if (!hasReq)
			return;
		p.getInventory().deleteItem(item.getDefinition().getId(),
				item.getAmount());
		if (targetSlot == 3) {
			if (Equipment.isTwoHanded(item.getDefinition()) && p.getEquipment().get(5) != null) {
				if (!p.getInventory().addItem(p.getEquipment().get(5).getDefinition().getId(), p.getEquipment().get(5).getAmount())) {
					p.getInventory().addItem(buttonId3, item.getAmount());
					return;
				}
				p.getEquipment().set(5, null);
			}
		} else if (targetSlot == 5) {

			if (p.getEquipment().get(3) != null && Equipment.isTwoHanded(p.getEquipment().get(3).getDefinition())) {
				if (!p.getInventory().addItem(p.getEquipment().get(3).getDefinition().getId(), p.getEquipment().get(3).getAmount())) {
					p.getInventory().addItem(buttonId3, item.getAmount());
					return;
				}
				p.getEquipment().set(3, null);
			}

		}
		if (p.getEquipment().get(targetSlot) != null && (buttonId3 != p.getEquipment().get(targetSlot).getDefinition().getId() || !item.getDefinition().isStackable())) {
			if(p.getInventory().get(buttonId2) == null) {
				p.getInventory().set(buttonId2,	p.getEquipment().get(targetSlot));
			} else {
				p.getInventory().getContainer().add(p.getEquipment().get(targetSlot));
			}
			p.getInventory().refresh();
			p.getEquipment().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (p.getEquipment().get(targetSlot) != null) {
			oldAmt = p.getEquipment().get(targetSlot).getAmount();
		}
		Item item2 = new Item(buttonId3, oldAmt + item.getAmount());
		p.getEquipment().set(targetSlot, item2);
	}

	public boolean usingRanged() {
		Item item = get(SLOT_WEAPON);
		if (item != null) {
			String name = item.getDefinition().getName().toLowerCase();
			if (name.contains("bow") || name.contains("dart")
					|| name.contains("knife") || name.contains("javelin")) {
				return true;
			}
		}
		return false;
	}

	public boolean barrowsSet(int setID) {
		Item hat = get(0), body = get(4), bottoms = get(7), weaponSlot = get(3);
		if (hat == null || body == null || bottoms == null || weaponSlot == null)
			return false;
		String helmet = hat.getDefinition().getName();
		String platebody = body.getDefinition().getName();
		String weapon = weaponSlot.getDefinition().getName();
		String platelegs = bottoms.getDefinition().getName();
		String set = "";
		switch (setID) {
		case 1:	//Ahrim's
			set = "Ahrim";
			break;
		case 2: //Dharok's
			set = "Dharok";
			break;
		case 3: //Guthan's
			set = "Guthan";
			break;
		case 4: //Karil's
			set = "Karil";
			break;
		case 5: //Torag's
			set = "Torag";
			break;
		case 6: //Verac's
			set = "Verac";
			break;
		}
		boolean hasHelmet = helmet.contains(set);
		boolean hasPlatebody = platebody.contains(set);
		boolean hasWeapon = weapon.contains(set);
		boolean hasPlatelegs = platelegs.contains(set);
		if (hasHelmet && hasPlatebody && hasWeapon && hasPlatelegs) {
			return true;
		}
		return false;
	}

	public int getSlot(int i) {
		if(get(i) == null) {
			return -1;
		}
		return get(i).getId();
	}

	public boolean voidSet(int setID) {
		String helmet = get(0) == null ? "" : get(0).getDefinition().getName();
		String set = "";
		switch (setID) {
		case 1: //Melee
			set = "Void melee";
			break;
		case 2: //Range
			set = "Void ranger";
			break;
		case 3: //Mage
			set = "Void mage";
			break;
		}
		boolean hasHelmet = helmet.contains(set);
		boolean hasTop = contains(8839);
		boolean hasGloves = contains(8842);
		boolean hasBottom = contains(8840);
		if (hasHelmet && hasTop && hasGloves && hasBottom) {
			return true;
		}
		return false;
	}

	public void reset() {
		equipment.reset();
		refresh();
	}
}
