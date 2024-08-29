package org.dementhium.model.player;

import java.util.Iterator;
import java.util.LinkedList;

import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.map.Region;
import org.dementhium.net.ActionSender;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.util.Misc;
/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2
 */
public class PlayerUpdate {

	private final Player player;
	private final LinkedList<Player> localPlayers = new LinkedList<Player>();
	private final boolean[] playerExists = new boolean[2048];

	public PlayerUpdate(Player player) {
		this.player = player;
	}

	public void loginData(MessageBuilder stream) {
		stream.startBitAccess();
		stream.writeBits(30, player.getLocation().getY() | ((player.getLocation().getZ() << 28) | (player.getLocation().getX() << 14)));
		int playerIndex = player.getIndex();
		for (int index = 1; index < 2048; index++) {
			if (index == playerIndex)
				continue;
			Player other = World.getWorld().getPlayers().get(index);
			if (other == null || !other.isOnline()) {
				stream.writeBits(18, 0);
				continue;
			}
			if (!player.getLocation().withinDistance(other.getLocation())) {
				stream.writeBits(18, 0);
				continue;
			}
			stream.writeBits(18, other.getLocation().get18BitsHash());
		}
		stream.finishBitAccess();
	}

	public void sendUpdate() {
		if (player.getRegion().isDidMapRegionChange()) {
			ActionSender.sendMapRegion(player, true);
		}

		boolean chatUpdate = player.getMask().getLastChatMessage() != null;

		MessageBuilder packet = new MessageBuilder(53, PacketType.VAR_SHORT);
		MessageBuilder updateBlock = new MessageBuilder();

		appendPlayerUpdateBlock(updateBlock, player, false);
		packet.startBitAccess();
		if (player.getMask().isUpdateNeeded()) {
			packet.writeBits(1, 1);
			sendLocalPlayerUpdate(packet, player);
		} else {
			packet.writeBits(1, 0);
		}
		if (chatUpdate) {
			ActionSender.sendPublicChatMessage(player, player.getIndex(), player.getRights(), player.getMask().getLastChatMessage());
		}
		Iterator<Player> it = localPlayers.iterator();
		while (it.hasNext()) {
			Player other = it.next();
			if (other != null && other.isOnline() && other.getLocation().withinDistance(player.getLocation(), 14)) {
				if (chatUpdate) {
					ActionSender.sendPublicChatMessage(other, player.getIndex(), player.getRights(), player.getMask().getLastChatMessage());
				}
				appendPlayerUpdateBlock(updateBlock, other, false);
				if (other.getMask().isUpdateNeeded()) {
					packet.writeBits(1, 1);
					packet.writeBits(11, other.getIndex());
					sendLocalPlayerUpdate(packet, other);
					continue;
				}
				packet.writeBits(1, 0);
			} else {
				playerExists[other.getIndex()] = false;
				packet.writeBits(1, 1);
				packet.writeBits(11, other.getIndex());
				removeLocalPlayer(packet, other);
				it.remove();
			}
		}
		int addedCount = 0;
		for (Player other : Region.getLocalPlayers(player.getLocation())) {
			if (addedCount >= 5 || localPlayers.size() >= 255) {
				break;
			}
			if (other == null || !other.isOnline()) {
				continue;
			}
			if (other.getIndex() == player.getIndex() || playerExists[other.getIndex()] || !player.getLocation().withinDistance(other.getLocation(), 14) || !other.isOnline()) {
				continue;
			}
			addedCount++;
			localPlayers.add(other);
			packet.writeBits(1, 1);
			packet.writeBits(11, other.getIndex());
			addLocalPlayer(packet, other);
			appendPlayerUpdateBlock(updateBlock, other, true);
			playerExists[other.getIndex()] = true;
		}
		packet.writeBits(1, 0);
		packet.finishBitAccess();
		packet.writeBytes(updateBlock.getBuffer());
		player.getConnection().write(packet.toMessage());
	}

	private void addLocalPlayer(MessageBuilder packet, Player other) {
		packet.writeBits(2, 0);
		boolean hashUpdated = false;
		packet.writeBits(1, hashUpdated ? 0 : 1);
		if (!hashUpdated) {
			packet.writeBits(2, 3);
			packet.writeBits(18, other.getLocation().get18BitsHash());
		}
		packet.writeBits(6, other.getLocation().getX() - (other.getLocation().getRegionX() << 6));
		packet.writeBits(6, other.getLocation().getY() - (other.getLocation().getRegionY() << 6));
		packet.writeBits(1, 1);
	}

