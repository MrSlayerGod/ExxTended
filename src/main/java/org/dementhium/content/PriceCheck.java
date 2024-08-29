package org.dementhium.content;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * 
 * @author Khaled
 * 
 */
public class PriceCheck {

	public static int PRICE_CHECK_SIZE = 40;
	private Player player;
	private Container pc = new Container(PRICE_CHECK_SIZE, false);

	public PriceCheck(Player p) {
		player = p;
	}
	/**
	 * Fires up the PriceCheck. Sending required things.
	 */
	public void execute() {
		Object[] objects = new Object[] { "", "", "", "", "Add-X", "Add-All",
				"Add-10", "Add-5", "Add", -1, 1, 7, 4, 93, 13565952 };
		ActionSender.sendClientScript(player, 150, objects, "IviiiIsssssssss");
		ActionSender.sendAMask(player, 0, 207, 27, 0, 2360382);
		ActionSender.sendItems(player, 90, pc, false);
		ActionSender.sendAMask(player, 18, 206, 54, 0, 1086);
		ActionSender.sendInterface(player, 206);
		ActionSender.sendInventoryInterface(player, 207);
		ActionSender.sendItems(player, 93, player.getInventory().getContainer(), false);
		ActionSender.sendAMask(player, 0, 27, 336, 0, 0, 254);
	}

	public void addItem(int slot) {
		Item item = player.getInventory().getContainer().get(slot);
		pc.add(item);
		player.getInventory().deleteItem(item.getId(), item.getAmount());
		refresh();
	}

	public void removeItem(int slot) {
		Item item = pc.get(slot);
		pc.remove(item);
		player.getInventory().addItem(item.getId(), item.getAmount());
		refresh();
	}

	public void refresh() {
		ActionSender.sendItems(player, 90, pc, false);
		ActionSender.sendItems(player, 93, player.getInventory().getContainer(),
				false);
	}

}
