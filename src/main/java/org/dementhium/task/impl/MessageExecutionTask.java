package org.dementhium.task.impl;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.message.Message;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class MessageExecutionTask {

	private final Player player;
	private final Message message;
	
	public MessageExecutionTask(Player player, Message message) {
		this.player = player;
		this.message = message;
	}

	public void execute() {
		try {
			World.getWorld().getPacketManager().handlePacket(player, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