	private void sendLocalPlayerUpdate(MessageBuilder packet, Player other) {
		if (other.getRegion().isDidTeleport()) {
			sendLocalPlayerTeleport(packet, other);
			return;
		}
		int walkDir = other.getWalkingQueue().getWalkDir();
		int runDir = other.getWalkingQueue().getRunDir();
		sendLocalPlayerStatus(packet, walkDir > -1 ? 1 : runDir > -1 ? 2 : 0, true);
		if (walkDir < 0 && runDir < 0)
			return;
		packet.writeBits(walkDir > -1 ? 3 : 4, walkDir > -1 ? walkDir : runDir);
	}

	private void sendLocalPlayerTeleport(MessageBuilder packet, Player other) {
		sendLocalPlayerStatus(packet, 3, true);
		packet.writeBits(1, 1);
		packet.writeBits(30, other.getLocation().get30BitsHash());
	}

	private void sendLocalPlayerStatus(MessageBuilder packet, int type, boolean status) {
		packet.writeBits(1, status ? 1 : 0);
		packet.writeBits(2, type);
	}

	private void removeLocalPlayer(MessageBuilder packet, Player other) {
		sendLocalPlayerStatus(packet, 0, false);
		packet.writeBits(1, 0);
	}

	private void appendPlayerUpdateBlock(MessageBuilder updateBlock, Player other, boolean forceAppearance) {
		if(!other.getMask().isUpdateNeeded() && !forceAppearance) {
			return;
		}
		int maskData = 0;
		if (other.getHits().getSecondaryHit() != null) {
			maskData |= 0x10000;
		}
		if (other.getMask().getLastAnimation() != null) {
			maskData |= 0x80;
		}
		if (other.getMask().isForceMovementUpdate()) {
			maskData |= 0x8000;
		}
		if (other.getMask().getLastGraphics() != null) {
			maskData |= 0x1000;
		}
		if (other.getWalkingQueue().getWalkDir() != -1 || other.getWalkingQueue().getRunDir() != -1) {
			maskData |= 0x10;
		}
		if (other.getMask().getForceText() != null) {
			maskData |= 0x400;
		}
		if (other.getMask().isFaceEntityUpdate()) {
			maskData |= 0x2;
		}
		if (other.getMask().isApperanceUpdate() || forceAppearance) {
			maskData |= 0x20;
		}
		if (other.getMask().getLastHeal() != null) {
			maskData |= 0x100;
		}
		if (other.getWalkingQueue().isDidTele()) {
			maskData |= 0x2000;
		}
		if (other.getHits().getPrimaryHit() != null) {
			maskData |= 0x40;
		}
		if (other.getMask().getFacePosition() != null) {
			maskData |= 0x4;
		}
		if (maskData > 128)
			maskData |= 0x1;
		if (maskData > 32768)
			maskData |= 0x800;
		updateBlock.writeByte(maskData);
		if (maskData > 128)
			updateBlock.writeByte(maskData >> 8);
		if (maskData > 32768)
			updateBlock.writeByte(maskData >> 16);
		if (other.getHits().getSecondaryHit() != null) {
			applyHit2Mask(other, updateBlock);
		}
		if (other.getMask().getLastAnimation() != null) {
			applyAnimationMask(other, updateBlock);
		}
		if (other.getMask().isForceMovementUpdate()) {
			applyForceMovementMask(other, updateBlock);
		}
		if (other.getMask().getLastGraphics() != null) {
			applyGraphicMask(other, updateBlock);
		}
		if (other.getWalkingQueue().getWalkDir() != -1 || other.getWalkingQueue().getRunDir() != -1) {
			applyMovementMask(other, updateBlock);
		}
		if (other.getMask().getForceText() != null) {
			applyForceText(other, updateBlock);
		}
		if (other.getMask().isFaceEntityUpdate()) {
			applyTurnToMask(other, updateBlock);
		}
		if (other.getMask().isApperanceUpdate() || forceAppearance) {
			applyAppearanceMask(other, updateBlock);
		}
		if (other.getMask().getLastHeal() != null) {
			applyHealMask(other, updateBlock);
		}
		if (other.getWalkingQueue().isDidTele()) {
			applyTeleTypeMask(updateBlock);
		}
		if (other.getHits().getPrimaryHit() != null) {
			applyHitMask(other, updateBlock);
		}
		if (other.getMask().getFacePosition() != null) {
			applyTurnToCoordMask(other, updateBlock);
		}
	}

