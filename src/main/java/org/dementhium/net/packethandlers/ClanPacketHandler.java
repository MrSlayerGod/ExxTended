package org.dementhium.net.packethandlers;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class ClanPacketHandler extends PacketHandler {

	public static final int JOIN = 8;
	
	@Override
	public void handlePacket(Player player, Message packet) {
		switch(packet.getOpcode()) {
		case JOIN:
			String owner = "";
			if(packet.remaining() > 0) {
				owner = packet.readRS2String();
			}
			if (owner.length() > 0) {
				World.getWorld().getClanManager().joinClan(player, owner);
			} else {
				World.getWorld().getClanManager().leaveClan(player);
				player.getSettings().setCurrentClan(null);
			}
			break;
		}
		
	}

}
