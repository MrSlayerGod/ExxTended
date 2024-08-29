package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.InterfaceDecoder;

public class PaneSwitchHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 7:
			switchPanes(player, packet);
			break;
		}
	}

	private void switchPanes(Player player, Message packet) {
		int mode = packet.readByte();
		packet.readByte();
		if (!player.isOnline())
			return;
		if (mode == 2 && player.getConnection().getDisplayMode() != 2
				|| mode == 3 && player.getConnection().getDisplayMode() != 3) {
			InterfaceDecoder.switchWindow(player, mode);
		} else if (mode == 1 && player.getConnection().getDisplayMode() != 1
				|| mode == 0 && player.getConnection().getDisplayMode() != 0) {
			InterfaceDecoder.switchWindow(player, mode);
		}
	}
}
