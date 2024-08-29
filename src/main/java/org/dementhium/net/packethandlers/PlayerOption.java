package org.dementhium.net.packethandlers;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.TradeSession;
import org.dementhium.model.player.DuelSession;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Constants;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.content.misc.Following;
import org.dementhium.util.Misc;

public class PlayerOption extends PacketHandler {

	public static final int PLAYER_ATTACK = 66;
	public static final int TRADE_PLAYER = 26; // RIGHT!
	public static final int TRADE_PLAYER_CLICK = 42; // CLICK!

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case PLAYER_ATTACK:
			attack(player, packet);
			break;

		case TRADE_PLAYER:
		case TRADE_PLAYER_CLICK:
			handleTradeRequest(player, packet);
			break;
		}
	}

	private void attack(Player player, Message packet) {
		int playerIndex = packet.readShortA();
		if (playerIndex < 0 || playerIndex >= Constants.MAX_AMT_OF_PLAYERS) {
			return;
		}
		Player victim = World.getWorld().getPlayers().get(playerIndex);
		if (victim == null || !victim.isOnline()) {
			return;
		}
		if (Constants.ANTI_FARM) {
			if (victim.getIp().equalsIgnoreCase(player.getIp())) {
				player.sendMessage("You cannot fight yourself!");
				return;
			}
		}
		//if(player.getRights() < 2) {
			player.turnTo(victim);
			player.getCombatState().setVictim(victim);
		//} else {
			//handleDuelRequest(player, packet, playerIndex);
		//}
	}

	private void handleTradeRequest(Player player, Message packet) {
		int partnerIndex = -1;
		if(packet.getOpcode() == 26) {
			packet.readByte();
			partnerIndex = packet.readLEShortA();
		} else if(packet.getOpcode() == 42) {
			partnerIndex = packet.readShort();
		}
		if (partnerIndex < 0 || partnerIndex >= Constants.MAX_AMT_OF_PLAYERS) {
			return;
		}
		Player partner = World.getWorld().getPlayers().get(partnerIndex);
		if (partner == null || !partner.isOnline()) {
			return;
		}
		String username = Misc.formatPlayerNameForDisplay(partner.getUsername());
		String username2 = Misc.formatPlayerNameForDisplay(player.getUsername());
		if(Constants.ANTI_MULE) {
			player.sendMessage("You cannot trade yourself!");
			return;
		}
		if(Constants.ADMIN_TRADE_ENABLED) {
			player.sendMessage("Admins can't trade!");
			return;
		}
		player.turnTo(partner);
		//player.requestPath(partner.getLocation().getX(), partner.getLocation().getY(), true);
		Following.combatFollow(player, partner);
		if (partner.getTradeSession() != null || partner.getTradePartner() != null) {
			ActionSender.sendMessage(player, "The other player is busy.");
			return;
		}
		if (partner.getAttribute("didRequestTrade") == Boolean.TRUE && partner.tradeString.equalsIgnoreCase(player.getUsername())) {
				if (player.getTradeSession() != null) {
					player.getTradeSession().tradeFailed();
				} else if (player.getTradePartner() != null) {
					player.getTradePartner().getTradeSession().tradeFailed();
				}
				if (player.getDuelSession() != null) {
					player.getDuelSession().duelFailed();
				} else if (player.getDuelPartner() != null) {
					player.getDuelPartner().getDuelSession().duelFailed();
				}
				player.setTradeSession(new TradeSession(player, partner));
				partner.setTradePartner(player);
		} else {
			ActionSender.sendMessage(player, "Sending trade request...");
			ActionSender.sendTradeReq(partner, player.getUsername(),
			"wishes to trade with you.");
			player.setAttribute("didRequestTrade", Boolean.TRUE);
			player.tradeString = partner.getUsername();
		}
	}

	private void handleDuelRequest(Player player, Message packet, int partnerIndex) {
		if (partnerIndex < 0 || partnerIndex >= Constants.MAX_AMT_OF_PLAYERS) {
			return;
		}
		Player partner = World.getWorld().getPlayers().get(partnerIndex);
		if (partner == null || !partner.isOnline()) {
			return;
		}
		if(Constants.ANTI_FARM) {
			player.sendMessage("You cannot trade yourself!");
			return;
		}
		/*if(partner.getRights() == 2 && !partner.getUsername().equalsIgnoreCase("armo") || player.getRights() == 2 && !player.getUsername().equalsIgnoreCase("armo")) {
			player.sendMessage("Admins can't trade!");
			return;
		}*/
		player.turnTo(partner);
		//player.requestPath(partner.getLocation().getX(), partner.getLocation().getY(), true);
		Following.combatFollow(player, partner);
		if (partner.getDuelSession() != null || partner.getDuelPartner() != null) {
			ActionSender.sendMessage(player, "The other player is busy.");
			return;
		}
		if (partner.getAttribute("didRequestDuel") == Boolean.TRUE && partner.duelString.equalsIgnoreCase(player.getUsername())) {
				if (player.getTradeSession() != null) {
					player.getTradeSession().tradeFailed();
				} else if (player.getTradePartner() != null) {
					player.getTradePartner().getTradeSession().tradeFailed();
				}	
				if (player.getDuelSession() != null) {
					player.getDuelSession().duelFailed();
				} else if (player.getDuelPartner() != null) {
					player.getDuelPartner().getDuelSession().duelFailed();
				}
				player.setDuelSession(new DuelSession(player, partner));
				partner.setDuelPartner(player);
		} else {
			ActionSender.sendMessage(player, "Sending duel request...");
			ActionSender.sendDuelReq(partner, player.getUsername(),
			"wishes to duel with you.");
			player.setAttribute("didRequestDuel", Boolean.TRUE);
			player.duelString = partner.getUsername();
		}
	}
}
