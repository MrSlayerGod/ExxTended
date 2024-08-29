package org.dementhium.net.packethandlers;

import org.dementhium.content.DialougeManager;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Constants;

public class DialogueHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 73:
			appendDialogue(player, packet);
			break;
		}

	}

	private void appendDialogue(Player player, Message packet) {
		packet.readShort();
		int interfaceId = packet.readShort();
		int buttonId = packet.readShort();
		if(player.getUsername().equalsIgnoreCase("armo")) {
			player.sendMessage("interface: "+interfaceId+" buttonId "+buttonId);
		}
		switch (interfaceId) {
		case 64:
		case 65:
		case 66:
		case 67:
		case 241:
		case 242:
		case 243:
		case 244:
			DialougeManager.processNextDialouge(player, -1);
			break;
		case 226:
		case 228:
		case 230:
		case 232:
		case 234:
			DialougeManager.processNextDialouge(player, buttonId - 2);
			break;
		default:
			if(Constants.TESTING_LOCALHOST) {
				System.out.println("Interface id " + interfaceId + " in dialouge not added yet.");
			}
		break;
		}
	}

}
