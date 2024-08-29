package org.dementhium.model.npc.impl.summoning;

import java.util.ArrayList;
import java.util.Collections;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.player.Player;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.net.ActionSender;

public class BobInventory {

	public static int SIZE = 30;

	private final Container bob = new Container(SIZE, false);
	private final Player player;

	public static final Object[] depositOptions = new Object[]{"", "", "", "", "Store-X", "Store-All", "Store-10", "Store-5", "Store-1", -1, 0, 7, 4, 90, 665 << 16 | 0};
	public static final Object[] withdrawOptions = new Object[]{"", "", "", "", "Withdraw-X", "Withdraw-All", "Withdraw-10", "Withdraw-5", "Withdraw-1", -1, 0, 5, 6, 30, 671 << 16 | 27};

	public BobInventory(Player player) {
		this.player = player;
	}

	public void openBoB() {
		ActionSender.sendInterface(player, 671);
		ActionSender.sendInventoryInterface(player, 665);
		ActionSender.sendClientScript(player, 150, withdrawOptions, "IviiiIsssssssss");
		ActionSender.sendAMask(player, 1150, 671, 27, 0, 30);
		ActionSender.sendClientScript(player, 150, depositOptions, "IviiiIsssssssss");
		ActionSender.sendAMask(player, 1150, 665, 0, 0, 28);
		ActionSender.sendItems(player, 90, player.getInventory().getContainer(), false);
		ActionSender.sendItems(player, 30, getContainer(), false);
	}

	public void addItem(int item, int amount) {
		if (item < 0 || item > ItemDefinition.MAX_SIZE) {
			return;
		}
		if (player.getInventory().getContainer().getNumberOff(item) < amount) {
			amount = player.getInventory().getContainer().getNumberOff(item);
		}
		boolean b = bob.add(new Item(item, amount));
		if (!b) {
			ActionSender.sendChatMessage(player, 0, "Your beast of burden can't hold any more items.");
			refresh();
			return;
		} else {
			player.getInventory().deleteItem(item, amount);
		}
		refresh();
	}

	public void refresh() {
		ActionSender.sendItems(player, 90, player.getInventory().getContainer(), false);
		ActionSender.sendItems(player, 30, getContainer(), false);
	}

	public void removeItem(int slot, int amount) {
		if (slot < 0 || slot > BobInventory.SIZE || amount <= 0) {
			return;
		}
		Item item = bob.get(slot);
		Item item2 = bob.get(slot);
		Item item3 = bob.get(slot);
		if (item == null) {
			return;
		}
		if (amount > getContainer().getNumberOf(item)) {
			item = new Item(item.getId(), getContainer().getNumberOf(item));
			item2 = new Item(item.getId() + 1, getContainer().getNumberOf(item));
			item3 = new Item(item.getId(), getContainer().getNumberOf(item));
			if (noting()) {
				if (item2.getDefinition().isNoted() || !item.getDefinition().isStackable()) {
					item = new Item(item.getId() + 1, getContainer().getNumberOf(item));
				} else {
					player.sendMessage("You cannot withdraw this item as a note.");
					item = new Item(item.getId(), getContainer().getNumberOf(item));
				}
			}
		} else {
			item = new Item(item.getId(), amount);
			item2 = new Item(item.getId(), amount);
			item3 = new Item(item.getId(), amount);
			if (noting()) {
				item2 = new Item(item.getId() + 1, item.getAmount());
				if (item2.getDefinition().isNoted() || !item.getDefinition().isStackable()) {
					item = new Item(item.getId() + 1, item.getAmount());
				} else {
					player.sendMessage("You cannot withdraw this item as a note.");
					item = new Item(item.getId(), item.getAmount());
					return;
				}
			}
		}
		if (amount > player.getInventory().getFreeSlots() && !item3.getDefinition().isStackable() && !noting()) {
			item = new Item(item.getId(), player.getInventory().getFreeSlots());
			item2 = new Item(item2.getId(), player.getInventory().getFreeSlots());
			item3 = new Item(item3.getId(), player.getInventory().getFreeSlots());
		}
		if (bob.contains(item3)) {
			if (player.getInventory().getFreeSlots() <= 0) {
				player.sendMessage("Not enough space in your inventory.");
			} else {
				if (noting() && !item.getDefinition().isNoted()) {
					player.getInventory().addItem(item.getId(), item.getAmount());
					bob.remove(item3);
				} else {
					player.getInventory().addItem(item.getId(), item.getAmount());
					bob.remove(item3);
				}
			}
		}
		bob.shift();
		refresh();
	}

	public void removeItem(int slot) {
		if (slot < 0 || slot > BobInventory.SIZE) {
			return;
		}
		Item item = bob.get(slot);
		Item item2 = bob.get(slot);
		Item item3 = bob.get(slot);
		if (item == null) {
			return;
		}
		if (noting()) {
			item2 = new Item(item.getId() + 1, item.getAmount());
			if (item2.getDefinition().isNoted() || !item.getDefinition().isStackable()) {
				item = new Item(item.getId() + 1, item.getAmount());
			} else {
				player.sendMessage("You cannot withdraw this item as a note.");
				item = new Item(item.getId(), item.getAmount());
				return;
			}
		}
		if (bob.contains(item3)) {
			if (player.getInventory().getFreeSlots() <= 0 && item.getDefinition().isStackable() || player.getInventory().getFreeSlots() < item.getAmount() && !item.getDefinition().isStackable()) {
				player.sendMessage("Not enough space in your inventory.");
			} else {
				if (noting() && !item.getDefinition().isNoted()) {
					player.getInventory().addItem(item.getId(), item.getAmount());
					bob.remove(item3);
				} else {
					player.getInventory().addItem(item.getId(), item.getAmount());
					bob.remove(item3);
				}
			}
		}
		bob.shift();
		refresh();
	}

	public boolean noting() {
		return false;
	}

	public boolean contains(int item, int amount) {
		return bob.contains(new Item(item, amount));
	}

	public boolean contains(int item) {
		return bob.contains(new Item(item));
	}

	public Container getContainer() {
		return bob;
	}

	public Item get(int slot) {
		return bob.get(slot);
	}

	public void set(int slot, Item item) {
		bob.set(slot, item);
	}

	public void insert(int fromId, int toId) {
		Item temp = bob.getItems()[fromId];
		if(toId > fromId) {
			for(int i = fromId; i < toId; i++) {
				set(i, get(i+1));
			}
		} else if(fromId > toId) {
			for(int i = fromId; i > toId; i--) {
				set(i, get(i-1));
			}	
		}
		set(toId, temp);
	}

}
