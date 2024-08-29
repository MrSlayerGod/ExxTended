package org.dementhium.net.packethandlers;

import org.dementhium.content.DialougeManager;
import org.dementhium.model.World;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.net.ActionSender;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.model.player.Bank;

public class NpcOption extends PacketHandler {

	private final int ATTACK = 36, OPTION_1 = 15, OPTION_2 = 50;

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case ATTACK:
			try {
				attack(player, packet);
			} catch(Exception e) {}
			break;
		case OPTION_1:
			try {
				option1(player, packet);
			} catch(Exception e) {}
			break;
		case OPTION_2:
			try {
				option2(player, packet);
			} catch(Exception e) {}
			break;
		}
	}

	private void option2(final Player player, Message packet) {
		if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
			if((Integer) player.getAttribute("bankScreen", 1) == 1){
				player.getBank().setHasEnteredPin(false);
			}
			Bank.resetPinScreen(player);
			ActionSender.closeInter(player);
			ActionSender.closeInventoryInterface(player);
			//ActionSender.sendLoginMasks(player);
		}
		packet.readByte();
		int index = packet.readShort();
		final NPC npc = World.getWorld().getNpcs().get(index);
		player.turnTo(npc);
		World.getWorld().doPath(new AStarPathFinder(), player, npc.getLocation().getX(), npc.getLocation().getY()-1);
		player.setCoordinateEvent(new CoordinateEvent(player, npc.getLocation().getX(), npc.getLocation().getY(), npc.size(), npc.size()) {
			@Override
			public void execute() {
				if (npc.equals(player.getBob())) {
					player.getBob().open();
				}
				switch(npc.getId()){
				case 494:
					player.getBank().openBank();
					break;
				case 3161:
					World.getWorld().getShopManager().openShop(player, 1);
					break;
				case 3163:
					World.getWorld().getShopManager().openShop(player, 2);
					break;
				}
			}
		});
	}

	private void option1(final Player player, Message in) {
		if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
			if((Integer) player.getAttribute("bankScreen", 1) == 1){
				player.getBank().setHasEnteredPin(false);
			}
			Bank.resetPinScreen(player);
			ActionSender.closeInter(player);
			ActionSender.closeInventoryInterface(player);
			//ActionSender.sendLoginMasks(player);
		}
		in.readByte();
		int index = in.readLEShort();
		final NPC npc = World.getWorld().getNpcs().get(index);
		final int id = npc.getId();
		player.turnTo(npc);
		World.getWorld().doPath(new AStarPathFinder(), player, npc.getLocation().getX(), npc.getLocation().getY()-1);
		player.setCoordinateEvent(new CoordinateEvent(player, npc.getLocation().getX(), npc.getLocation().getY(), npc.size(), npc.size()) {
			@Override
			public void execute() {
				if(DialougeManager.handle(player, npc)) {
					return;
				}
				switch(id) {
				case 494:
					player.getBank().openBank();
					break;
				case 3161:
					World.getWorld().getShopManager().openShop(player, 1);
					break;
				case 3163:
					World.getWorld().getShopManager().openShop(player, 2);
					break;
				}
			}
		});
	}

	private void attack(Player player, Message in) {
		if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
			if((Integer) player.getAttribute("bankScreen", 1) == 1){
				player.getBank().setHasEnteredPin(false);
			}
			Bank.resetPinScreen(player);
			ActionSender.closeInter(player);
			ActionSender.closeInventoryInterface(player);
			//ActionSender.sendLoginMasks(player);
		}
		in.readByte();
		int index = in.readLEShortA();
		final NPC npc = World.getWorld().getNpcs().get(index);
		if(npc.getId() != 1265) {
			player.sendMessage("They are not interested in fighting right now.");
			return;
		}
		player.turnTo(npc);
		player.getCombatState().setVictim(npc);
	}
}
