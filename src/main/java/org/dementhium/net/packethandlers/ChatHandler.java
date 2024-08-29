package org.dementhium.net.packethandlers;

import org.dementhium.content.Commands;
import org.dementhium.model.World;
import org.dementhium.model.player.ChatMessage;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public final class ChatHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 25:
			appendChat(player, packet);
			break;
		case 83:
			break;
		}
	}

	private void appendChat(Player player, Message packet) {
		if(player.cantTalk) {
			return;
		}
		int effects = packet.readUnsignedShort();
		int numChars = packet.readUnsignedByte();
		String username = Misc.formatPlayerNameForDisplay(player.getUsername());
		String text = TextUtils.decryptPlayerChat(packet, numChars);
		if(text.startsWith(":/")) {
			Commands.handle(player, text.substring(2));
			player.updateLog("[Command] "+username+": "+text, null);
			return;
		} else if(text.startsWith("::")) {
			Commands.handle(player, text.substring(2));
			player.updateLog("[Command] "+username+": "+text, null);
			return;
		}
		text = Misc.optimizeText(text);
		if(player.getSettings().getCurrentClan() != null) {
			if(text.startsWith("/") && !player.getConnection().isInLobby()) {
				text = Misc.optimizeText(text.substring(1));
				World.getWorld().getClanManager().sendClanMessage(player, text);
				player.updateLog("[Clan Chat] "+username+": "+text, null);
				return;
			} else if(player.getConnection().isInLobby()) {
				World.getWorld().getClanManager().sendClanMessage(player, text);
				player.updateLog("[Clan Chat] "+username+": "+text, null);
				return;
			}
		}
		player.getMask().setLastChatMessage(new ChatMessage(effects, numChars, text));
		player.updateLog("[Public Chat] "+username+": "+text, null);
	}

}
