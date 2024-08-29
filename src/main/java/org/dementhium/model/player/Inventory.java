package org.dementhium.model.player;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.net.ActionSender;

/**
 * Manages the player inventory.
 *
 * @author Graham
 */
public class Inventory {

	public static final int SIZE = 28;

	private final Container inventory = new Container(SIZE, false);

	private final Player player;

	public Inventory(Player player) {
		this.player = player;
	}

	public boolean addItem(int item, int amount) {
		if (item < 0 || item > ItemDefinition.MAX_SIZE) {
			return false;
		}
		boolean b = inventory.add(new Item(item, amount));
		if (!b) {
			ActionSender.sendChatMessage(player, 0, "Not enough space in your inventory.");
			refresh();
			return false;
		}
		refresh();
		return true;
	}

	public boolean contains(int item, int amount) {
		return inventory.contains(new Item(item, amount));
	}

	public boolean contains(int item) {
		return inventory.containsOne(new Item(item));
	}

	public void deleteItem(int item, int amount) {
		inventory.remove(new Item(item, amount));
		refresh();
	}

	public void deleteAll(int item) {
		inventory.removeAll(new Item(item));
		refresh();
	}

	public void refresh() {
		ActionSender.sendItems(player, 93, inventory, false);
	}

	public Container getContainer() {
		return inventory;
	}

	public int getFreeSlots() {
		return inventory.getFreeSlots();
	}

	public boolean hasRoomFor(int id, int itemAmount) {
		if (ItemDefinition.forId(id).isStackable()) {
			return getFreeSlots() >= 1 || contains(id);
		} else {
			return getFreeSlots() >= itemAmount;
		}
	}

	public int numberOf(int id) {
		return inventory.getNumberOf(new Item(id, 1));
	}

	public Item lookup(int id) {
		return inventory.lookup(id);
	}

	public int lookupSlot(int id) {
		return inventory.lookupSlot(id);
	}

	public Item get(int slot) {
		return inventory.get(slot);
	}

	public void set(int slot, Item item) {
		inventory.set(slot, item);
	}

	public void deleteItem(int id, int amount, int slot) {
		inventory.remove(slot, new Item(id, amount));
		refresh();
	}

	public void reset() {
		inventory.reset();
		refresh();
	}

}
