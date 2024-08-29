package org.dementhium.task.impl;

import java.io.IOException;

import org.dementhium.io.XMLHandler;
import org.dementhium.model.World;
import org.dementhium.task.Task;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ClanSaveTask implements Task {

	@Override
	public void execute() {
		World.getWorld().getExecutor().submitWork(new Runnable() {
			public void run() {
				try {
					XMLHandler.toXML("data/xml/clans.xml", World.getWorld().getClanManager().getClans());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
