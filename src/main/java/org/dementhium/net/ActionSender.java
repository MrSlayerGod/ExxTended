package org.dementhium.net;

import java.util.Random;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.GroundItemManager.GroundItem;
import org.dementhium.model.player.ChatMessage;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.TextHandling;
import org.dementhium.model.player.Equipment;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.util.Constants;
import org.dementhium.util.HintIcon;
import org.dementhium.util.InterfaceDecoder;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;
import org.dementhium.util.MapData;
import org.dementhium.event.Tickable;
import org.dementhium.mysql.Vote;

public class ActionSender {

	public static int messageCounter = 1;
	public static final Random r = new Random();
	public static final TextHandling text = new TextHandling();

	private static short frameIndex;

	public static short getFrameIndex() {
		return frameIndex;
	}

	public static void sendVotePage(Player p) {
		MessageBuilder bldr = new MessageBuilder(118);
		p.getConnection().write(bldr.toMessage());
	}

	public static void sendDonatePage(Player p) {
		MessageBuilder bldr = new MessageBuilder(119);
		p.getConnection().write(bldr.toMessage());
	}

	public static void sendForumsPage(Player p) {
		MessageBuilder bldr = new MessageBuilder(120);
		p.getConnection().write(bldr.toMessage());
	}

	public static void sendCommandPage(Player p) {
		MessageBuilder bldr = new MessageBuilder(121);
		p.getConnection().write(bldr.toMessage());
	}

	public static void sendOverlay(Player p, int childId) {
		switch (p.getConnection().getDisplayMode()) {
		case 0:
		case 1:
			sendInterface(p, 1, 548, 6, childId);
			break;
		case 2:
			sendInterface(p, 1, 746, 8, childId);
			break;
		}
	}

	public static void sendOverlay(Player p, int childId, int where) {
		switch (p.getConnection().getDisplayMode()) {
		case 0:
		case 1:
			sendInterface(p, 1, 548, where, childId);
			break;
		case 2:
			sendInterface(p, 1, 746, where, childId);
			break;
		}
	}
	
	public static void sendSystemUpdate(Player p, int seconds) {
		MessageBuilder bldr = new MessageBuilder(31);
		bldr.writeShort(seconds);
		p.getConnection().write(bldr.toMessage());
	}

