package org.dementhium.content.clans;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.event.Tickable;
import org.dementhium.io.XMLHandler;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 * @author 'Mystic Flow
 */
public class ClanManager {

	private Map<String, Clan> clans;

	public ClanManager() {
		System.out.println("Loading clans....");
		try {
			clans = XMLHandler.fromXML("data/xml/clans.xml");
		} catch (Exception e) {
			e.printStackTrace();
			clans = new HashMap<String, Clan>();
		}
		for (Map.Entry<String, Clan> entries : clans.entrySet()) {
			entries.getValue().setTransient();
		}
		System.out.println("Loaded " +clans.size()+ " clans.");
	}

	public Clan getClans(String s) {
		return clans.get(Misc.formatPlayerNameForProtocol(s));
	}
	public Map<String, Clan> getClans() {
		return clans;
	}

	public void createClan(Player p, String name) {
		if (name.equals("")) {
			return;
		}		
		String user = Misc.formatPlayerNameForProtocol(p.getUsername());
		//child 32 = coinshare below
		if (!clans.containsKey(user)) {
			Clan clan = new Clan(user, name);
			clans.put(Misc.formatPlayerNameForProtocol(p.getUsername()), clan);
			refresh(clan);
		} else {
			Clan clan = clans.get(user);
			clan.setName(name);
			refresh(clan);
		}
	}

	public void joinClan(final Player p, final String user) {
		p.sendMessage("Attempting to join channel...");
		final Clan clan = clans.get(Misc.formatPlayerNameForProtocol(user));
		if (clan == null) {
			World.getWorld().submit(new Tickable(1) {
				@Override
				public void execute() {
					p.sendMessage("Then channel you tried to join does not exist.");
					this.stop();
				}
			});
			return;
		}
		World.getWorld().submit(new Tickable(1) {
			@Override
			public void execute() {
				if(clan.canJoin(p)) {
					p.getSettings().setCurrentClan(clan);
					clan.addMember(p);
					refresh(clan);
					ActionSender.sendConfig(p,1083, clan.isLootsharing() ? 1 : 0);
					String ccName = Misc.formatPlayerNameForDisplay(clan.getName());
					p.sendMessage("Now talking in the clan channel " +ccName);
					p.sendMessage("To talk, start each line of chat with the / symbol.");
				} else {
					p.sendMessage("You don't have a high enough rank to join this channel.");
				}
				this.stop();
			}
		});
	}
	public void destroy(Player player, String username) {
		Clan c = clans.get(Misc.formatPlayerNameForProtocol(username));
		if (c != null) {
			for (Player p : c.getMembers()) {
				if (p == null) { 
					continue;
				}
				ClanPacket.sendClanList(p, null);
			}
		}
		clans.remove(username);
	}
	public void refresh(Clan clan) {
		for (Player p : clan.getMembers()) {
			if (p == null) {
				continue;
			}
			ClanPacket.sendClanList(p, clan);
		}
	}
	public void leaveClan(Player player) {
		Clan c = player.getSettings().getCurrentClan();
		if (c != null) {
			c.removeMember(player);
			refresh(c);
			ClanPacket.sendClanList(player, null);
		}
		ActionSender.sendConfig(player, 1083, 0);
	}

	public void rankMember(Player player, String user, int rank) {
		Clan c = clans.get(player.getUsername());
		if (c == null) {
			return;
		}
		c.rankUser(user, rank);
		refresh(c);
	}
	public String getClanName(String user) {
		Clan c = clans.get(user);
		if (c == null) {
			return "Chat disabled";
		} 
		return c.getName();
	}
	public void sendClanMessage(Player player, String text) {
		Clan c = player.getSettings().getCurrentClan();
		if (c == null) {
			return;
		}
		for (Player pl : c.getMembers()) {
			/*if (pl.getIndex() == player.getIndex()) {
				continue;
			}*/
			ClanMessage.sendClanChatMessage(player, pl, c.getName(), c.getOwner(), text);
		}
		//ClanMessage.sendClanChatMessage(player, null, c.getName(), c.getOwner(), text);
	}

	public void toggleLootshare(Player player) {
		Clan c = clans.get(player.getUsername());
		if(c != null) {
			c.toggleLootshare();
		} else {
			player.sendMessage("You don't have a clan to active lootshare with.");
		}
	}
}
