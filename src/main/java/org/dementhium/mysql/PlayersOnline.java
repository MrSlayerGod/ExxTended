package org.dementhium.mysql;

import org.dementhium.model.World;

public class PlayersOnline extends SQLConnection{

	public PlayersOnline() {
		super();
	}

	public boolean offline() {
		try {
			updateQuery("DELETE FROM `online` WHERE id = 1;");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean online() {
		int playersOnline = World.getWorld().getPlayers().size() + World.getWorld().getLobbyPlayers().size(); // Adds the players in-game and in the lobby.
		try {
			updateQuery("INSERT INTO `online` (id, currentlyonline) VALUES('1','"+playersOnline+"');");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}