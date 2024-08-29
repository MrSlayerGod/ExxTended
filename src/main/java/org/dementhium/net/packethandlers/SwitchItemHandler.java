package org.dementhium.net.packethandlers;

import org.dementhium.model.Item;
import org.dementhium.model.player.Bank;
import org.dementhium.model.player.Inventory;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
@SuppressWarnings("unused")
public class SwitchItemHandler extends PacketHandler {

	public static final int SWITCH_ITEMS = 10;

	@Override
	public void handlePacket(Player player, Message packet) {
		switch(packet.getOpcode()) {
		case SWITCH_ITEMS:
			switchItems(player, packet);
			break;
		}
	}

	public void switchItems(Player player, Message packet) {
		int fromInterfaceHash = packet.readInt();
		int fromInterfaceId = fromInterfaceHash >> 16;
		int toItemId = packet.readShort();
		int toId = packet.readShortA();
		int toInterfaceHash = packet.readLEInt();
		int toInterfaceId = toInterfaceHash >> 16;
		int tabId = (toInterfaceHash & 0xFF);
		int fromItemId = packet.readLEShortA();
		int fromId = packet.readLEShort();
		int tabIndex = player.getBank().getArrayIndex(tabId);
		int fromTab;
		switch(fromInterfaceId) {
		case 762:
			/*
			 * Bank.
			 */
			if(tabId == 92) {
				if(fromId < 0 || fromId >= Bank.SIZE || toId < 0 || toId >= Bank.SIZE) {
					break;
				}
				if (!isInserting(player)) {
					Item temp  = player.getBank().getContainer().get(fromId);
					Item temp2 = player.getBank().getContainer().get(toId);
					player.getBank().getContainer().set(fromId, temp2);
					player.getBank().getContainer().set(toId, temp);
					player.getBank().refresh();
				} else {
					if(toId > fromId) {
						player.getBank().insert(fromId, toId - 1);
					} else if(fromId > toId) {
						player.getBank().insert(fromId, toId);
					}
					player.getBank().refresh();
				}
				break;
			} else {
				if(tabIndex > -1) {
					toId = tabIndex == 10 ? player.getBank().getContainer().getFreeSlot() : player.getBank().getTab()[tabIndex] + player.getBank().getItemsInTab(tabIndex);
					fromTab = player.getBank().getTabByItemSlot(fromId);
					if(toId > fromId) {
						player.getBank().insert(fromId, toId - 1);
					} else if(fromId > toId) {
						player.getBank().insert(fromId, toId);
					}
					player.getBank().increaseTabStartSlots(tabIndex);
					player.getBank().decreaseTabStartSlots(fromTab);
					player.getBank().refresh();
					player.getBank().sendTabConfig();
					break;
				}
			}
			break;
		case 149:
			switch (toInterfaceId) {
			case 149:
				toId -= 28;
				if (fromId < 0 || fromId >= Inventory.SIZE || player.getInventory().getContainer().get(fromId) == null) {
					player.getInventory().refresh();
					return;
				}
				if (toId < 0 || fromId >= Inventory.SIZE) {
					player.getInventory().refresh();
					return;
				}
				Item toSlotItem = player.getInventory().getContainer().get(toId);
				player.getInventory().getContainer().set(toId,player.getInventory().getContainer().get(fromId));
				player.getInventory().getContainer().set(fromId, toSlotItem);
				player.getInventory().refresh();
				break;
			}
			break;
		}
	}

	private boolean isInserting(Player player) {
		return (Boolean) player.getAttribute("inserting", Boolean.FALSE);
	}
}
