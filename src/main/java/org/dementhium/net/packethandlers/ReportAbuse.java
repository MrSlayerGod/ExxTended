package org.dementhium.net.packethandlers;

import org.dementhium.content.skills.magic.MagicHandler;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author Armo
 */
public class ReportAbuse extends PacketHandler {

	public static final int REPORT = 81;

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case REPORT:
			handleReportAbuse(player, packet);
			break;
		}
	}

	private void handleReportAbuse(Player player, Message in) {
	}
}
