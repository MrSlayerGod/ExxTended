package org.dementhium.model;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.event.Tickable;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author Stephen
 */
public class GroundItemManager {

	public static final int DEFAULT_DELAY = 60;
	private List<GroundItem> groundItems = new ArrayList<GroundItem>();

	public void sendGlobalGroundItem(GroundItem item, boolean unique) {
		checkStacking(null, item);
		groundItems.add(item);
		for (Player pl : Region.getLocalPlayers(item.getLocation())) {
			ActionSender.sendGroundItem(pl, item.getLocation(), item, unique);
		}
	}

	/**
	 * Delay is usually 60.
	 */
	public void sendDelayedGlobalGroundItem(int ticks, final GroundItem item, final boolean unique) {
		checkStacking(item.getDropper(), item);
		ActionSender.sendGroundItem(item.getDropper(), item.getLocation(), item, unique);
		groundItems.add(item);
		World.getWorld().submit(new Tickable(ticks) {
			int timesThrough = 1;
			@Override
			public void execute() {
				if (timesThrough == 1) {
					if (worldContainsGroundItem(item.getId(), item.getLocation())) {
						item.setGlobal(true);
					}
					timesThrough++;
				} else if (timesThrough == 2) {
					if (item != null) {
						if (worldContainsGroundItem(item.getId(), item.getLocation())) {
							removeGlobalItem(item);
						}
					}
					this.stop();
				}
			}

		});
	}

	private void checkStacking(Player player, GroundItem item) {
		if (getGroundItemAtPos(item.getId(), item.getLocation()) != null) {
			GroundItem itemToDelete = getGroundItemAtPos(item.getId(), item.getLocation());
			if (itemToDelete.getDefinition().isStackable() || itemToDelete.getDefinition().isNoted() && (itemToDelete.isGlobal() || (item.getDropper() != null ? item.getDropper().equals(player) : false))) {
				removeGlobalItem(itemToDelete.getLocation().getX(), itemToDelete.getLocation().getY(), itemToDelete.getLocation().getZ(), itemToDelete.getId());
				item.setAmount(item.getAmount() + itemToDelete.getAmount());
			}
		}
	}

	public void removeGlobalItem(int x, int y, int z, int itemId) {
		GroundItem item = getGroundItemAtPos(itemId, Location.create(x, y, z));
		for (Player p : Region.getLocalPlayers(item.getLocation())) {
			if(p == item.getDropper()) {
				ActionSender.removeGroundItem(p, item);
			}
		}
		groundItems.remove(item);
	}

	public void removeGlobalItem(GroundItem item) {
		removeGlobalItem(item.getLocation().getX(), item.getLocation().getY(), 0, item.getId());
	}

	public boolean worldContainsGroundItem(int itemId, Location pos) {
		for (GroundItem g : groundItems) {
			if (g.getLocation().getX() == pos.getX() && g.getLocation().getY() == pos.getY() && g.getLocation().getZ() == pos.getZ() && itemId == g.getId()) {
				return true;
			}
		}
		return false;
	}

	public GroundItem getGroundItemAtPos(int itemId, Location pos) {
		for (GroundItem g : groundItems) {
			if (g.getLocation().getX() == pos.getX() && g.getLocation().getY() == pos.getY() && g.getLocation().getZ() == pos.getZ() && itemId == g.getId()) {
				return g;
			}
		}
		return null;
	}
	
	
	public void refreshItems(Player p) {
		for (GroundItem g : Region.getLocalItems(p.getLocation())) {
			if (g.isGlobal() && g.getDropper() == p) {
				ActionSender.sendGroundItem(p, g.getLocation(), g, false);
			}
		}
	}
	
	public void removeItems(Player p) {
		for (GroundItem g : Region.getLocalItems(p.getLocation())) {
			if (g.isGlobal() && g.getDropper() == p) {
				ActionSender.removeGroundItem(p, g);
			}
		}
	}

	public class GroundItem extends Entity {

		private Item item;
		private boolean isGlobal = false;
		private final Player dropper;

		public GroundItem(Player dropper, int id, int amount, Location loc) {
			this(dropper, new Item(id, amount), loc);
		}

		public Player getDropper() {
			return dropper;
		}

		public GroundItem(Player dropper, Item item, Location loc) {
			this.item = item;
			setLocation(loc);
			this.dropper = dropper;
		}

		public int getId() {
			return item.getId();
		}

		public int getAmount() {
			return item.getAmount();
		}

		public void setAmount(int amount) {
			item.setAmount(amount);
		}

		public ItemDefinition getDefinition() {
			return item.getDefinition();
		}

		public Item getItem() {
			return item;
		}

		@Override
		public boolean isGroundItem() {
			return true;
		}

		@Override
		public GroundItem getGroundItem() {
			return this;
		}
		
		public void setGlobal(boolean b) {
			this.isGlobal = b;
		}
		
		public boolean isGlobal() {
			return isGlobal;
		}

	}

	public GroundItem create(Player p, Item item, Location location) {
		return new GroundItem(p, item, location);
	}


}
