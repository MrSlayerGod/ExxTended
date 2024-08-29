package org.dementhium.mysql;

import org.dementhium.util.Misc;
import org.dementhium.model.player.Player;

public class HighscoresPK extends SQLConnection {

	public HighscoresPK() {
		super();
	}

	public void saveHighScore(Player player) {
		try {
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			updateQuery("DELETE FROM `highscorespk` WHERE name = '"+username+"';");
			updateQuery("INSERT INTO `highscorespk` (`name`, `dangerkills`, `dangerdeaths`, `dangerkdr`, `safekills`, `safedeaths`, `safekdr`) VALUES ('" + username + "','" + player.getDangerousKills() + "','" + player.getDangerousDeaths() + "','" + player.getDangerousKDR() + "','" + player.getSafeKills() + "','" + player.getSafeDeaths() + "','" + player.getSafeKDR() + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}