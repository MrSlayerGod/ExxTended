package org.dementhium.model.definition;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dementhium.io.XMLHandler;
import org.dementhium.model.combat.Combat.CombatStyle;
import org.dementhium.model.combat.Combat.CombatType;
import org.dementhium.model.player.Player;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class WeaponInterface {

	private static final Map<Integer, WeaponInterface> weaponInterfaces = new HashMap<Integer, WeaponInterface>();

	public static void init() {
		try {
			List<WeaponInterface> weps = XMLHandler.fromXML("data/xml/weapon_interfaces.xml");
			for (WeaponInterface weapon : weps) {
				for (int i : weapon.getWeaponIds()) {
					weaponInterfaces.put(i, weapon);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static WeaponInterface forId(int id) {
		return weaponInterfaces.get(id);
	}

	public static int getConfig(Player player, WeaponInterface lastWeaponInterface) {
		if (lastWeaponInterface != null) {
			for (int i = 0; i < lastWeaponInterface.getButtons().size(); i++) {
				WeaponButton button = lastWeaponInterface.getButtons().get(i);
				if (player.getSettings().getCombatStyle() == button.getCombatStyle() && player.getSettings().getCombatType() == button.getCombatType())
					return i;
			}
		}
		return 4; // None set
	}

	private List<WeaponButton> buttons;
	private int[] weaponIds;

	public List<WeaponButton> getButtons() {
		return buttons;
	}

	public int[] getWeaponIds() {
		return weaponIds;
	}

	public static class WeaponButton {

		public WeaponButton() {
		}

		private CombatStyle combatStyle;
		private CombatType combatType;

		public CombatStyle getCombatStyle() {
			return combatStyle;
		}

		public CombatType getCombatType() {
			return combatType;
		}
	}
}
