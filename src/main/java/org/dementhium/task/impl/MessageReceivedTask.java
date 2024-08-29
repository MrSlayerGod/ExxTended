package org.dementhium.task.impl;

import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class MessageReceivedTask implements Task {

	private final Player player;
	private final MessageExecutionTask task;

	public MessageReceivedTask(Player player, MessageExecutionTask task) {
		this.player = player;
		this.task = task;
	}

	@Override
	public void execute() {
		if(player != null) {
			player.appendPacket(task);
		}
	}

}
