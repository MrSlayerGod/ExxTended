package org.dementhium.event.impl;

import org.dementhium.event.Event;
import org.dementhium.model.World;
import org.dementhium.task.impl.ClanSaveTask;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ClanSaveEvent extends Event {

	public ClanSaveEvent() {
		super(60000);
	}

	@Override
	public void run() {
		World.getWorld().submit(new ClanSaveTask());
	}

}
