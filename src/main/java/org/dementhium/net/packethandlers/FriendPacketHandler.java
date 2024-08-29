package org.dementhium.net.packethandlers;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class FriendPacketHandler extends PacketHandler {

	public static final int ADD_FRIEND = 75, REMOVE_FRIEND = 27;

	public static final int SEND_PRIVATE_MESSAGE = 54;

	@Override
	public void handlePacket(Player player, Message packet) {
		switch(packet.getOpcode()) {
		case ADD_FRIEND:
			player.getFriendManager().addFriend(packet.readRS2String());
			break;
		case REMOVE_FRIEND:
			player.getFriendManager().removeFriend(packet.readRS2String());
			break;
		case SEND_PRIVATE_MESSAGE:
			String otherName = Misc.formatPlayerNameForDisplay(packet.readRS2String());
			if (otherName == null || otherName.equals(player.getUsername())) {
				//System.out.println("Null :( - otherName");
				return;
			}
			int numChars = packet.readUnsignedByte();
			String outMessage = TextUtils.decompressHuffman(packet, numChars);
			if (outMessage == null) {
				//System.out.println("Null :( - outMessage");
				return;
			}
			outMessage = Misc.optimizeText(outMessage);
			Player sendPlayer = null;
			for(Player p2 : World.getWorld().getPlayers()) {
				if (Misc.formatPlayerNameForDisplay(p2.getUsername()).equalsIgnoreCase(otherName)) {
					//System.out.println("Username: "+Misc.formatPlayerNameForDisplay(p2.getUsername())+" otherName: "+otherName);
					sendPlayer = p2;
					break;
				}
			}
			if(sendPlayer == null) {
				for (Player p2 : World.getWorld().getLobbyPlayers()) {
					if (Misc.formatPlayerNameForDisplay(p2.getUsername()).equalsIgnoreCase(otherName)) {
						//System.out.println("Username: "+Misc.formatPlayerNameForDisplay(p2.getUsername())+" otherName: "+otherName);
						sendPlayer = p2;
						break;
					}
				}
			}
			if(sendPlayer != null) {
				ActionSender.sendPrivateMessage(player, otherName, outMessage);
				ActionSender.receivePrivateMessage(sendPlayer, Misc.formatPlayerNameForDisplay(player.getUsername()), Misc.formatPlayerNameForDisplay(player.getUsername()), player.getRights(), outMessage);
				return;
			} else {
				player.sendMessage("That player is not available at the moment.");
				//System.out.println("Null, player not available.");
			}
			break;
		}
	}

}
