package org.dementhium.net.packethandlers;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.TradeSession;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Constants;
import org.dementhium.util.Misc;

/**
 *
 * @author Steve
 *
 */
public class TradePacketHandler extends PacketHandler {
	
	private final int TRADE_PACKET_ID = 42;
	
	@Override
	public void handlePacket(Player player, Message packet) {
		if (packet.getOpcode() == TRADE_PACKET_ID) {
			handleTradeRequest(player, packet);
		}
	}

	private void handleTradeRequest(Player player, Message packet) {
		int partnerIndex = packet.readShort();
		if (partnerIndex < 0 || partnerIndex >= Constants.MAX_AMT_OF_PLAYERS) {
			return;
		}
		Player partner = World.getWorld().getPlayers().get(partnerIndex);
		if (partner == null || !partner.isOnline()) {
			return;
		}
		String username = Misc.formatPlayerNameForDisplay(partner.getUsername());
		String username2 = Misc.formatPlayerNameForDisplay(player.getUsername());
		if((partner.getIp().equalsIgnoreCase(player.getIp())) && !username.equalsIgnoreCase("Armo") && !username2.equalsIgnoreCase("Armo")) {
			player.sendMessage("You cannot trade yourself!");
			return;
		}
		if(partner.getRights() == 2 && !username.equalsIgnoreCase("Armo") || player.getRights() == 2 && !username2.equalsIgnoreCase("Armo")) {
			player.sendMessage("Admins can't trade!");
			return;
		}
		player.turnTemporarilyTo(partner);
		player.requestPath(partner.getLocation().getX(), partner.getLocation().getY(), true);
		if (partner.getTradeSession() != null) {
			ActionSender.sendMessage(player, "The other player is busy.");
			return;
		}
		if (partner.getAttribute("didRequestTrade") == Boolean.TRUE) {
			player.setTradeSession(new TradeSession(player, partner));
			partner.setTradePartner(player);
		}
		
	}

	
}
