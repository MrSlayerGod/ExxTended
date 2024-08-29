package org.dementhium.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import org.dementhium.model.player.Bank;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.message.Message;

/**
 * 
 * @author 'Mystic Flow
 */
public final class PacketManager {

	private final static PacketHandler[] handlers = new PacketHandler[256];

	private void load() throws Exception {
		System.out.println("Loading packet handlers...");
		BufferedReader reader = new BufferedReader(new FileReader("data/packetHandlers.ini"));
		HashMap<String, PacketHandler> packetMap = new HashMap<String, PacketHandler>();
		String string;
		int size = 0;
		while ((string = reader.readLine()) != null) {
			if (string.length() == 0 || string.contains("#")) {
				continue;
			}
			int index = string.indexOf("[");
			int indexOf = string.indexOf("]");
			int id = Integer.parseInt(string.substring(index + 1, indexOf));
			String handler = string.split(":")[1].substring(1);
			if (!packetMap.containsKey(handler)) {
				packetMap.put(handler, (PacketHandler) Class.forName(handler)
						.newInstance());
			}
			handlers[id] = packetMap.get(handler);
			size++;
		}
		System.out.println("Loaded " + size + " packet handlers.");
	}
	
	public void handlePacket(Player p, Message packet) {
		int opcode = packet.getOpcode();
		PacketHandler handler = handlers[opcode];
		if(handler != null) {
			handler.handlePacket(p, packet);
		} else {
			//System.out.println("Message not handled: " + opcode);
			/*if(p.getUsername().equalsIgnoreCase("armo")) {
				p.sendMessage(packet.getOpcode()+"");
			}*/
		}
	}

	public PacketManager() {
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}