package org.dementhium.content;

import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class DialougeManager {

	public static boolean proceedDialouge(Player player, int stage) {
		
		return false;
	}
	
	public static boolean handle(Player player, NPC npc) {
		
		int stage = player.getAttribute("dialougeStage", -1);
		if(stage == -1) {
			int id = npc.getId();
			switch(id) {
			case 2: // Man
				//sendOptionDialouge(player, new int[] {3, 1}, "MysticFloe", "Da");
				sendDialouge(player, -1, 9, "Hey there", "What's up?", "You noob");
				return true;
			}
		} else {
			resetDialouge(player);
			return handle(player, npc);
		}
		return false;
	} 
	
	public static void processNextDialouge(Player player, int button) {
		int stage;
		Object attribute = player.getAttribute("nextDialougeStage");
		if(attribute instanceof int[]) {
			stage = ((int[]) attribute)[button];
		} else {
			stage = (Integer) attribute;
		}
		if(!proceedDialouge(player, stage)) {
			resetDialouge(player);
		}
	}
	
	public static void sendDialouge(Player player, int face, int nextStage, String... dialouge) {
		if(dialouge.length == 0 || dialouge.length > 4) {
			return;
		}
		int interfaceId = (face == -1 ? 240 : 63) + dialouge.length;
		int index = 4;
		ActionSender.sendString(player, interfaceId, 3, face == -1 ? Misc.formatPlayerNameForDisplay(player.getUsername()) : NPCDefinition.forId(face).getName());
		for(String s : dialouge) {
			ActionSender.sendString(player, interfaceId, index, s);
			index++;
		}
		ActionSender.sendChatboxInterface(player, interfaceId);
		ActionSender.sendEntityOnInterface(player, face == -1, face, interfaceId, 2);
		ActionSender.sendInterAnimation(player, 9827, interfaceId, 2);
		player.setAttribute("nextDialougeStage", nextStage);
	}
	
	public static void sendOptionDialouge(Player player, int[] nextStages, String... dialouge) {
		if(dialouge.length < 2 || dialouge.length > 5) { //cant have 1 option
			return;
		}
		int interfaceId = 224 + (dialouge.length * 2);
		int index = 2;
		for(String string : dialouge) {
			ActionSender.sendString(player, string, interfaceId, index);
			index++;
		}
		ActionSender.sendChatboxInterface(player, interfaceId);
		player.setAttribute("nextDialougeStage", nextStages);
	}

	public static void resetDialouge(Player player){
		ActionSender.sendCloseChatBox(player);
		player.removeAttribute("nextDialougeStage");
		player.setAttribute("dialougeStage", -1);
	}

}
