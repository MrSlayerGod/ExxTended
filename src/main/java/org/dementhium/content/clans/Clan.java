package org.dementhium.content.clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;
/**
 * @author 'Mystic Flow
 */
public class Clan {

	private String roomName;
	private String roomOwner;
	private int joinReq = 0;
	private int talkReq = 0;
	private int kickReq = 7;
	private HashMap<String, Byte> ranks;
	private transient List<Player> members;
	private transient boolean lootsharing;

	public Clan(String owner, String name) {
		this.roomName = name;
		this.roomOwner = owner;
		setTransient();
	}

	public void setTransient() {
		setLootsharing(false);
		if(kickReq == 0) {
			kickReq = 7;
		}
		if (members == null) {
			this.members = new ArrayList<Player>();
		}
		if (ranks == null) {
			this.ranks = new HashMap<String, Byte>();
		}
	}

	public String getName() {
		return roomName;
	}
	public String getOwner() {
		return roomOwner;
	}
	public void rankUser(String name, int rank) {
		if (!ranks.containsKey(name)) {
			ranks.put(name, (byte) rank);
		} else {
			ranks.remove(name);
			ranks.put(name, (byte) rank); 
		}
	}
	
	public Byte getRank(Player player) {
		if (Misc.formatPlayerNameForProtocol(player.getUsername()).equals(roomOwner)) {
			return 7;
		} else if (player.getRights() == 2) {
			return 127;
		} else if (ranks.containsKey(player.getUsername())) {
			return ranks.get(player.getUsername());
		}
		return -1;
	}

	public boolean canJoin(Player player) {
		byte rank = 0;
		if(ranks.containsKey(player.getUsername())) {
			rank = ranks.get(player.getUsername());
		}
		return rank >= joinReq;
	}
	
	public boolean canTalk(Player player) {
		byte rank = 0;
		if(ranks.containsKey(player.getUsername())) {
			rank = ranks.get(player.getUsername());
		}
		return rank >= talkReq;
	}

	public void toggleLootshare() {
		lootsharing = !lootsharing;
		String message = "";
		if(lootsharing) {
			message = "Lootshare has been enabled.";
		} else {
			message = "Lootshare has been disabled.";
		}
		for(Player pl : members) {
			ActionSender.sendMessage(pl, message);
			ActionSender.sendConfig(pl, 1083, lootsharing ? 1 : 0);
		}
	}
	public void addMember(Player member) {
		members.add(member);
	}

	public void setName(String name) {
		this.roomName = name;
	}

	public List<Player> getMembers() {
		return members;
	}

	public void removeMember(Player player) {
		members.remove(player);
	}

	public HashMap<String, Byte> getRanks() {
		return ranks;
	}

	public void setLootsharing(boolean lootsharing) {
		this.lootsharing = lootsharing;
	}

	public boolean isLootsharing() {
		return lootsharing;
	}

	public void setTalkReq(int talkReq) {
		this.talkReq = talkReq;
	}

	public int getTalkReq() {
		return talkReq;
	}

	public void setJoinReq(int joinReq) {
		this.joinReq = joinReq;
	}

	public int getJoinReq() {
		return joinReq;
	}

	public int getKickReq() {
		return kickReq;
	}
}
