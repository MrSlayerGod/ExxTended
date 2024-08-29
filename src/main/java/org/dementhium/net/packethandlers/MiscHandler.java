package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Bank;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

public final class MiscHandler extends PacketHandler {

	public static final int MINIMIZED_OR_RESTORED = 4, CLICK = 34, KEY_PRESSED = 37, PING = 39, CAMERA_MOVED = 61, IDK = 68, IDK2 = 18, CLOSE = 59;

	@Override
	public void handlePacket(Player p, Message packet) {
		switch(packet.getOpcode()) {
		case CLICK:
			int position = packet.readShort();
			int interfaceId = packet.readShort();
			if(p.getUsername().equalsIgnoreCase("armo")) {
				//p.sendMessage(interfaceId+"");
			}
			break;
		case PING:
			break;
		}
	}
}