	public static void moveCamera(Player p, int idk, int idk1, int idk2, int idk3, int e, int speed) {
		MessageBuilder bldr = new MessageBuilder(100);
		bldr.writeShort(idk);
		bldr.writeByte(idk1);
		bldr.writeByte(idk2);
		bldr.writeShort(idk3 << 2);
		bldr.writeByte(e);
		bldr.writeByte(speed);
		p.getConnection().write(bldr.toMessage());
	}
	public static void sendItems(Player player, int interfaceId, int childId, int type, Container items) {
		int main = interfaceId * 65536 + childId;
		MessageBuilder bldr = new MessageBuilder(120, PacketType.VAR_SHORT);
		bldr.writeInt(main);
		bldr.writeShort(type);
		bldr.writeShort(items.getSize());
		for (int i = 0; i < items.getSize(); i++) {
			Item item = items.get(i);
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getDefinition().getId();
				amt = item.getAmount();
			}
			if (amt > 254) {
				bldr.writeByteC((byte) 255);
				bldr.writeInt1(amt);
			} else {
				bldr.writeByteC(amt);
			}
			bldr.writeLEShort(id + 1);
		}
		player.getConnection().write(bldr.toMessage());
	}


	public static void closeInventoryInterface(Player p) {
		boolean fullscreen = p.getConnection().getDisplayMode() == 2;
		//closeSideInterface(p);
		closeInterface(p, fullscreen ? 746:548, fullscreen ? 83:193);
		p.getInventory().refresh();
	}

	public static void closeSideInterface(Player p) {
		boolean fullscreen = p.getConnection().getDisplayMode() == 2;
		closeInterface(p, fullscreen ? 746:548, 195);
	}

	public static void setHintIcon(Player player, int targetType, Mob target, int arrowType, int playerModel) {
		MessageBuilder bldr = new MessageBuilder(10);
		bldr.writeByte(((targetType & 0x1f) | (0 << 5)));
		bldr.writeByte((byte) arrowType);
		if (targetType == 1 || targetType == 10) {
			bldr.writeShort(target == null ? -1 : (target instanceof Player ? target.getIndex() : target.getClientIndex() + 1));
			bldr.skip(6);
		}
		bldr.writeShort(playerModel);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendHintIcon(Player player, HintIcon icon) {
		MessageBuilder bldr = new MessageBuilder(10);
		bldr.writeByte(((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5)));
		if (icon.getTargetType() == 0)
			bldr.skip(1);
		else {
			bldr.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				bldr.writeShort(icon.getTargetIndex());
				bldr.skip(6);
			} else if (icon.getTargetType() < 8) {
				bldr.writeByte(icon.getHeight());
				bldr.writeShort(icon.getCoordX());
				bldr.writeShort(icon.getCoordY());
				bldr.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				bldr.skip(2); // unknown short here
			}
		}
		bldr.writeShort(icon.getModelId());
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendProjectile(Player player, int projectileId, Mob mob, Location start, Location end, int startHeight, int endHeight, int speed) {
		sendProjectile(player, projectileId, mob, start, end, startHeight, endHeight, speed, 49, 15, 11);
	}

	public static void sendProjectile(Player player, int projectileId, Mob mob, Location start, Location end, int startHeight, int endHeight, int speed, int i1, int i2, int i3) {
		if(player.getRegion().getLastMapRegion() == null) {
			return;
		}
		MessageBuilder bldr = new MessageBuilder(11, PacketType.VAR_SHORT);
		int x = start.getRegionX() - (player.getRegion().getLastMapRegion().getRegionX() - 6);
		int y = start.getRegionY() - (player.getRegion().getLastMapRegion().getRegionY() - 6);
		bldr.writeByteA(x);
		bldr.writeByte(player.getLocation().getZ());
		bldr.writeByteA(y);
		bldr.writeByte(6); 
		x = start.getX() - (start.getRegionX() << 3);
		y = start.getY() - (start.getRegionY() << 3);
		bldr.writeByte((x & 0x7) << 3 | y & 0x7); 
		bldr.writeByte((start.getX() - end.getX()) * -1);
		bldr.writeByte((start.getY() - end.getY()) * -1);
		bldr.writeShort(mob != null ? (mob.isPlayer() ? -(mob.getIndex() + 1) : (mob.getIndex() + 1)) : 0);
		bldr.writeShort(projectileId);
		bldr.writeByte(startHeight / 4);
		bldr.writeByte(endHeight / 4);
		bldr.writeShort(speed / 2);
		bldr.writeShort(i1);
		bldr.writeByte(i2);
		bldr.writeShort(i3);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendBlankClientScript(Player player, int id) {
		MessageBuilder bldr = new MessageBuilder(98, PacketType.VAR_SHORT);
		bldr.writeShort(0);
		bldr.writeRS2String("");
		bldr.writeInt(id);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendGroundItem(Player player, Location tile, GroundItem item, boolean uniqueDrop) {
		MessageBuilder bldr = new MessageBuilder(60);
		int localX = tile.getX() - (player.getRegion().getLastMapRegion().getRegionX() - 6) * 8;
		int localY = tile.getY() - (player.getRegion().getLastMapRegion().getRegionY() - 6) * 8;
		if (uniqueDrop)
			sendCoordsForUniqueValue(player, localX, localY, tile.getZ());
		else
			sendCoords(player, localX, localY, tile.getZ());
		int deltaX = localX - ((localX >> 3) << 3);
		int deltaY = localY - ((localY >> 3) << 3);
		bldr.writeByte((0x7 & deltaY) | ((deltaX << 4) & 0x70));
		bldr.writeLEShortA(item.getAmount());
		bldr.writeShort(item.getId());
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendGroundItem(Player player, Location tile, int itemId, boolean uniqueDrop) {
		MessageBuilder bldr = new MessageBuilder(60);
		int localX = tile.getX() - (player.getRegion().getLastMapRegion().getRegionX() - 6) * 8;
		int localY = tile.getY() - (player.getRegion().getLastMapRegion().getRegionY() - 6) * 8;
		if (uniqueDrop)
			sendCoordsForUniqueValue(player, localX, localY, tile.getZ());
		else
			sendCoords(player, localX, localY, tile.getZ());
		int deltaX = localX - ((localX >> 3) << 3);
		int deltaY = localY - ((localY >> 3) << 3);
		bldr.writeByte((0x7 & deltaY) | ((deltaX << 4) & 0x70));
		bldr.writeLEShortA(1);
		bldr.writeShort(itemId);
		player.getConnection().write(bldr.toMessage());
	}
	

	private static void sendCoordsForUniqueValue(Player player, int localX, int localY, int z) {
		MessageBuilder bldr = new MessageBuilder(115);
		bldr.writeByteC(z);
		bldr.writeByteS(localX >> 3);
		bldr.writeByteA(localY >> 3);
		player.getConnection().write(bldr.toMessage());
	}

	private static void sendCoords(Player player, int localX, int localY, int z) {
		MessageBuilder bldr = new MessageBuilder(91);
		bldr.writeByteS(z);
		bldr.writeByteA(localY >> 3);
		bldr.writeByte(localX >> 3);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendPublicChatMessage(Player player, int playerIndex, int rights, ChatMessage chat) {
		MessageBuilder bldr = new MessageBuilder(90, PacketType.VAR_BYTE);
		bldr.writeShort(playerIndex);
		bldr.writeShort(chat.getEffects());
		bldr.writeByte(rights);
		byte[] chatStr = new byte[256];
		chatStr[0] = (byte) chat.getChatText().length();
		int offset = 1 + TextUtils.encryptPlayerChat(chatStr, 0, 1, chat
				.getChatText().length(), chat.getChatText().getBytes());
		bldr.writeBytes(chatStr, 0, offset);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendChatMessage(Player player, int TextType, String Text) {
		MessageBuilder bldr = new MessageBuilder(18, PacketType.VAR_BYTE);
		bldr.writeByte(TextType);
		bldr.writeInt(0);
		bldr.writeByte(0);
		bldr.writeRS2String(Text);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendTradeReq(Player player, String user, String message) {
		MessageBuilder bldr = new MessageBuilder(18, PacketType.VAR_BYTE);
		bldr.writeByte(100);
		bldr.writeInt(0);
		bldr.writeByte(0x1);
		bldr.writeRS2String(Misc.formatPlayerNameForDisplay(user));
		bldr.writeRS2String(message);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendDuelReq(Player player, String user, String message) {
		MessageBuilder bldr = new MessageBuilder(18, PacketType.VAR_BYTE);
		bldr.writeByte(101);
		bldr.writeInt(0);
		bldr.writeByte(0x1);
		bldr.writeRS2String(Misc.formatPlayerNameForDisplay(user));
		bldr.writeRS2String(message);
		player.getConnection().write(bldr.toMessage());
	}


	public static void sendMessage(Player player, String text) {
		sendChatMessage(player, 0, text);
	}

	public static void sendFriend(Player player, String Username, String displayName, int world, boolean writeOnline, boolean WarnMessage, boolean isLobby) {
		short WorldId = 1;
		MessageBuilder bldr = new MessageBuilder(49, PacketType.VAR_SHORT);
		bldr.writeByte(WarnMessage ? 0 : 1);
		bldr.writeRS2String(Misc.formatPlayerNameForDisplay(Username));
		bldr.writeRS2String("");
		bldr.writeShort(writeOnline ? (world == WorldId ? 1 : 2) : 0);
		bldr.writeByte(1);
		if (writeOnline) {
			bldr.writeRS2String(isLobby ? "Lobby" : "<col=00FF00>ExemptionX");
			bldr.writeByte(0);
		}
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendIgnore(Player player, String Username, String displayName) {
		MessageBuilder bldr = new MessageBuilder(4, PacketType.VAR_BYTE);
		bldr.writeByte(0);
		if (displayName == Username)
			Username = "";
		bldr.writeRS2String(displayName);
		bldr.writeRS2String(Username);
		bldr.writeRS2String(Username);
		bldr.writeRS2String(displayName);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendPrivateMessage(Player player, String Username, String message) {
		byte[] bytes = new byte[256];
		int length = TextUtils.huffmanCompress(message, bytes, 0);
		MessageBuilder bldr = new MessageBuilder(99, PacketType.VAR_BYTE);
		bldr.writeRS2String(Username);
		bldr.writeByte(message.length());
		bldr.writeBytes(bytes, 0, length);
		player.getConnection().write(bldr.toMessage());
	}

	public static void receivePrivateMessage(Player player, String Username, String displayName, int rights, String message) {
		long id = (long) (++messageCounter + ((Math.random() * Long.MAX_VALUE) + (Math.random() * Long.MIN_VALUE)));
		byte[] bytes = new byte[256];
		bytes[0] = (byte) message.length();
		int length = 1 + TextUtils.huffmanCompress(message, bytes, 1);
		MessageBuilder bldr = new MessageBuilder(42, PacketType.VAR_BYTE);
		bldr.writeByte(0);
		bldr.writeRS2String(Username);
		bldr.writeShort((int) (id >> 32));
		bldr.writeMediumInt((int) (id - ((id >> 32) << 32)));
		bldr.writeByte(rights);
		bldr.writeBytes(bytes, 0, length);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendUnlockIgnoreList(Player player) {
		player.getConnection().write(new MessageBuilder(2).toMessage());
	}

	public static void sendMusic(Player player, int music) {
		MessageBuilder bldr = new MessageBuilder(76);
		bldr.writeByte(255);
		bldr.writeShortA(music);
		bldr.writeByte(50);
		player.getConnection().write(bldr.toMessage());
	}


	public static void sendLoginInterfaces(Player player) {
		InterfaceDecoder.sendInterfaces(player);
		ActionSender.sendInterface(player, 1, 752, 9, 137);
	}

	public static void sendLoginMasks(Player player) {
		switch (player.getConnection().getDisplayMode()) {
		case 0:
		case 1:
			InterfaceDecoder.sendFixedAMasks(player);
			break;
		case 2:
		case 3:
			InterfaceDecoder.sendFullScreenAMasks(player);
			break;
		}
	}

	public static void sendLoginConfigurations(Player player) {
		sendRunEnergy(player);
		sendConfig(player, 1240, player.getSkills().getHitPoints() * 2);
		sendConfig2(player, 300, player.getSpecialAmount());

		sendLoginMasks(player);

		/*
		 * Configuration start
		 */
		sendConfig(player, 173, 0);
		sendConfig(player, 313, -1);
		sendConfig(player, 465, -1);
		sendConfig(player, 802, -1);
		sendConfig(player, 1085, 249852);
		sendConfig(player, 1160, -1);
		sendConfig(player, 1583, 511305630);
		//TODO sendConfig(player, 43, player.getAttackStyle());
		sendConfig(player, 1584, player.getPrayer().isAncientCurses() ? 1 : 0);
		sendConfig(player, 172, player.isAutoRetaliating() ? 0 : 1);
		organizeSpells(player);
		
		/*
		 * Bottom Configuration start
		 */
		sendBConfig(player, 168, 4);
		sendBConfig(player, 181, 0);
		sendBConfig(player, 234, 0);
		sendBConfig(player, 695, 0);
		sendBConfig(player, 768, 0);
	}

	public static void organizeSpells(Player player) {
		if(player.getSettings().getSpellBook() == 192)
			sendConfig(player, 439, 0);
		else if(player.getSettings().getSpellBook() == 193)
			sendConfig(player, 439, 1);
		else
			sendConfig(player, 439, 2);
	}

	public static void sendOtherLoginPackets(final Player player) {
		if(player.starter == 0) {
			player.cantTalk = true;
		}
		if (World.getWorld().isInGame(player.getUsername(), player)) {
			sendLogout(player, 0, false);
		}
		player.getInventory().refresh();
		player.getEquipment().refresh();
		player.getSkills().refresh();
		ActionSender.sendPlayerOption(player, "Follow", 2, false);
		ActionSender.sendPlayerOption(player, "Trade with", 3, false);
		sendMessage(player, "<img=1> Welcome to ExemptionX 634.");
		sendMessage(player, "<img=1> Try these to help you get started ::vote, ::donate, ::commands");
		sendMessage(player, "<img=1> You have "+player.pmsUnread+"/"+player.pmsTotal+" unread private messages on the forums. (::forums)");
		//sendMessage(player, "<img=1><img=2> New command!, '::report username' - abusing this will result in a ban!");
		//sendMessage(player, "<col=FF0000>Notice: Everything on the donation page is half price today! Limited time. ::donate</col>");
		//sendMessage(player, "TRIPLE TOKKULS FOR VOTING - LIMITED TIME OFFER! ::vote");
		/*sendMessage(player, "SOL AND CHAOTIC MAUL BONUS FOR VOTING! LIMITED TIME! ::vote");
		sendMessage(player, "SOL AND CHAOTIC MAUL BONUS FOR VOTING! LIMITED TIME! ::vote");
		sendMessage(player, "SOL AND CHAOTIC MAUL BONUS FOR VOTING! LIMITED TIME! ::vote");
		sendMessage(player, "SOL AND CHAOTIC MAUL BONUS FOR VOTING! LIMITED TIME! ::vote");
		sendMessage(player, "SOL AND CHAOTIC MAUL BONUS FOR VOTING! LIMITED TIME! ::vote");*/
		if(Constants.DOUBLE_BONUS_WEEKEND) {
			sendMessage(player, "<img=1> Enjoy double tokkul weekend! (::dwhelp)");
		}
		// Start of month - sendMessage(player, "<img=1>Donator Day<img=1>: Vote today to get your free donator for a day! ::vote - Only today."); // Start of month
		sendConfig(player, 287, player.getSettings().getPrivateTextColor());
		sendConfig(player, 170, 0);
		if(player.setWantTitle == 0) {
			player.setWantTitle = 1;
			player.getDefinition().setWantTitle(true);
		}
		/*if(player.resetPhats2 == 0) {
			clearItem(player, 20147);
			clearItem(player, 20148);
			clearItem(player, 20149);
			clearItem(player, 20150);
			clearItem(player, 20151);
			clearItem(player, 20152);
			clearItem(player, 20153);
			clearItem(player, 20154);
			clearItem(player, 20155);
			clearItem(player, 20156);
			clearItem(player, 20157);
			clearItem(player, 20158);

			clearItem(player, 13734);
			clearItem(player, 13735);
			clearItem(player, 13736);
			clearItem(player, 13737);
			clearItem(player, 13738);
			clearItem(player, 13739);
			clearItem(player, 13740);
			clearItem(player, 13741);
			clearItem(player, 13742);
			clearItem(player, 13743);
			clearItem(player, 13744);
			clearItem(player, 13745);

			clearItem(player, 15241);
			player.resetPhats2 = 1;
		}*/
		if(player.starter == 0) {
			if(text.gotStarter(player)) {
				player.starter = 1;
				player.sendMessage("You've already got a starter on another account!");
				return;
			}
			player.starter = 1;
			player.sendMessage("Here's your starter!");
			player.getInventory().addItem(10828, 1);
			player.getInventory().addItem(1127, 1);
			player.getInventory().addItem(1079, 1);
			player.getInventory().addItem(7460, 1);
			player.getInventory().addItem(1201, 1);
			player.getInventory().addItem(4151, 1);
			player.getInventory().addItem(1704, 1);
			player.getInventory().addItem(3105, 1);
			player.getInventory().addItem(1052, 1);
			player.getInventory().addItem(5698, 1);
			player.getInventory().addItem(6529, 50);
		}
		String username = Misc.formatPlayerNameForDisplay(player.getUsername());
		text.writeTo(player.getIp(), "data/text/ipstarters");
		text.writeTo("^ "+username, "data/text/ipstarters");
		clearItem(player, 4178);
		clearItem(player, 11286);
		clearItem(player, 11287);
		clearItem(player, 12790);
		clearItem(player, 1959);
		clearItem(player, 981);
		clearItem(player, 1961);
		clearItem(player, 13879);
		clearItem(player, 17291);
		//Noted now
		clearItem(player, 4179);
		clearItem(player, 12791);
		clearItem(player, 1960);
		clearItem(player, 982);
		clearItem(player, 1962);
		clearItem(player, 13880);
		clearItem(player, 17292);

		clearItem(player, 10491);
	

		clearItem(player, 14097);
		clearItem(player, 14098);
		clearItem(player, 14099);
		clearItem(player, 14100);
		clearItem(player, 14101);
		clearItem(player, 14102);
		clearItem(player, 14103);
		clearItem(player, 14104);
		clearItem(player, 14105);
		clearItem(player, 14106);
		clearItem(player, 14107);
		clearItem(player, 14108);
		clearItem(player, 14109);
		clearItem(player, 14111);
		clearItem(player, 14112);
		clearItem(player, 14113);
		clearItem(player, 14132);
		clearItem(player, 14133);
		clearItem(player, 14134);
		clearItem(player, 14135);
		clearItem(player, 14136);
		clearItem(player, 14137);
		clearItem(player, 14138);
		clearItem(player, 14139);
		clearItem(player, 14140);
		clearItem(player, 14141);

		clearItem(player, 975);
		clearItem(player, 976);
		clearItem(player, 6313);
		clearItem(player, 6314);
		clearItem(player, 6315);
		clearItem(player, 6316);
		clearItem(player, 6317);
		clearItem(player, 6318);



		boolean banked = false;
		/*for (int i2 = 0; i2 < Equipment.SIZE; i2++) {
			Item item = player.getEquipment().get(i2);
			if(item == null) {
				continue;
			}
			for (int i = 0; i < player.donorItems.length; i++) {
				if (item.getId() == player.donorItems[i] && player.getRights() < 2 && !player.getGroup().equalsIgnoreCase("Donator") && !player.getGroup().equalsIgnoreCase("Premium") && !player.getGroup().equalsIgnoreCase("Super")) {
					banked = true;
					player.getBank().addItemEquip(i2, player.getEquipment().getEquipment().getNumberOf(item));
				}
			}
			for (int i = 0; i < player.premItems.length; i++) {
				if (item.getId() == player.premItems[i] && player.getRights() < 2 && !player.getGroup().equalsIgnoreCase("Premium") && !player.getGroup().equalsIgnoreCase("Super")) {
					banked = true;
					player.getBank().addItemEquip(i2, player.getEquipment().getEquipment().getNumberOf(item));
				}
			}
			for (int i = 0; i < player.superItems.length; i++) {
				if (item.getId() == player.superItems[i] && player.getRights() < 2 && !player.getGroup().equalsIgnoreCase("Super")) {
					banked = true;
					player.getBank().addItemEquip(i2, player.getEquipment().getEquipment().getNumberOf(item));
				}
			}
		}*/
		if(banked) {
			player.sendMessage("Some of your items you were equipping have been banked.");
		}
		World.getWorld().submit(new Tickable(5) {
			public void execute() {
				int amt = 0;
				String username = Misc.formatPlayerNameForDisplay(player.getUsername());
				if(player.riggedDice || player.riggedDiceD) {
					String textBan = username+" has been flagged for rigged dice.";
					text.writeTo(textBan, "data/text/flagban");
				}
				if(player.getRights() < 2) {
				if(player.getBank().getContainer().getNumberOff(6529)+player.getInventory().getContainer().getNumberOff(6529) >= 20000) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(6529)+player.getInventory().getContainer().getNumberOff(6529))+" tokkul.";
					text.writeTo(textBan, "data/text/flagban");
				}
				if(player.getBank().getContainer().getNumberOff(1038)+player.getInventory().getContainer().getNumberOff(1038)+player.getBank().getContainer().getNumberOff(1039)+player.getInventory().getContainer().getNumberOff(1039) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1038)+player.getInventory().getContainer().getNumberOff(1038)+player.getBank().getContainer().getNumberOff(1039)+player.getInventory().getContainer().getNumberOff(1039))+" Red partyhat.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1038)+player.getInventory().getContainer().getNumberOff(1038)+player.getBank().getContainer().getNumberOff(1039)+player.getInventory().getContainer().getNumberOff(1039)+player.getEquipment().getEquipment().getNumberOff(1038)+player.getEquipment().getEquipment().getNumberOff(1039));
				if(player.getBank().getContainer().getNumberOff(1040)+player.getInventory().getContainer().getNumberOff(1040)+player.getBank().getContainer().getNumberOff(1041)+player.getInventory().getContainer().getNumberOff(1041) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1040)+player.getInventory().getContainer().getNumberOff(1040)+player.getBank().getContainer().getNumberOff(1041)+player.getInventory().getContainer().getNumberOff(1041))+" Yellow partyhat.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1040)+player.getInventory().getContainer().getNumberOff(1040)+player.getBank().getContainer().getNumberOff(1041)+player.getInventory().getContainer().getNumberOff(1041)+player.getEquipment().getEquipment().getNumberOff(1040)+player.getEquipment().getEquipment().getNumberOff(1041));
				if(player.getBank().getContainer().getNumberOff(1042)+player.getInventory().getContainer().getNumberOff(1042)+player.getBank().getContainer().getNumberOff(1043)+player.getInventory().getContainer().getNumberOff(1043) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1042)+player.getInventory().getContainer().getNumberOff(1042)+player.getBank().getContainer().getNumberOff(1043)+player.getInventory().getContainer().getNumberOff(1043))+" Blue partyhat.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1042)+player.getInventory().getContainer().getNumberOff(1042)+player.getBank().getContainer().getNumberOff(1043)+player.getInventory().getContainer().getNumberOff(1043)+player.getEquipment().getEquipment().getNumberOff(1042)+player.getEquipment().getEquipment().getNumberOff(1043));
				if(player.getBank().getContainer().getNumberOff(1044)+player.getInventory().getContainer().getNumberOff(1044)+player.getBank().getContainer().getNumberOff(1045)+player.getInventory().getContainer().getNumberOff(1045) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1044)+player.getInventory().getContainer().getNumberOff(1044)+player.getBank().getContainer().getNumberOff(1045)+player.getInventory().getContainer().getNumberOff(1045))+" Green partyhat.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1044)+player.getInventory().getContainer().getNumberOff(1044)+player.getBank().getContainer().getNumberOff(1045)+player.getInventory().getContainer().getNumberOff(1045)+player.getEquipment().getEquipment().getNumberOff(1044)+player.getEquipment().getEquipment().getNumberOff(1045));
				if(player.getBank().getContainer().getNumberOff(1046)+player.getInventory().getContainer().getNumberOff(1046)+player.getBank().getContainer().getNumberOff(1047)+player.getInventory().getContainer().getNumberOff(1047) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1046)+player.getInventory().getContainer().getNumberOff(1046)+player.getBank().getContainer().getNumberOff(1047)+player.getInventory().getContainer().getNumberOff(1047))+" Purple partyhat.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1046)+player.getInventory().getContainer().getNumberOff(1046)+player.getBank().getContainer().getNumberOff(1047)+player.getInventory().getContainer().getNumberOff(1047)+player.getEquipment().getEquipment().getNumberOff(1046)+player.getEquipment().getEquipment().getNumberOff(1047));
				if(player.getBank().getContainer().getNumberOff(1048)+player.getInventory().getContainer().getNumberOff(1048)+player.getBank().getContainer().getNumberOff(1049)+player.getInventory().getContainer().getNumberOff(1049) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1048)+player.getInventory().getContainer().getNumberOff(1048)+player.getBank().getContainer().getNumberOff(1049)+player.getInventory().getContainer().getNumberOff(1049))+" White partyhat.";
					text.writeTo(textBan, "data/text/flagban");
				}	
amt += (player.getBank().getContainer().getNumberOff(1048)+player.getInventory().getContainer().getNumberOff(1048)+player.getBank().getContainer().getNumberOff(1049)+player.getInventory().getContainer().getNumberOff(1049)+player.getEquipment().getEquipment().getNumberOff(1048)+player.getEquipment().getEquipment().getNumberOff(1049));			
				if(player.getBank().getContainer().getNumberOff(1050)+player.getInventory().getContainer().getNumberOff(1050)+player.getBank().getContainer().getNumberOff(1051)+player.getInventory().getContainer().getNumberOff(1051) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1050)+player.getInventory().getContainer().getNumberOff(1050)+player.getBank().getContainer().getNumberOff(1051)+player.getInventory().getContainer().getNumberOff(1051))+" Santa hat.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1050)+player.getInventory().getContainer().getNumberOff(1050)+player.getBank().getContainer().getNumberOff(1051)+player.getInventory().getContainer().getNumberOff(1051)+player.getEquipment().getEquipment().getNumberOff(1050)+player.getEquipment().getEquipment().getNumberOff(1051));
				if(player.getBank().getContainer().getNumberOff(1053)+player.getInventory().getContainer().getNumberOff(1053)+player.getBank().getContainer().getNumberOff(1054)+player.getInventory().getContainer().getNumberOff(1054) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1053)+player.getInventory().getContainer().getNumberOff(1053)+player.getBank().getContainer().getNumberOff(1054)+player.getInventory().getContainer().getNumberOff(1054))+" Green h'ween.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1053)+player.getInventory().getContainer().getNumberOff(1053)+player.getBank().getContainer().getNumberOff(1054)+player.getInventory().getContainer().getNumberOff(1054)+player.getEquipment().getEquipment().getNumberOff(1053)+player.getEquipment().getEquipment().getNumberOff(1054));
				if(player.getBank().getContainer().getNumberOff(1055)+player.getInventory().getContainer().getNumberOff(1055)+player.getBank().getContainer().getNumberOff(1056)+player.getInventory().getContainer().getNumberOff(1056) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1055)+player.getInventory().getContainer().getNumberOff(1055)+player.getBank().getContainer().getNumberOff(1056)+player.getInventory().getContainer().getNumberOff(1056))+" Blue h'ween.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1055)+player.getInventory().getContainer().getNumberOff(1055)+player.getBank().getContainer().getNumberOff(1056)+player.getInventory().getContainer().getNumberOff(1056)+player.getEquipment().getEquipment().getNumberOff(1055)+player.getEquipment().getEquipment().getNumberOff(1056));
				if(player.getBank().getContainer().getNumberOff(1057)+player.getInventory().getContainer().getNumberOff(1057)+player.getBank().getContainer().getNumberOff(1058)+player.getInventory().getContainer().getNumberOff(1058) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(1057)+player.getInventory().getContainer().getNumberOff(1057)+player.getBank().getContainer().getNumberOff(1058)+player.getInventory().getContainer().getNumberOff(1058))+" Red h'ween.";
					text.writeTo(textBan, "data/text/flagban");
				}
amt += (player.getBank().getContainer().getNumberOff(1057)+player.getInventory().getContainer().getNumberOff(1057)+player.getBank().getContainer().getNumberOff(1058)+player.getInventory().getContainer().getNumberOff(1058)+player.getEquipment().getEquipment().getNumberOff(1057)+player.getEquipment().getEquipment().getNumberOff(1058));
				if(player.getBank().getContainer().getNumberOff(13740)+player.getInventory().getContainer().getNumberOff(13740)+player.getBank().getContainer().getNumberOff(13741)+player.getInventory().getContainer().getNumberOff(13741) >= 5) {
					String textBan = username+" has been flagged for duping "+(player.getBank().getContainer().getNumberOff(13740)+player.getInventory().getContainer().getNumberOff(13740)+player.getBank().getContainer().getNumberOff(13741)+player.getInventory().getContainer().getNumberOff(13741))+" Divine spirit shield.";
					text.writeTo(textBan, "data/text/flagban");
				}
				if(player.getBank().getContainer().getNumberOff(962)+player.getInventory().getContainer().getNumberOff(962)+player.getBank().getContainer().getNumberOff(963)+player.getInventory().getContainer().getNumberOff(963) >= 1) {
					String textBan = username+" has been flagged for having "+(player.getBank().getContainer().getNumberOff(963)+player.getInventory().getContainer().getNumberOff(963)+player.getBank().getContainer().getNumberOff(963)+player.getInventory().getContainer().getNumberOff(963))+" Chistmas cracker.";
					text.writeTo(textBan, "data/text/flagban");
				}
				}
				if(player.trading == true) {
					player.trading = false;
					player.getInventory().getContainer().addAll(player.getSt().getContainer());
					player.getInventory().refresh();
					player.getSt().reset();
				}
				/*if(player.clearRares3 == 0 && amt > 0) {
					clearItem(player, 1038);
					clearItem(player, 1039);
					clearItem(player, 1040);
					clearItem(player, 1041);
					clearItem(player, 1042);
					clearItem(player, 1043);
					clearItem(player, 1044);
					clearItem(player, 1045);
					clearItem(player, 1046);
					clearItem(player, 1047);
					clearItem(player, 1048);
					clearItem(player, 1049);
					clearItem(player, 1050);
					clearItem(player, 1051);
					clearItem(player, 1053);
					clearItem(player, 1054);
					clearItem(player, 1055);
					clearItem(player, 1056);
					clearItem(player, 1057);
					clearItem(player, 1058);
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.sendMessage("All rares have been replaced with tokkul due to a dupe. Tokkuls in the bank.");
					player.getBank().addItem(6529, (5000*amt), true);
				}
				player.clearRares3 = 1;*/
				if (Constants.VOTE_ENABLED) {
					if (!Constants.getVoteDatabase().hasVoted(username, player)) {
						sendVotePage(player);
						sendInterface(player, 382);
						sendString(player, 382, 22, "Please vote!");
						sendString(player, 382, 23, "It has been detected that you haven't voted today, everytime we detect you haven't voted, the voting page will automatically open on login. Please vote to help the server. Voting stops the page from loading on login and gains you access to curse prayers and tokkul reward! Please click 'Vote Now' below to vote.");
						sendString(player, 382, 24, "Vote Now");
						sendString(player, 382, 25, "Remind me later");
					} else {
						player.voted = true;
					}
				}
				if(player.voted) {
					player.cantTalk = false;
				} else {
					World.getWorld().submit(new Tickable(10) {
						public void execute() {
							player.cantTalk = false;
							this.stop();
						}
					});
				}
				this.stop();
			}
		});
	}

	public static void clearItem(Player player, int itemId) {
		player.getInventory().deleteAll(itemId);
		player.getEquipment().deleteAll(itemId);
		player.getBank().deleteAll(itemId);
	}

	public static void sendRunEnergy(Player player) {
		MessageBuilder bldr = new MessageBuilder(14);
		bldr.writeByte(player.getWalkingQueue().getRunEnergy());
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendSkillLevel(Player player, int skill) {
		MessageBuilder bldr = new MessageBuilder(85);
		bldr.writeInt((int) player.getSkills().getXp(skill));
		bldr.writeByte(player.getSkills().getLevel(skill));
		bldr.writeByteS(skill);
		player.getConnection().write(bldr.toMessage());

	}

	// private static byte Unblack_Out = 0;
	// private static byte Black_out_Orb = 1;
	// private static byte Black_out_Map = 2;
	// private static byte Black_out_Orb_and_Map = 5;

	public static void Blackout(Player player, int stage) {
		/*
		 * OutStream out = new OutStream(2); bldr.writePacket(208);
		 * bldr.writeByte(stage); player.getConnection().write(bldr.toMessage());
		 */
	}

	public static void sendPlayerOption(Player player, String option, int slot, boolean top) {
		MessageBuilder bldr = new MessageBuilder(20, PacketType.VAR_BYTE);
		bldr.writeByte(slot);
		bldr.writeLEShortA(65535);
		bldr.writeRS2String(option);
		bldr.writeByte(top ? 1 : 0);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendTradeOptions(Player player) {
		Object[] tparams1 = new Object[]{"", "", "", "Value<col=FF9040>", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, 335 << 16 | 31};
		sendClientScript(player, 150, tparams1, "IviiiIsssssssss");
		sendAMask(player, 1150, 335, 31, 0, 27);		
		Object[] tparams3 = new Object[]{"", "", "", "", "", "", "", "Value<col=FF9040>", "Examine", -1, 0, 7, 4, 90, 335 << 16 | 34};
		sendClientScript(player, 695, tparams3, "IviiiIsssssssss");
		sendAMask(player, 1026, 335, 34, 0, 27);
		Object[] tparams2 = new Object[]{"", "", "Lend", "Value<col=FF9040>", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, 336 << 16};
		sendClientScript(player, 150, tparams2, "IviiiIsssssssss");
		sendAMask(player, 1278, 336, 0, 0, 27);
		sendAMask(player, 1026, 335, 87, -1, -1);
		sendAMask(player, 1030, 335, 88, -1, -1);
		sendAMask(player, 1024, 335, 83, -1, -1);
		sendInterfaceConfig(player, 335, 74, true);
		sendInterfaceConfig(player, 335, 75, true);
	}

	public static void sendDuelOptions(Player p) {
		sendAMask(p, 1278, 631, 102, 0, 27);
		sendAMask(p, 1278, 631, 103, 0, 27);
		sendAMask(p, 1278, 628, 0, 0, 27); 
		Object[] tparams1 = new Object[]{"", "", "", "", "Remove X", "Remove All", "Remove 10", "Remove 5", "Remove", 1, 0, 2, 2, 134, 631 << 16 | 94};
		sendClientScript(p, 150, tparams1, "IviiiIsssssssss");
		Object[] tparams2 = new Object[]{"", "", "", "", "Stake X", "Stake All", "Stake 10", "Stake 5", "Stake", -1, 0, 7, 4, 93, 628 << 16};
		sendClientScript(p, 150, tparams2, "IviiiIsssssssss");
	}

	public static void sendDuelOptions(Player p, int lol) {
		sendAMask(p, 1278, 631, lol, 0, 27);
		sendAMask(p, 1278, 628, 0, 0, 27); 
		Object[] tparams1 = new Object[]{"", "", "", "", "Remove X", "Remove All", "Remove 10", "Remove 5", "Remove", 1, 0, 2, 2, 134, 631 << 16 | 94};
		sendClientScript(p, 150, tparams1, "IviiiIsssssssss");
		Object[] tparams2 = new Object[]{"", "", "", "", "Stake X", "Stake All", "Stake 10", "Stake 5", "Stake", -1, 0, 7, 4, 93, 628 << 16};
		sendClientScript(p, 150, tparams2, "IviiiIsssssssss");
	}


	public static void sendAMask(Player player, int set1, int set2, int interfaceId1, int childId1, int interfaceId2, int childId2) {
		MessageBuilder bldr = new MessageBuilder(35);
		bldr.writeLEShortA(set2);
		bldr.writeLEInt(interfaceId1 << 16 | childId1);
		bldr.writeLEShortA(frameIndex++);
		bldr.writeShortA(set1);
		bldr.writeInt2(interfaceId2 << 16 | childId2);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendAMask(Player p, int set, int interfaceId, int childId, int off, int len) {
		MessageBuilder bldr = new MessageBuilder(35);
		bldr.writeLEShortA(len);
		bldr.writeLEInt(interfaceId << 16 | childId);
		bldr.writeLEShortA(frameIndex++);
		bldr.writeShortA(off);
		bldr.writeInt2(set);
		p.getConnection().write(bldr.toMessage());
	}

	public static void sendInterfaceConfig(Player player, int interfaceId, int childId, boolean hidden) {
		MessageBuilder bldr = new MessageBuilder(34);
		bldr.writeShort(frameIndex++);
		bldr.writeInt1((interfaceId << 16) | childId);
		bldr.writeByteA(hidden ? 1 : 0);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendBConfig(Player player, int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendBConfig2(player, id, value);
		} else {
			sendBConfig1(player, id, value);
		}
	}

	public static void sendBConfig1(Player player, int configId, int value) {
		MessageBuilder bldr = new MessageBuilder(103);
		bldr.writeLEShort(frameIndex++);
		bldr.writeByteA(value);
		bldr.writeShortA(configId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendBConfig2(Player player, int configId, int value) {
		MessageBuilder bldr = new MessageBuilder(89);
		bldr.writeLEShortA(frameIndex++);
		bldr.writeLEShortA(configId);
		bldr.writeInt(value);
		player.getConnection().write(bldr.toMessage());

	}

	public static void sendConfig(Player player, int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendConfig2(player, id, value);
		} else {
			sendConfig1(player, id, value);
		}
	}

	public static void sendConfig1(Player player, int configId, int value) {
		MessageBuilder bldr = new MessageBuilder(25);
		bldr.writeShortA(configId);
		bldr.writeByteS(value);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendConfig2(Player player, int configId, int value) {
		MessageBuilder bldr = new MessageBuilder(84);
		bldr.writeInt2(value);
		bldr.writeShortA(configId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendEntityOnInterface(Player player, boolean isPlayer, int entityId, int interId, int childId) {
		if (isPlayer)
			ActionSender.sendPlayerOnInterface(player, interId, childId);
		else
			ActionSender.sendNpcOnInterface(player, interId, childId, entityId);
	}

	public static void sendPlayerOnInterface(Player player, int interId, int childId) {
		MessageBuilder bldr = new MessageBuilder(65);
		bldr.writeLEShortA(ActionSender.frameIndex++);
		bldr.writeInt(interId << 16 | childId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendNpcOnInterface(Player player, int interId, int childId, int npcId) {
		MessageBuilder bldr = new MessageBuilder(17);
		bldr.writeLEShortA(ActionSender.frameIndex++);
		bldr.writeLEShort(npcId);
		bldr.writeInt1(interId << 16 | childId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendInterAnimation(Player player, int emoteId, int interId, int childId) {
		MessageBuilder bldr = new MessageBuilder(74);
		bldr.writeInt2(interId << 16 | childId);
		bldr.writeShort(ActionSender.frameIndex++);
		bldr.writeShortA(emoteId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendString(Player player, String string, int interfaceId, int childId) {
		MessageBuilder bldr = new MessageBuilder(95, PacketType.VAR_SHORT);
		bldr.writeInt2(interfaceId << 16 | childId);
		bldr.writeRS2String(string);
		bldr.writeShortA(ActionSender.frameIndex++);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendString(Player player, int interfaceId, int childId, String string) {
		MessageBuilder bldr = new MessageBuilder(95, PacketType.VAR_SHORT);
		bldr.writeInt2(interfaceId << 16 | childId);
		bldr.writeRS2String(string);
		bldr.writeShortA(ActionSender.frameIndex++);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendInterface(Player player, int childId) {
		switch (player.getConnection().getDisplayMode()) {
		case 0:
		case 1:
			ActionSender.sendInterface(player, 0, 548, 16, childId);
			break;
		case 2:
			ActionSender.sendInterface(player, 0, 746, 8, childId);
			break;
		}
	}

	public static void sendTab(Player player, int tabId, int childId) {
		ActionSender.sendInterface(player, 1, childId == 137 ? 752 : 548,
				tabId, childId);
	}

	public static void sendChatboxInterface(Player player, int childId) {
		ActionSender.sendInterface(player, 1, 752, 9, childId);
	}

	public static void sendCloseChatBox(Player player) {
		ActionSender.sendInterface(player, 1, 752, 9, 137);
	}

	public static void closeInterface(Player player, int window, int tab) {
		MessageBuilder bldr = new MessageBuilder(54);
		bldr.writeShort(0);
		bldr.writeInt(window << 16 | tab);
		player.getConnection().write(bldr.toMessage());
	}

	public static void closeInter(Player player) {
		int winId = player.getConnection().getDisplayMode() < 2 ? 548 : 746;
		int slotId = player.getConnection().getDisplayMode() < 2 ? 16 : 8;
		closeInterface(player, winId, slotId);
		//sendCloseChatBox(player);
		//InterfaceDecoder.sendInterfaces(player);
	}

	public static void sendInventoryInterface(Player player, int childId) {
		switch (player.getConnection().getDisplayMode()) {
		case 0:
		case 1:
			ActionSender.sendInterface(player, 0, 548, 193, childId);
			break;
		case 2:
			ActionSender.sendInterface(player, 0, 746, 83, childId);
			break;
		}
	}

	public static void sendInterface(Player player, int showId, int windowId, int interfaceId, int childId) {
		if(interfaceId == 16 || interfaceId == 8) {
			player.setAttribute("bankScreen", 2);
		}
		MessageBuilder bldr = new MessageBuilder(3);
		bldr.writeLEInt(windowId * 65536 + interfaceId);
		bldr.writeShort(frameIndex++);
		bldr.writeByteC(showId);
		bldr.writeShort(interfaceId >> 16 | childId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendWindowsPane(Player player, short PaneId, byte subWindowsId) {
		MessageBuilder bldr = new MessageBuilder(37);
		bldr.writeShort(frameIndex++);
		bldr.writeByteS(subWindowsId);
		bldr.writeShort(PaneId);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendMapRegion(Player player, boolean loggedin) {
		MessageBuilder bldr = new MessageBuilder(71, PacketType.VAR_SHORT);
		if (!loggedin) {
			player.getGpi().loginData(bldr);
		}
		bldr.writeLEShortA(player.getLocation().getRegionY());
		bldr.writeShortA(player.getLocation().getRegionX());
		bldr.writeByteA(0);
		bldr.writeByteA(0);//
		boolean forceSend = true;
		if ((((player.getLocation().getRegionX() / 8) == 48) || ((player.getLocation().getRegionX() / 8) == 49)) && ((player.getLocation().getRegionY() / 8) == 48)) {
			forceSend = false;
		}
		if (((player.getLocation().getRegionX() / 8) == 48) && ((player.getLocation().getRegionY() / 8) == 148)) {
			forceSend = false;
		}
		for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6) / 8); xCalc++) {
			for (int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
				short region = (short) (yCalc + (xCalc << 8));
				if (forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					int[] mapData = MapData.getMapData().get(region);
					if (mapData == null)
						mapData = new int[4];
					for (int i = 0; i < 4; i++)
						bldr.writeInt(0);
				}
			}
		}
		player.getConnection().write(bldr.toMessage());
		player.getRegion().setLastMapRegion(Location.create(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
		player.getRegion().setDidMapRegionChange(true);
		//World.getWorld().getGroundItemManager().removeItems(player);
		World.getWorld().getGroundItemManager().refreshItems(player);
		//sendMapRegion2(player, true);
	}

	public static void sendMapRegion2(Player player, boolean loggedin) {
		MessageBuilder bldr = new MessageBuilder(71, PacketType.VAR_SHORT);
		if (!loggedin) {
			player.getGpi().loginData(bldr);
		}
		bldr.writeLEShortA(player.getLocation().getRegionY());
		bldr.writeShortA(player.getLocation().getRegionX());
		bldr.writeByteA(0);
		bldr.writeByteA(0);//
		boolean forceSend = true;
		if ((((player.getLocation().getRegionX() / 8) == 48) || ((player.getLocation().getRegionX() / 8) == 49)) && ((player.getLocation().getRegionY() / 8) == 48)) {
			forceSend = false;
		}
		if (((player.getLocation().getRegionX() / 8) == 48) && ((player.getLocation().getRegionY() / 8) == 148)) {
			forceSend = false;
		}
		for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6) / 8); xCalc++) {
			for (int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
				short region = (short) (yCalc + (xCalc << 8));
				if (forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					int[] mapData = MapData.getMapData().get(region);
					if (mapData == null)
						mapData = new int[4];
					for (int i = 0; i < 4; i++)
						bldr.writeInt(mapData[i]);
				}
			}
		}
		player.getConnection().write(bldr.toMessage());
		player.getRegion().setLastMapRegion(Location.create(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
		player.getRegion().setDidMapRegionChange(false);
		//World.getWorld().getGroundItemManager().removeItems(player);
		//World.getWorld().getGroundItemManager().refreshItems(player);
	}


	public static void sendItems(Player player, int type, Container inventory, boolean split) {
		MessageBuilder bldr = new MessageBuilder(56, PacketType.VAR_SHORT);
		bldr.writeShort(type);
		bldr.writeByte((split ? 1 : 0));
		bldr.writeShort(inventory.getSize());
		for (int i = 0; i < inventory.getSize(); i++) {
			Item item = inventory.get(i);
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getDefinition().getId();
				amt = item.getAmount();
			}
			bldr.writeByteA(amt > 254 ? 0xff : amt);
			if (amt > 0xfe)
				bldr.writeInt(amt);
			bldr.writeShort(id + 1);
		}
		player.getConnection().write(bldr.toMessage());
	}


	public static void itemOnInterface(Player player, int interfaceid, int child, int amount, int itemid) {
		MessageBuilder bldr = new MessageBuilder(56);
		bldr.writeInt(interfaceid << 16 | child);// Interface this should be in
		bldr.writeInt1(amount);// Amount to display, (Number next to pic)
		bldr.writeLEShortA(0);// Count
		bldr.writeLEShortA(itemid);// ITEM Id
		player.getConnection().write(bldr.toMessage());
	}

	public static void loginResponse(Player player) {
		MessageBuilder bldr = new MessageBuilder();
		bldr.writeByte(13); //length
		bldr.writeByte(player.getRights());
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(1);
		bldr.writeByte(0);
		bldr.writeShort(player.getIndex());
		bldr.writeByte(1);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(1); // members
		player.getConnection().write(bldr.toMessage());
		ActionSender.sendMapRegion(player, false);
	}

	public static void sendLogout(Player player, int button, boolean kicked) {
		if(player.getCombatState().getLastAttacker() != null || player.getSkills().getHitPoints() <= 0) {
			player.sendMessage("Please wait 10 seconds after combat before logging out.");
			return;
		}
		MessageBuilder bldr = new MessageBuilder(38);
		player.getConnection().write(bldr.toMessage());
		World.getWorld().unregister(player);
		player.getConnection().setPlayer(null);
	}

	public static void sendClientScript(Player player, int id, Object[] params, String types) {
		if (params.length != types.length())
			throw new IllegalArgumentException("params size should be the same as types length");
		MessageBuilder bldr = new MessageBuilder(98, PacketType.VAR_SHORT);
		bldr.writeShort(ActionSender.frameIndex++);
		bldr.writeRS2String(types);
		int idx = 0;
		for (int i = types.length() - 1; i >= 0; i--) {
			if (types.charAt(i) == 's')
				bldr.writeRS2String((String) params[idx]);
			else
				bldr.writeInt((Integer) params[idx]);
			idx++;
		}
		bldr.writeInt(id);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendItemKeptOnDeath(Player player) {
		sendAMask(player, 211, 0, 2, 102, 18, 4);
		sendAMask(player, 212, 0, 2, 102, 21, 42);
		Object[] params = new Object[] { 11510, 12749, "", 0, 0, -1, 4151, 15441, 15443, 3, 0 };
		sendClientScript(player, 118, params, "Iviiiiiiiiiiiissssssssssss");
		sendBConfig(player, 199, 442);
	}

	public static void sendGESell(Player player) {
		sendConfig1(player, 1112, -1);
		sendConfig1(player, 1113, -1);
		sendConfig1(player, 1111, 1);
		sendConfig1(player, 1109, -1);
		sendAMask(player, 6, 209, 105, -1, -1);
		sendAMask(player, 6, 211, 105, -1, -1);
		sendInterface(player, 105);
		sendInventoryInterface(player, 107);
	}

	public static void sendGrandExchange(Player player, int slot, int progress, int item, int price, int amount, int currentAmount) {
		MessageBuilder bldr = new MessageBuilder(22);
		bldr.writeByte((byte) slot);
		bldr.writeByte((byte) progress);
		bldr.writeShort(item);
		bldr.writeInt(price);
		bldr.writeInt(amount);
		bldr.writeInt(currentAmount);
		bldr.writeInt(price * currentAmount);
		player.getConnection().write(bldr.toMessage());
	}

	public static void switchPanes(Player player, int paneFrom, int windowPosFrom, int paneTo, int windowPosTo) {
		MessageBuilder bldr = new MessageBuilder(72);
		bldr.writeInt(paneTo << 16 | windowPosTo);
		bldr.writeInt(paneFrom << 16 | windowPosFrom);
		bldr.writeLEShort(ActionSender.frameIndex++);
		player.getConnection().write(bldr.toMessage());
	}

	public static void resetGe(Player p, int i) {
		MessageBuilder bldr = new MessageBuilder(22);
		bldr.writeByte((byte) i);
		bldr.writeByte((byte) 0);
		bldr.writeShort(0);
		bldr.writeInt(0);
		bldr.writeInt(0);
		bldr.writeInt(0);
		bldr.writeInt(0);
		p.getConnection().write(bldr.toMessage());
	}

	public static void removeGroundItem(Player player, GroundItem item) {
		removeGroundItem(player, item.getLocation(), item.getItem());
	}

	public static void sendLocation(Player player, Location location) {
		int x = location.getRegionX() - (player.getRegion().getLastMapRegion().getRegionX() - 6);
		int y = location.getRegionY() - (player.getRegion().getLastMapRegion().getRegionX() - 6);
		MessageBuilder pb = new MessageBuilder(91);
		pb.writeByteS((byte) location.getZ()).writeByteA(y).writeByte((byte) (byte) x);
		player.getConnection().getChannel().write(pb.toMessage());
	}

	public static void sendFullInterface(Player player, int childId) {
		switch (player.getConnection().getDisplayMode()) {
		case 0:
		case 1:
			ActionSender.sendInterface(player, 1, 548, 16, childId);
			break;
		case 2:
			ActionSender.sendInterface(player, 1, 746, 8, childId);
			break;
		}
	}

	public static void sendUnlockFriendList(Player player) {
		MessageBuilder bldr = new MessageBuilder(49, PacketType.VAR_SHORT);
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendLobbyResponse(Player player) {
		MessageBuilder bldr = new MessageBuilder();
		bldr.writeByte((byte) 0);
		bldr.writeByte((byte) 0);
		bldr.writeByte((byte) 0);
		bldr.writeByte((byte) 0);
		bldr.writeByte((byte) 0);
		bldr.writeShort(1337); // member days left
		bldr.writeShort(0); // recovery questions
		bldr.writeShort(player.pmsUnread); // unread messages
		bldr.writeShort(3563);
		int ipHash = Misc.IPAddressToNumber(player.getIp());
		bldr.writeInt(ipHash); // last ip
		bldr.writeByte((byte) 3); // email status (0 - no email, 1 - pending
		// parental confirmation, 2 - pending
		// confirmation, 3 - registered)
		bldr.writeShort(0);
		bldr.writeShort(0);
		bldr.writeByte((byte) 0);
		bldr.putGJString2(player.getUsername());
		bldr.writeByte((byte) 0);
		bldr.writeInt(1);
		bldr.writeShort(1); // current world id
		bldr.putGJString2(player.getIp());
		MessageBuilder lobbyResponse = new MessageBuilder();
		lobbyResponse.writeByte((byte) (byte) bldr.position());
		lobbyResponse.writeBytes(bldr.getBuffer());
		player.getConnection().write(lobbyResponse.toMessage());
	}

	public static void sendCrashPacket(Player player) {
		MessageBuilder bldr = new MessageBuilder(95, PacketType.VAR_SHORT);
		bldr.writeInt2(100);
		bldr.writeRS2String("Sup");
		bldr.writeShortA(Short.MAX_VALUE);
		player.getConnection().write(bldr.toMessage());
	}

	public static void removeGroundItem(Player player, Location tile, Item item) {
		if(tile == null || item == null || player == null || player.getRegion().getLastMapRegion() == null) {
			return;
		}
		MessageBuilder bldr = new MessageBuilder(88);
		int localX = tile.getX() - (player.getRegion().getLastMapRegion().getRegionX() - 6) * 8;
		int localY = tile.getY() - (player.getRegion().getLastMapRegion().getRegionY() - 6) * 8;
		sendCoords(player, localX, localY, tile.getZ());
		int deltaX = localX - ((localX >> 3) << 3);
		int deltaY = localY - ((localY >> 3) << 3);
		bldr.writeLEShortA(item.getId());
		bldr.writeByte(((0x7 & deltaY) | ((deltaX << 4) & 0x70)));
		player.getConnection().write(bldr.toMessage());

	}

	public static void requestPath(Player player, int x, int y, boolean flag) {
		MessageBuilder bldr = new MessageBuilder(117);
		bldr.writeShort(y);
		bldr.writeShort(x);
		bldr.writeByte((flag ? 1 : 0));
		player.getConnection().write(bldr.toMessage());
	}

	public static void sendAccessMask(Player player, int range1, int range2, int interfaceId1, int childId1, int interfaceId2, int childId2) {
		MessageBuilder pb = new MessageBuilder(35);
		pb.writeLEShortA(range2).writeLEInt(interfaceId1 << 16 | childId1).writeLEShortA(0).writeShortA(range1).writeInt2(interfaceId2 << 16 | childId2);
		player.getConnection().getChannel().write(pb.toMessage());
	}
}
