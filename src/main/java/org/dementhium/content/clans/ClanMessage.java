package org.dementhium.content.clans;

import java.util.Random;

import org.dementhium.model.player.Player;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;


/**
 * @author 'Mystic Flow
 */
public class ClanMessage {

	public static int messageCounter = 1;
	public static final Random r = new Random();
	public static int id = 0;
	
	public static void sendClanChatMessage(Player from, Player pl, String roomName, String user, String message) {
		int messageCounter = getNextUniqueId();
		MessageBuilder bldr = new MessageBuilder(64, PacketType.VAR_BYTE);
		bldr.writeByte(0);
		bldr.writeRS2String(Misc.formatPlayerNameForDisplay((from.getUsername())));
		bldr.writeLong(Misc.stringToLong(roomName));
		bldr.writeShort(r.nextInt());
		byte[] bytes = new byte[256];
		bytes[0] = (byte) message.length();
		int len = 1 + TextUtils.huffmanCompress(message, bytes, 1);
		bldr.writeMediumInt(messageCounter);
		bldr.writeByte((byte) from.getRights());
		bldr.writeBytes(bytes, 0, len);
		if (pl != null)
			pl.getConnection().write(bldr.toMessage());
		else 
			from.getConnection().write(bldr.toMessage());
		//TODO
	}

	public static int getNextUniqueId() {
		if (messageCounter >= 16000000) {
			messageCounter = 0;
		}
		return messageCounter++;
	}
}