	private void applyForceMovementMask(Player p, MessageBuilder updateBlock) {
		Location myLocation = p.getLocation();
		Location fromLocation = p.getLocation();
		Location toLocation = Location.create(p.getForceWalk()[0], p.getForceWalk()[1], 0);

		int distfromx = 0;
		int distfromy = 0;
		boolean positiveFromX = false;
		boolean positiveFromY = false;
		int distanceToX = 0;
		int distanceToY = 0;
		boolean positiveToX = false;
		boolean positiveToY = false;

		if (myLocation.getX() < fromLocation.getX()) {
			positiveFromX = true;
		}
		if (myLocation.getY() < fromLocation.getY()) {
			positiveFromY = true;
		}
		if (fromLocation.getX() < toLocation.getX()) {
			positiveToX = true;
		}
		if (fromLocation.getY() < toLocation.getY()) {
			positiveToY = true;
		}

		if (positiveFromX) {
			distfromx = fromLocation.getX() - myLocation.getX();
		} else {
			distfromx = myLocation.getX() - fromLocation.getX();
		}
		if (positiveFromY) {
			distfromy = fromLocation.getY() - myLocation.getY();
		} else {
			distfromy = myLocation.getY() - fromLocation.getY();
		}
		if (positiveToX) {
			distanceToX = toLocation.getX() - fromLocation.getX();
		} else {
			distanceToX = fromLocation.getX() - toLocation.getX();
		}

		if (positiveToY) {
			distanceToY = toLocation.getY() - fromLocation.getY();
		} else {
			distanceToY = fromLocation.getY() - toLocation.getY();
		}

		updateBlock.writeByteA(positiveFromX ? distfromx : -distfromx);
		updateBlock.writeByteC(positiveFromY ? distfromy : -distfromy);
		updateBlock.writeByteA(positiveToX ? distanceToX : -distanceToX);
		updateBlock.writeByteA(positiveToY ? distanceToY : -distanceToY);

		updateBlock.writeLEShortA(p.getForceWalk()[2]);
		updateBlock.writeLEShort(p.getForceWalk()[3]);
		updateBlock.writeByte(p.getForceWalk()[4]);

	}

	private static void applyTurnToCoordMask(Player p, MessageBuilder updateBlock) {
		updateBlock.writeLEShort(Misc.getFacingDirection(p.getLocation().getX(), p.getLocation().getY(), p.getMask().getFacePosition().getX(), p.getMask().getFacePosition().getY()));
	}

	private static void applyForceText(Player p, MessageBuilder updateBlock) {
		updateBlock.writeRS2String(p.getMask().getForceText().getLastForceText());
	}

	public static void applyForceMovement(Player p, MessageBuilder updateBlock) {

	}

	public static void applyTurnToMask(Player p, MessageBuilder outStream) {
		if(p.getMask().getInteractingEntity() != null) {
			outStream.writeShortA(p.getMask().getInteractingEntity().getClientIndex());
		} else {
			outStream.writeShortA(-1);
		}
	}

	public static void applyTeleTypeMask(MessageBuilder outStream) {
		outStream.writeByteC(127);
	}

	public static void applyHitMask(Player p, MessageBuilder outStream) {
		outStream.writeSmart(p.getHits().getHitDamage1());
		outStream.writeByte(p.getHits().getHitType1());
		int Amthp = p.getSkills().getHitPoints();
		int Maxhp = p.getSkills().getLevelForXp(3) * 10;
		if (Amthp > Maxhp)
			Amthp = Maxhp;
		outStream.writeByte(Amthp * 255 / Maxhp);
	}

	public static void applyHit2Mask(Player p, MessageBuilder outStream) {
		outStream.writeSmart(p.getHits().getHitDamage2());
		outStream.writeByteS(p.getHits().getHitType2());
	}

	public static void applyHealMask(Player p, MessageBuilder outStream) {
		outStream.writeShort(p.getMask().getLastHeal().getHealDelay());
		outStream.writeByteS(p.getMask().getLastHeal().getBarDelay());
		outStream.writeByteS(p.getMask().getLastHeal().getHealSpeed());
	}

