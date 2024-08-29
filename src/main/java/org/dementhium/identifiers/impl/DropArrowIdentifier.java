package org.dementhium.identifiers.impl;

import org.dementhium.identifiers.Identifier;
import org.dementhium.model.Mob;
import org.dementhium.model.GroundItemManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.util.Misc;

/**
 *
 * @author 'Mystic Flow
 *
 */
public class DropArrowIdentifier extends Identifier {

	@Override
	public void identify(Object... args) {
		Player player = (Player) args[0];
		Mob victim = (Mob) args[1];
		if(player.getEquipment().get(Equipment.SLOT_ARROWS) != null && player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			int arrow = player.getEquipment().get(Equipment.SLOT_ARROWS).getId();
			if(player.itemName("crystal") || player.itemName("javelin") || player.itemName2("Zaryte")) {
				return;
			}
			if (arrow > 0) {
				player.getEquipment().deleteItem(arrow, 1);
				if(Misc.random(1) == 0) {
					World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(player, new Item(arrow, 1), victim.getLocation()), true);
				}
			}
		}
	}

}
