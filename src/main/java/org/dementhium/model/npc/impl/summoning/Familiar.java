package org.dementhium.model.npc.impl.summoning;

import org.dementhium.content.misc.Following;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author Steve
 * 
 */
public abstract class Familiar extends NPC {

	private Player owner;
	public boolean bigNpc = false;
	private CombatAction familiarCombatAction = new FamiliarCombatAction();
	private int originalId;
	
	public Familiar(int npcId, Player owner, boolean big) {
		super(npcId, owner.getLocation());
		this.originalId = npcId;
		this.bigNpc = big;
		this.owner = owner;
		Following.familiarFollow(this, owner);
		getMask().setSwitchId(originalId);
		sendInterface();
		callToPlayer();
		World.getWorld().getNpcs().add(this);
	}
	
	private void sendInterface() {
		ActionSender.sendConfig(owner, 448, getId());
		ActionSender.sendConfig(owner, 1174, getId());
		ActionSender.sendConfig(owner,1175, 102025930);
		ActionSender.sendConfig(owner, 1175, 102025930);
		ActionSender.sendConfig(owner, 1171, 20480);
		ActionSender.sendConfig(owner, 1171, 20480);
		ActionSender.sendConfig(owner, 1176, 7424);
		ActionSender.sendConfig(owner, 1801, 48);
		ActionSender.sendConfig(owner, 1231, 333839);
		ActionSender.sendConfig(owner, 1160, getHeadAnimConfig(getId()));
		ActionSender.sendConfig(owner, 1175, 102025930);
		ActionSender.sendConfig(owner, 1175, 102025930);
		ActionSender.sendConfig(owner,1160, getHeadAnimConfig(getId()));
		ActionSender.sendConfig(owner, 108, 1);
		ActionSender.sendInterface(owner,1, 746, 51, 662);
		ActionSender.sendInterface(owner, 1, 746, 34, 884);
		ActionSender.sendAMask(owner, -1, -1, 746, 125, 0, 2);
		ActionSender.sendAMask(owner, -1, -1, 884, 11, 0, 2);
		ActionSender.sendAMask(owner, -1, -1, 884, 12, 0, 2);
		ActionSender.sendAMask(owner, -1, -1, 884, 13, 0, 2);
		ActionSender.sendConfig(owner, 1175, 102025930);
		ActionSender.sendConfig(owner, 1175, 102025930);
		ActionSender.sendInterface(owner, 1, 746, 51, 662);
	}

	public static int getHeadAnimConfig(int npcId) {
		switch(npcId) {
		case 6873://yak
			return 8388608;
		}
		return -1;
	}
	
	@Override
	public void tick() {
		if (!owner.isOnline()) {
			dismiss();
			return;
		}
		if (inWilderness() && (getMask().getSwitchId() == originalId || getMask().getSwitchId() == -1)) {
			getMask().setSwitchId(getId() + 1);
		} else if (getMask().getSwitchId() != originalId) {
			getMask().setSwitchId(originalId);
		}
		if (owner.getCombatState().getVictim() == null && getCombatState().getVictim() == null) {
			if (getLocation().getDistance(owner.getLocation()) > 1) {
				if (getLocation().getDistance(owner.getLocation()) >= 20) {
					callToPlayer();
					return;
				}
				turnTo(owner);
				Following.familiarFollow(this, owner);
			}
		} else if (getCombatState().getVictim() == null && isMulti()) {
			getCombatState().setVictim(owner.getCombatState().getVictim());
			Following.familiarFollow(this, owner.getCombatState().getVictim());
		} else if (getCombatState().getVictim() != null) {
			Following.familiarFollow(this, getCombatState().getVictim());
		}
	}
	

	public void callToPlayer() {
		resetCombat();
		teleport(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ());
		turnTo(owner);
		getCombatState().setVictim(null);
		graphics(bigNpc ? 1315 : 1314);
	}
		
	public abstract void specialAttack(Mob victim);

	@Override
	public CombatAction getCustomCombatAction() {
		return familiarCombatAction;
	}
	public class FamiliarCombatAction extends CombatAction {

		@Override
		public CombatHit hit(Mob mob, Mob victim) {
			return getHit(mob, victim);
		}

		@Override
		public boolean canAttack(Mob mob, Mob victim) {
			return canAttackPlayer(mob, victim);
		}
	}

	public abstract CombatHit getHit(Mob mob, Mob victim);

	public boolean canAttackPlayer(Mob mob, Mob victim) {
		if (isDead() || victim.isDead()) {
			resetCombat();
			return false;
		}
		if (getCombatState().getVictim() == null) {
			resetCombat();
			return false;
		}
		if (getLocation().getDistance(victim.getLocation()) > 1) {
			Following.combatFollow(mob, victim);
			return false;
		}
		return true;
	}

	public void dismiss() {
		owner.setAttribute("familiar", null);//sec brb
		resetTurnTo();
		World.getWorld().getNpcs().remove(this);
	}

}
