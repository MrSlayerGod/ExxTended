package org.dementhium.util;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;


public class ShutDownHookThread implements Runnable {

	@Override
	public void run() {
		for(Player player : World.getWorld().getPlayers()) {
			World.getWorld().getPlayerLoader().save(player);
		}
	}

}
