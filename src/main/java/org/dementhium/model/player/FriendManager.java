package org.dementhium.model.player;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.World;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 *
 * @author 'Mystic Flow
 */
public final class FriendManager {

	private final List<String> friends = new ArrayList<String>(200);
	private final List<String> ignores = new ArrayList<String>(100);
	private final Player player;

	public FriendManager(Player player) { 
		this.player = player;
	}

	public void loadFriendList() {
		if(friends.size() > 0) {
			for (String friend : friends) {
				Player other = World.getWorld().getPlayerInServer(friend);
				boolean isOnline = other != null;
				ActionSender.sendFriend(player, friend, friend, isOnline ? 0 : 1, isOnline, false, other != null && other.getConnection().isInLobby());
			}
		} else {
			ActionSender.sendUnlockFriendList(player);
		}
	}

	public void loadIgnoreList() {
		for (String Ignore : ignores) {
			ActionSender.sendIgnore(player, Ignore, Ignore);
		}
	}

	public void updateFriend(String friend) {
		Player other = World.getWorld().getPlayerInServer(friend);
		boolean isOnline = other != null;
		ActionSender.sendFriend(player, friend, friend, isOnline ? 0 : 1, isOnline, true, other != null && other.getConnection().isInLobby());
	}
	
	public void updateFriend(String friend, Player other) {
		boolean isOnline = other != null;
		ActionSender.sendFriend(player, friend, friend, isOnline ? 0 : 1, isOnline, true, other != null && other.getConnection().isInLobby());
	}


	public void addFriend(String name) {
		if (friends.size() >= 200
				|| name == null
				|| name.equals("")
				|| friends.contains(name)
				|| ignores.contains(name)
				|| name.equals(Misc.formatPlayerNameForDisplay(player.getUsername())
				))
			return;
		friends.add(Misc.formatPlayerNameForDisplay(name));
		updateFriend(name);
	}

	public void addIgnore(String ignore) {
		if (ignores.size() >= 100
				|| ignore == null
				|| friends.contains(ignore)
				|| ignores.contains(ignore)
				|| ignores.equals(Misc.formatPlayerNameForDisplay(player.getUsername())
				))
			return;
		ignores.add(Misc.formatPlayerNameForDisplay(ignore));
		ActionSender.sendIgnore(player, ignore, ignore);
	}

	public void removeIgnore(String Ignore) {
		if (Ignore == null || !ignores.contains(Ignore))
			return;
		ignores.remove(Misc.formatPlayerNameForDisplay(Ignore));
	}

	public void removeFriend(String Friend) {
		if (Friend == null || !friends.contains(Friend))
			return;
		friends.remove(Misc.formatPlayerNameForDisplay(Friend));
	}

	public List<String> getFriends() {
		return friends;
	}

	public List<String> getIgnores() {
		return ignores;
	}

}