	public static void applyAnimationMask(Player p, MessageBuilder outStream) {
		outStream.writeShortA(p.getMask().getLastAnimation().getId());
		outStream.writeByteA(p.getMask().getLastAnimation().getDelay());
	}

	public static void applyGraphicMask(Player p, MessageBuilder outStream) {
		outStream.writeShortA(p.getMask().getLastGraphics().getId());
		outStream.writeInt(p.getMask().getLastGraphics().getDelay());
		outStream.writeByte(p.getMask().getLastGraphics().getHeight());
	}

	public static void applyMovementMask(Player p, MessageBuilder outStream) {
		outStream.writeByteC(p.getWalkingQueue().getWalkDir() != -1 ? 1 : 2);
	}

	public static void applyAppearanceMask(Player p, MessageBuilder outStream) {
		MessageBuilder playerUpdate = new MessageBuilder();
		NPCDefinition def = p.getAppearence().getNpcType() != -1 ? NPCDefinition.forId(p.getAppearence().getNpcType()) : null;
		int hash = 0;
		hash |= p.getAppearence().getGender() & 0x1;
		playerUpdate.writeByte(hash);
		playerUpdate.writeByte(p.getDefinition().getTitle()); // mobi arms titles
		playerUpdate.writeByte(p.getHeadIcons().getIcon()); // pk icon
		playerUpdate.writeByte(p.getPrayer().getHeadIcon());
		if (p.getAppearence().getNpcType() == -1) {
			for (int i = 0; i < 4; i++) {
				if (p.getEquipment().get(i) == null)
					playerUpdate.writeByte(0);
				else
					playerUpdate.writeShort(32768 + p.getEquipment().get(i).getDefinition().getEquipId());
			}
			if (p.getEquipment().get(Equipment.SLOT_CHEST) != null) {
				playerUpdate.writeShort(32768 + p.getEquipment().get(
						Equipment.SLOT_CHEST).getDefinition().getEquipId());
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[2]);
			}
			if (p.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
				playerUpdate.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_SHIELD).getDefinition().getEquipId());
			} else {
				playerUpdate.writeByte((byte) 0);
			}
			Item chest = p.getEquipment().get(Equipment.SLOT_CHEST);
			if (chest != null) {
				if (!Equipment.isFullBody(chest.getDefinition())) {
					playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[3]);
				} else {
					playerUpdate.writeByte((byte) 0);
				}
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[3]);
			}
			if (p.getEquipment().get(Equipment.SLOT_LEGS) != null) {
				playerUpdate.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_LEGS).getDefinition().getEquipId());
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[5]);
			}
			Item hat = p.getEquipment().get(Equipment.SLOT_HAT);
			if (hat != null) {
				if (!Equipment.isFullHat(hat.getDefinition()) && !Equipment.isFullMask(hat.getDefinition())) {
					playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[0]);
				} else {
					playerUpdate.writeByte((byte) 0);
				}
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[0]);
			}
			if (p.getEquipment().get(Equipment.SLOT_HANDS) != null) {
				playerUpdate.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_HANDS).getDefinition().getEquipId());
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[4]);
			}
			if (p.getEquipment().get(Equipment.SLOT_FEET) != null) {
				playerUpdate.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_FEET).getDefinition().getEquipId());
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[6]);
			}
			if (hat != null) {
				if (!Equipment.isFullMask(hat.getDefinition())) {
					playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[1]);
				} else {
					playerUpdate.writeByte((byte) 0);
				}
			} else {
				playerUpdate.writeShort(0x100 + p.getAppearence().getLook()[1]);
			}
		} else {
			playerUpdate.writeShort(-1);
			playerUpdate.writeShort(def.getId());
			playerUpdate.writeByte(0);
		}
		for (int j = 0; j < 5; j++) {
			playerUpdate.writeByte(p.getAppearence().getColour()[j]);
		}
		playerUpdate.writeShort(p.getAppearence().getNpcType() != -1 && def != null ? def.getRenderAnim() : p.getEquipment().getRenderAnim());
		playerUpdate.writeRS2String(p.getCustomName());
		playerUpdate.writeByte(p.getSkills().getCombatLevel());
		playerUpdate.writeShort(0);
		playerUpdate.writeShort(0);
		outStream.writeByteS(playerUpdate.position());
		outStream.addBytes128(playerUpdate.getBuffer());
	}

}
