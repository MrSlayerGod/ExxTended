package org.dementhium.net.packethandlers;

import org.dementhium.content.Commands;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Misc;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public final class CommandHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		String username = Misc.formatPlayerNameForDisplay(player.getUsername());
		packet.readByte();//client command
		String command = packet.readJagString();
		if(player.getRights() != 2) {
			ActionSender.sendCrashPacket(player);
			return;
		}
		Commands.handle(player, command);
		player.updateLog("[Command Console] "+username+": "+command, null);
	}

}
