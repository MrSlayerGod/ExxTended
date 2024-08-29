package org.dementhium.event.impl;

import org.dementhium.event.Event;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class AutoSaveEvent extends Event {

	public AutoSaveEvent() {
		super(600);
	}

	@Override
	public void run() {
		World.getWorld().getExecutor().submitWork(new Runnable() {
			@Override
			public void run() {
				for(Player player : World.getWorld().getPlayers()) {
					World.getWorld().getPlayerLoader().save(player);
				}
			}
		});
	}

}
