package org.dementhium.model.player;

import java.util.Iterator;
import java.util.LinkedList;

import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.net.message.Message;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.net.message.Message.PacketType;

public class Gni {

	private final Player player;

	private final LinkedList<NPC> localNpcs = new LinkedList<NPC>();

	public Gni(Player player) {
		this.player = player;
	}

	public void sendUpdate() {
		player.getConnection().write(createPacket());
	}

	public Message createPacket() {
		MessageBuilder packet = new MessageBuilder(6, PacketType.VAR_SHORT);
		MessageBuilder updateBlock = new MessageBuilder();
		processGlobalNpcInform(packet, updateBlock);
		packet.writeBytes(updateBlock.getBuffer());
		return packet.toMessage();
	}

	private void processGlobalNpcInform(MessageBuilder packet, MessageBuilder updateBlock) {
		processInScreenNpcs(packet, updateBlock);
		addInScreenNpcs(packet, updateBlock);
	}


	private void addInScreenNpcs(MessageBuilder packet, MessageBuilder updateBlock) {
		if(localNpcs.size() < 250) {
			for (NPC npc : Region.getLocalNPCs(player.getLocation())) {
				if(npc == null || npc.destroyed() || !player.getLocation().withinDistance(npc.getLocation()) || npc.isHidden()) {
					continue;
				}
				if(localNpcs.contains(npc)) {
					continue;
				}
				packet.writeBits(15, npc.getIndex());
				packet.writeBits(14, npc.getId());
				packet.writeBits(1, 1);
				int y = npc.getLocation().getY() - player.getLocation().getY();
				int x = npc.getLocation().getX() - player.getLocation().getX();
				if (x < 0)
					x += 32;
				if (y < 0)
					y += 32;
				packet.writeBits(5, y);
				packet.writeBits(5, x);
				packet.writeBits(1, npc.getMask().isUpdateNeeded() ? 1 : 0);
				packet.writeBits(2, npc.getLocation().getZ());
				packet.writeBits(3, npc.getFaceDir());
				localNpcs.add(npc);
				if (npc.getMask().isUpdateNeeded()) {
					appendUpdateBlock(npc, updateBlock);
				}
			}
		}
		packet.writeBits(15, 32767);
		packet.finishBitAccess();
	}

	private void processInScreenNpcs(MessageBuilder packet, MessageBuilder updateBlock) {
		packet.startBitAccess();
		packet.writeBits(8, localNpcs.size());
		Iterator<NPC> it = localNpcs.iterator();
		while(it.hasNext()) {
			NPC npc = it.next();
			if(npc == null || npc.destroyed() || npc.isHidden() || npc.isTeleporting() || !player.getLocation().withinDistance(npc.getLocation())) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
				it.remove();
				continue;
			}
			int sprite = npc.getWalkingQueue().getWalkDir();
			if(sprite == -1) {
				if(npc.getMask().isUpdateNeeded()) {
					packet.writeBits(1, 1);
					packet.writeBits(2, 0);
				} else {
					packet.writeBits(1, 0);
				}
			} else {
				packet.writeBits(1, 1);
				packet.writeBits(2, 1);
				packet.writeBits(3, sprite);
				packet.writeBits(1, npc.getMask().isUpdateNeeded() ? 1 : 0);
			}
			if (npc.getMask().isUpdateNeeded()) {
				appendUpdateBlock(npc, updateBlock);
			}
		}
	}

	private static void appendUpdateBlock(NPC npc, MessageBuilder out) {
		int maskData = 0x0;
		if(npc.getMask().getSwitchId() > -1) {
			maskData |= 0x40;
		}
		if(npc.getMask().getLastAnimation() != null) {
			maskData |= 0x4;
		}
		if(npc.getHits().getPrimaryHit() != null) {
			maskData |= 0x8;
		}
		if(npc.getMask().isFaceEntityUpdate()) {
			maskData |= 0x20;
		}
		if(npc.getHits().getSecondaryHit() != null) {
			maskData |= 0x400;
		}
		if(npc.getMask().getLastGraphics() != null) {
			maskData |= 0x10;
		}
		if(npc.getMask().isForceTextUpdate()) {
			maskData |= 0x80;
		}
		if(maskData > 128)
			maskData |= 0x1;
		out.writeByte(maskData);
		if(maskData > 128)
			out.writeByte(maskData >> 8);
		if(npc.getMask().getSwitchId() > -1) {
			applySwitchMask(npc, out);
		}
		if(npc.getMask().getLastAnimation() != null) {
			applyAnimationMask(npc, out);
		}
		if(npc.getHits().getPrimaryHit() != null) {
			applyHitUpdate(npc, out);
		}
		if(npc.getMask().isFaceEntityUpdate()) {
			applyFaceEntity(npc, out);
		}
		if(npc.getHits().getSecondaryHit() != null) {
			applyHit2Update(npc, out);
		}
		if(npc.getMask().getLastGraphics() != null) {
			applyGraphicMask(npc, out);
		}
		if(npc.getMask().isForceTextUpdate()) {
			applyForceText(npc, out);
		}
	}

	private static void applySwitchMask(NPC npc, MessageBuilder out) {
		out.writeLEShortA(npc.getMask().getSwitchId());
	}

	public static void applyFaceEntity(NPC npc, MessageBuilder out) {
		if(npc.getMask().getInteractingEntity() != null) {
			out.writeShortA(npc.getMask().getInteractingEntity().getClientIndex());
		} else {
			out.writeShortA(-1);
		}
	}

	private static void applyHitUpdate(NPC npc, MessageBuilder out) {
		out.writeSmart(npc.getHits().getHitDamage1());
		out.writeByteA(npc.getHits().getHitType1());
		int Amthp = npc.getHp();
		int Maxhp = npc.getMaxHp();
		if (Amthp > Maxhp)
			Amthp = Maxhp;
		out.writeByteA(Amthp*255/Maxhp);
	}

	private static void applyHit2Update(NPC npc, MessageBuilder out) {
		out.writeSmart(npc.getHits().getHitDamage2());
		out.writeByte(npc.getHits().getHitType2());
	}

	private static void applyForceText(NPC npc, MessageBuilder out) {
		out.writeRS2String(npc.getMask().getForceText().getLastForceText());
		npc.getMask().setForceTextUpdate(false);
	}

	private static void applyAnimationMask(NPC npc, MessageBuilder out) {
		out.writeLEShort(npc.getMask().getLastAnimation().getId());
		out.writeByteA(npc.getMask().getLastAnimation().getDelay());
	}

	private static void applyGraphicMask(NPC npc, MessageBuilder out) {
		out.writeShortA(npc.getMask().getLastGraphics().getId());
		out.writeInt1(npc.getMask().getLastGraphics().getDelay());
		out.writeByteA(npc.getMask().getLastGraphics().getHeight());
	}

}
