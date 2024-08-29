package org.dementhium.task.impl;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 *
 * @author Graham Edgecombe
 */
public class SessionLogoutTask implements Task {

	private final Player player;
	
	public SessionLogoutTask(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute() {
		World.getWorld().unregister(player);
	}
 
}
