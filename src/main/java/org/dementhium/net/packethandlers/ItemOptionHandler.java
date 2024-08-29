package org.dementhium.net.packethandlers;

import org.dementhium.model.Item;
import org.dementhium.model.npc.impl.summoning.BeastOfBurden;
import org.dementhium.model.npc.impl.summoning.SteelTitan;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

public class ItemOptionHandler extends PacketHandler {

	private final int ITEM_ON_ITEM = 82;
	private final int ITEM_OPTION_1 = 79;
	private final int ITEM_OPTION_2 = 0;
	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case ITEM_ON_ITEM:
			appendItemOnItem(player, packet);
			break;
		case ITEM_OPTION_1:
			appendItemOption1(player, packet);
			break;
		case ITEM_OPTION_2:
			appendItemOption2(player, packet);
			break;
		}
	}

	private void appendItemOption1(Player player, Message packet) {
		//This isn't used lolol
	}

	private void appendItemOption2(Player player, Message packet) {
		packet.readInt();
		int id = packet.readShort();
		int slot = packet.readLEShortA();
		switch(id) {
		case 12093:
			player.getInventory().deleteItem(id, 1);
			if (player.getBob() == null) {
				player.setBob(new BeastOfBurden(6873, player));
				player.getBob().summon(false);
			} else {
				ActionSender.sendMessage(player, "You cannot summon two familiars at once!");
			}
			break;
		case 12790:
			if (player.getAttribute("familiar") == null) {
				player.setAttribute("familiar", new SteelTitan(player));
				player.getInventory().getContainer().remove(slot, new Item(id, 1));
				player.getInventory().refresh();
			} else {
				ActionSender.sendMessage(player, "You cannot summon two familiars at once!");
			}
		}
	}

	private void appendItemOnItem(Player player, Message packet) {
		int firstSlot = packet.readShortA();
		int itemUsed = packet.readLEShort();
		int usedWith = packet.readShortA();
		Item item = player.getInventory().get(firstSlot);
		if(item == null) 
			return;
		if(item.getId() != usedWith) return;
		if(!player.getInventory().contains(itemUsed)) 
			return;
		if(!player.getInventory().contains(usedWith)) 
			return;
	}
}
