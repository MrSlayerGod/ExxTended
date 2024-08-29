package org.dementhium.task.impl;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class SessionLoginTask implements Task {

	private final Player player;

	public SessionLoginTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		World.getWorld().register(player);
	}

}
