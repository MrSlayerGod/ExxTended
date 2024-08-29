package org.dementhium.model.mask;

import org.dementhium.model.Mob;
import org.dementhium.model.Location;
import org.dementhium.model.player.ChatMessage;
import org.dementhium.model.player.Player;

public class Mask {

	private Mob mob;
	
	private ChatMessage lastChatMessage;
	private Graphics lastGraphics;
	private Animation lastAnimation;
	private Heal lastHeal;
	private Location facePosition;
	
	private Mob interactingEntity;

	private ForceText forceText;

	private boolean forceTextUpdate;
	private boolean appearanceUpdate;
	private boolean teleport;
	private boolean faceEntityUpdate;
	private boolean forceMovementUpdate;
	
	private int switchId = -1;

	public Mask(Mob mob) {
		this.mob = mob;
		this.setApperanceUpdate(true);
	}

	public void reset() {
		switchId = -1;
		lastChatMessage = null;
		lastGraphics = null;
		facePosition = null;
		lastAnimation = null;
		lastHeal = null;
		appearanceUpdate = false;
		faceEntityUpdate = false;
		forceText = null;
		forceMovementUpdate = false;
		this.setTeleport(false);
	}

	public Mob getPlayer() {
		return mob;
	}

	public boolean isUpdateNeeded() {
		if(forceMovementUpdate || switchId > -1 || facePosition != null || forceText != null || appearanceUpdate || faceEntityUpdate || lastChatMessage != null || lastGraphics != null || lastAnimation != null || lastHeal != null) {
			return true;
		}
		if(mob.isPlayer()) {
			Player player = mob.getPlayer();
			return player.getHits().update() || player.getWalkingQueue().getWalkDir() != -1 || player.getWalkingQueue().getRunDir() != -1 || player.getWalkingQueue().isDidTele();
		} else if(mob.isNPC()) {
			return mob.getNpc().getHits().update();
		}
		return false;
	}

	public void setLastChatMessage(ChatMessage lastChatMessage) {
		this.lastChatMessage = lastChatMessage;
	}

	public ChatMessage getLastChatMessage() {
		return lastChatMessage;
	}

	public void setLastGraphics(Graphics lastGraphics) {
		this.lastGraphics = lastGraphics;
	}

	public Graphics getLastGraphics() {
		return lastGraphics;
	}

	public void setLastAnimation(Animation lastAnimation) {
		this.lastAnimation = lastAnimation;
	}

	public Animation getLastAnimation() {
		return lastAnimation;
	}
	
	public void setLastHeal(Heal lastHeal) {
		this.lastHeal = lastHeal;
	}

	public Heal getLastHeal() {
		return lastHeal;
	}

	public void setLastForceText(ForceText text) {
		setForceText(text);
	}

	public boolean isForceTextUpdate() {
		return forceTextUpdate;
	}

	public void setForceText(ForceText forceText) {
		this.forceText = forceText;
		forceTextUpdate = true;
	}
	
	public void setForceTextUpdate(boolean forceText) {
		this.forceTextUpdate = forceText;
	}

	public ForceText getForceText() {
		return forceText;
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

	public boolean isTeleport() {
		return teleport;
	}


	public void setApperanceUpdate(boolean appearanceUpdate) {
		this.appearanceUpdate = appearanceUpdate;
	}

	public boolean isApperanceUpdate() {
		return appearanceUpdate;
	}

	public void setInteractingEntity(Mob interactingEntity) {
		this.interactingEntity = interactingEntity;
		this.faceEntityUpdate = true;
	}

	public Mob getInteractingEntity() {
		return interactingEntity;
	}
	
	public boolean isFaceEntityUpdate() {
		return faceEntityUpdate;
	}

	public void setFacePosition(Location facePosition) {
		this.facePosition = facePosition;
	}

	public Location getFacePosition() {
		return facePosition;
	}

	public void setSwitchId(int switchId) {
		this.switchId = switchId;
	}

	public int getSwitchId() {
		return switchId;
	}

	public void setForceMovementUpdate(boolean forceMovementUpdate) {
		this.forceMovementUpdate = forceMovementUpdate;
	}

	public boolean isForceMovementUpdate() {
		return forceMovementUpdate;
	}
}
