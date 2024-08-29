package org.dementhium.net.packethandlers;

import org.dementhium.action.Action;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.model.player.Bank;

/**
 * @author Steve
 */
public class GroundItemActionHandler extends PacketHandler {

	private static final int PICKUP_ITEM = 29, EXAMINE_ITEM = 31;

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case PICKUP_ITEM:
			handlePickupItem(player, packet);
			break;
		case EXAMINE_ITEM:
			handleExamineItem(player, packet);
			break;
		}

	}

	private void handleExamineItem(Player player, Message packet) {

	}

	private void handlePickupItem(final Player player, Message packet) {
		if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
			if((Integer) player.getAttribute("bankScreen", 1) == 1){
				player.getBank().setHasEnteredPin(false);
			}
			Bank.resetPinScreen(player);
			ActionSender.closeInter(player);
			ActionSender.closeInventoryInterface(player);
			//ActionSender.sendLoginMasks(player);
		}
		packet.readByteC();
		final int x = packet.readShortA();
		final int itemId = packet.readLEShort();
		final int y = packet.readLEShortA();
		//System.out.println(x + ", " + y);
		final Location pos = Location.create(x, y, player.getLocation().getZ());
		if (World.getWorld().getGroundItemManager().worldContainsGroundItem(itemId, pos)) { //Stop multiple pickups on the same item
			World.getWorld().doPath(new AStarPathFinder(), player, x, y);
			player.getActionManager().clearActions();
			player.setCoordinateEvent(new CoordinateEvent(player, x, y) {
				@Override
				public void execute() {
					pickup(player, itemId, pos, x, y, player.getLocation().getZ());
				}
			});
		}
	}

	public void pickup(Player player, int itemId, Location loc, int x, int y, int z) {
		try {
			if (World.getWorld().getGroundItemManager().worldContainsGroundItem(itemId, loc)) {
				if(player.getInventory().getFreeSlots() <= 0) {
					ActionSender.sendMessage(player, "Not enough space in your inventory.");
					return;
				}
				player.getInventory().getContainer().add(World.getWorld().getGroundItemManager().getGroundItemAtPos(itemId, loc).getItem());
				player.getInventory().refresh();
				World.getWorld().getGroundItemManager().removeGlobalItem(x, y, z, itemId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
