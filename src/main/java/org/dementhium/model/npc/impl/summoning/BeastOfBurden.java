package org.dementhium.model.npc.impl.summoning;

import org.dementhium.content.misc.Following;
import org.dementhium.model.Container;
import org.dementhium.model.GroundItemManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author Steve
 *
 */
public class BeastOfBurden extends NPC {

	private Player player;

	private BobInventory bobInv;

	public BeastOfBurden(int id, Player owner) {
		super(id);
		this.player = owner;
		this.bobInv = new BobInventory(owner);
		callToPlayer();
	}

	@Override
	public void tick() {
		if (!player.isOnline()) {
			dismiss();
			return;
		}
		turnTo(player);
		if (getLocation().getDistance(player.getLocation()) < this.size() || getLocation().getDistance(player.getLocation()) > this.size()) {
			if (getLocation().getDistance(player.getLocation()) >= 15) {
				callToPlayer();
				return;
			}
			Following.familiarFollow(this, player);
		}
	}

	public Item get(int slot) {
		return bobInv.getContainer().get(slot);
	}

	public void open() {
		bobInv.openBoB();
	}

	public void summon(boolean vanish) {
		boolean fullScreen = player.getConnection().getDisplayMode() == 2;
		ActionSender.sendConfig(player, 448, getId());
		ActionSender.sendConfig(player, 1174, getId());
		ActionSender.sendConfig(player,1175, 102025930);
		ActionSender.sendConfig(player, 1175, 102025930);
		ActionSender.sendConfig(player, 1171, 20480);
		ActionSender.sendConfig(player, 1171, 20480);
		ActionSender.sendConfig(player, 1176, 7424);
		ActionSender.sendConfig(player, 1801, 48);
		ActionSender.sendConfig(player, 1231, 333839);
		ActionSender.sendConfig(player, 1160, getHeadAnimConfig(getId()));
		ActionSender.sendConfig(player, 1175, 102025930);
		ActionSender.sendConfig(player, 1175, 102025930);
		ActionSender.sendConfig(player,1160, getHeadAnimConfig(getId()));
		ActionSender.sendConfig(player, 108, 0);
		ActionSender.sendInterface(player,1, fullScreen ? 746 : 548, 51, 662);
		ActionSender.sendInterface(player, 1, fullScreen ? 746 : 548, 34, 884);
		ActionSender.sendAMask(player, -1, -1, fullScreen ? 746 : 548, 125, 0, 2);
		ActionSender.sendAMask(player, -1, -1, 884, 11, 0, 2);
		ActionSender.sendAMask(player, -1, -1, 884, 12, 0, 2);
		ActionSender.sendAMask(player, -1, -1, 884, 13, 0, 2);
		ActionSender.sendConfig(player, 1175, 102025930);
		ActionSender.sendConfig(player, 1175, 102025930);
		ActionSender.sendInterface(player, 1, fullScreen ? 746 : 548, 51, 662);
		World.getWorld().getNpcs().add(this);
		turnTo(player);
	}

	public void putItem(int slot, int amount) {
		Item item = player.getInventory().get(slot);
		if(item.getDefinition().isNoted() || item.getDefinition().isStackable()) {
			player.sendMessage("You can't add stackable or noted items to your beast of burden!");
			return;
		}
		bobInv.addItem(item.getId(), amount);
	}

	public int getMaxSize() {
		switch(getId()) {
		case 6873:
			return 30;
		}
		return 0;
	}

	public void refresh() {
		bobInv.refresh();
	}

	public void removeItem(int slot) {
		bobInv.removeItem(slot, 1);
	}

	public void removeItem(int slot, int amount) {
		bobInv.removeItem(slot, amount);
	}

	public int numberOf(int id) {
		return bobInv.getContainer().getNumberOff(id);
	}

	public void removeAll() {
		player.getInventory().getContainer().addAll(bobInv.getContainer());
		bobInv.getContainer().clear();
		bobInv.refresh();
		player.getInventory().refresh();
	}

	public static int getHeadAnimConfig(int npcId) {
		switch(npcId) {
		case 6873://yak
			return 8388608;
		}
		return -1;
	}

	public Container getContainer() {
		return bobInv.getContainer();
	}

	public void take() {
		int slotsLeft = player.getInventory().getFreeSlots();
		for (int i = 0; i < slotsLeft; i++) {
			bobInv.removeItem(0);
		}
	}

	public void callToPlayer() {
		teleport(player.getLocation().getX() - this.size(), player.getLocation().getY() - this.size(), player.getLocation().getZ());
		turnTo(player);
		graphics(1314);
	}

	public void dismiss() {
		resetTurnTo();
		Region.getLocalNPCs(player.getLocation()).remove(this);
		World.getWorld().getNpcs().remove(this);
		player.setBob(null);
		dropBobsItems();
	}

	private void dropBobsItems() {
		for (Item i : bobInv.getContainer().getItems()) {
			if (i != null) {
				World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(player, i, getLocation()), false);
			}
		}
		
	}
}