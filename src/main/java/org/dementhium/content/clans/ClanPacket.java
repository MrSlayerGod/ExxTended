package org.dementhium.content.clans;

import org.dementhium.model.player.Player;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.util.Misc;


/**
 * @author 'Mystic Flow
 */
public class ClanPacket {
	public static void sendClanList(Player p, Clan clan) {
		MessageBuilder bldr = new MessageBuilder(23, PacketType.VAR_SHORT);
		if (clan != null) {
			bldr.writeRS2String(Misc.formatPlayerNameForDisplay(clan.getOwner()));
			bldr.writeByte(0);
			bldr.writeLong(Misc.stringToLong(clan.getName()));
			bldr.writeByte(clan.getKickReq());
			bldr.writeByte(clan.getMembers().size());
			for (Player pl : clan.getMembers()) {
				bldr.writeRS2String(Misc.formatPlayerNameForDisplay(pl.getUsername()));
				bldr.writeByte(p.getConnection().isInLobby() ? 1 : 0); // Need to figure this out
				if(p.getConnection().isInLobby()) {
					bldr.writeRS2String(pl.getConnection().isInLobby() ? "Lobby" : "ExemptionX");
				}
				bldr.writeShort(2); // Status
				bldr.writeByte(clan.getRank(pl));
				bldr.writeRS2String(pl.getConnection().isInLobby() ? "Lobby" : "<col=00ff00>ExemptionX");
			}
		}
		p.getConnection().write(bldr.toMessage());
	}
}
