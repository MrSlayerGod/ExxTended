package org.dementhium.model.combat;

import org.dementhium.content.misc.Following;
import org.dementhium.event.Tickable;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 * 
 * @author 'Mystic Flow
 * 
 */
public final class Combat {

	/**
	 * 
	 * @author 'Mystic Flow
	 * 
	 */
	public static enum FightType {
		RANGE(Skills.Range),
		MAGIC(Skills.Magic),
		MELEE(Skills.Strength);

		private final int skill;

		private FightType(int skill) {
			this.skill = skill;
		}

		public int getSkill() {
			return skill;
		}
	}

	public static enum CombatStyle {
		CRUSH, DRAGON_FIRE, MAGIC, 
		NONE, RANGED, SLASH, 
		STAB, VENGEANCE
	}
	public static enum CombatType {
		ACCURATE, AGGRESSIVE, CONTROLLED, DEFENSIVE, LONG_RANGE, NONE, RAPID
	}

	public static boolean canAttack(Mob mob, Mob victim) {
		if (mob == null || victim == null) {
			return false;
		}
		if (mob.isDead() || victim.isDead()) {
			mob.resetCombat();
			return false;
		}
		if(!CombatUtils.isDeepEnoughInWild(mob, victim)){
			mob.resetCombat();
			return false;
		}
		if(victim.isNPC() && mob.isPlayer()) {
			if(victim.getNpc().getOriginalLocation().distance(mob.getLocation()) > 14 && victim.getNpc().getLocation().distance(mob.getLocation()) > victim.size()) {
				if(mob.getFightType() == FightType.RANGE) {
					Following.combatFollow(mob, victim);
					return false;
				}
			}
		}

		boolean moved = false;
		if(mob.getLocation().equals(victim.getLocation()) && !mob.getCombatState().isFrozen() && !victim.getWalkingQueue().isMoving()) {
			int random = Misc.random(3);
			int moveX = mob.getLocation().getX(), moveY = mob.getLocation().getY();
			switch(random) {
			case 0:
				moveX--;
				break;
			case 1:
				moveY++;
				break;
			case 2:
				moveX++;
				break;
			case 3:
				moveY--;
				break;
			}
			mob.requestWalk(moveX, moveY);
			moved = true;
		}
		if(mob.getLocation().equals(victim.getLocation()) && !moved) {
			return false;
		}
		if (mob.isPlayer() && victim.isPlayer()) {
			if (!mob.getPlayer().refreshAttackOptions() && !mob.getPlayer().getLocation().atGe() || mob.getPlayer().getLocation().atGeLobby() || !victim.getPlayer().refreshAttackOptions() && !victim.getPlayer().getLocation().atGe() || victim.getPlayer().getLocation().atGeLobby()) {
				ActionSender.sendMessage(mob.getPlayer(), "You cannot attack players outside of the wilderness!");
				mob.resetCombat();
				return false;
			}
			if(World.getWorld().getAreaManager().getAreaByName("Edgeville").contains(mob.getLocation()) || mob.getLocation().atGe() && victim.getLocation().atGe()) {
				if(victim.getCombatState().getLastAttacker() != null) {
					if(victim.getCombatState().getLastAttacker().getIndex() != mob.getIndex()) {
						mob.getPlayer().sendMessage("That player is already in combat!");
						mob.resetCombat();
						return false;
					}
				}
				if(mob.getCombatState().getLastAttacker() != null) {
					if (mob.getCombatState().getLastAttacker().getIndex() != victim.getIndex()) {
						mob.getPlayer().sendMessage("You are already in combat!");
						mob.resetCombat();
						return false;
					}
				}
			}
		}
		if (mob.isPlayer() && victim.isNPC()) {
			if(!World.getWorld().getAreaManager().getAreaByName("Edgeville").contains(mob.getLocation()) || mob.getLocation().atGe() && victim.getLocation().atGe()) {
				if(victim.getCombatState().getLastAttacker() != null) {
					if(victim.getCombatState().getLastAttacker().getIndex() != mob.getIndex()) {
						mob.getPlayer().sendMessage("Someone else is fighting that.");
						mob.resetCombat();
						return false;
					}
				}
				if(mob.getCombatState().getLastAttacker() != null) {
					if (mob.getCombatState().getLastAttacker().getIndex() != victim.getIndex()) {
						mob.getPlayer().sendMessage("You are already in combat!");
						mob.resetCombat();
						return false;
					}
				}
			}
		}
		if (mob.isNPC() && victim.isPlayer()) {
			if(!World.getWorld().getAreaManager().getAreaByName("Edgeville").contains(mob.getLocation()) || mob.getLocation().atGe() && victim.getLocation().atGe()) {
				if(victim.getCombatState().getLastAttacker() != null) {
					if(victim.getCombatState().getLastAttacker().getIndex() != mob.getIndex()) {
						mob.resetCombat();
						return false;
					}
				}
				if(mob.getCombatState().getLastAttacker() != null) {
					if (mob.getCombatState().getLastAttacker().getIndex() != victim.getIndex()) {
						mob.resetCombat();
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void process(final Mob mob) {
		final Mob victim = mob.getCombatState().getVictim();
		if (!canAttack(mob, victim)) {
			return;
		}
		final FightType type = mob.getFightType();
		final CombatAction action = CombatAction.forType(mob, type);
		if(!action.canAttack(mob, victim)) {
			return;
		}
		victim.getCombatState().setLastAttacker(mob);
		boolean timerOver = type == FightType.MAGIC ? mob.getCombatState().getSpellDelay() == 0 : mob.getCombatState().getAttackDelay() == 0;
		if (timerOver && mob.getAttribute("spellQueued") == null) {
			mob.turnTo(victim); 
			final CombatHit hit = action.hit(mob, victim);
			if (hit != null) {
				World.getWorld().submit(new Tickable(hit.getTicks()) {
					@Override
					public void execute() {
						mob.getCombatState().setLastHit(hit.getDamage());
						IdentifierManager.get("combat_after_effect").identify(mob, victim, hit, type);
						victim.hit(hit.getDamage());
						this.stop();
					}
				});
			}
		}
	}

	public static boolean diagonal(Location l, Location l1) {
		int xDial = Math.abs(l.getX() - l1.getX());
		int yDial = Math.abs(l.getY() - l1.getY());
		return xDial == 1 && yDial == 1;
	}

}
