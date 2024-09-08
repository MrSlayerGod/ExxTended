package org.dementhium.model.combat.impl;

import org.dementhium.content.misc.Following;
import org.dementhium.content.skills.Prayer;
import org.dementhium.event.Tickable;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.Mob;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.CombatState;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Combat.CombatStyle;
import org.dementhium.model.combat.Combat.CombatType;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.util.Misc;


/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2 <for small modifications to support specials>
 * @author Steve <added some specials>
 */
public final class MeleeCombat extends CombatAction {

	private static final CombatAction INSTANCE = new MeleeCombat();

	public static CombatAction getAction() {
		return INSTANCE;
	}

	public static final int DEFAULT_TICKS = 1;

	private MeleeCombat() {

	}

	@Override
	public CombatHit hit(Mob mob, final Mob victim) { //just call this lol
		if(mob.isPlayer()) {
			if(!(mob.getPlayer().getAttribute("spellQueued") == null)) {
				mob.getPlayer().removeAttribute("spellQueued");
			}
		}
		mob.getCombatState().setSpellDelay(6);
		mob.getCombatState().setAttackDelay(mob.isNPC() ? 5 : mob.getPlayer().getAttackDelay());
		int damage = damage(mob, FightType.MELEE);
		int attackBonus;
		int defenceBonus;
		boolean specialPerformed = false;
		if (mob.usingSpecial()) {
			Player player = mob.getPlayer();
			if(!performMeleeSpecials(player, victim, damage)) {
				player.reverseSpecialActive();
				ActionSender.sendChatMessage(player, 0, "You do not have enough special attack to perform this action.");
				return null;
			} else {
				player.reverseSpecialActive();
				specialPerformed = true;
			}
		}
		if(specialPerformed && mob.isPlayer() && mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_WEAPON) == 14484) {
			return null;
		}
		attackBonus = getAttackBonus(mob);
		defenceBonus = getDefenceBonus(mob, victim);
		if(victim.isPlayer()) {
			if(victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.MELEE)) {
				attackBonus *= 0.40;
			}
		}
		if(!specialPerformed) {
			if(Misc.random(attackBonus) < Misc.random(defenceBonus)) {
				damage = 0;
			}
		} else {
			if(Misc.random(attackBonus) * CombatUtils.specialPower(mob.getPlayer().getEquipment().getSlot(3)) < Misc.random(defenceBonus)) {
				damage = 0;
			}
		}
		float accuracy = (attackBonus - defenceBonus) / 255;
		if (accuracy < 0.10) {
			accuracy = 0.10F;
		} else if (accuracy > 0.90) {
			accuracy = 0.90F;
		}
		float average = damage * accuracy;
		if (damage > 0 && !specialPerformed) {
			damage = calculateHit(damage, average);
		} else if(damage == 0 && !specialPerformed && mob.isPlayer() && mob.getPlayer().hitCap) {
			damage = calculateHit(damage, average);
		} else if(specialPerformed) {
			if(mob.isPlayer() && mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_WEAPON) == 19784) {
				Player player = mob.getPlayer();
				int magicBonus = (int) ((player.getSkills().getLevel(Skills.Magic) + player.getBonuses().getBonus(Bonuses.MAGIC)) * 1.785);
				damage = (int) (calculateHit(damage, average) + Misc.random(magicBonus) + magicBonus);
			} else {
				if(damage > 0) {
					damage = (int) ((Misc.random(calculateHit(damage, average) / 2) + (damage / 2)) * CombatUtils.specialPower(mob.getPlayer().getEquipment().getSlot(3)));
				}
				if(mob.isPlayer() && mob.getPlayer().hitCap) {
					damage = (int) ((Misc.random(calculateHit(damage, average) / 2) + (damage / 2)) * CombatUtils.specialPower(mob.getPlayer().getEquipment().getSlot(3)));
				}
			}
		}
		if(!specialPerformed) {
			mob.animate(mob.getAttackAnimation());
		}
		final boolean animating = victim.isAnimating();
		World.getWorld().submit(new Tickable(1) {
			public void execute() {
				if(!animating && !victim.isAnimating()) {
					victim.animate(victim.getDefenceAnimation());
				}
				this.stop();
			}
		});
		if(damage > 0 && mob.isPlayer()) {
			appendExperience(mob.getPlayer(), damage);
		}
		return new CombatHit(mob, victim, damage, 1);
	}

	@Override
	public boolean canAttack(Mob mob, Mob victim) {
		if(mob.getLocation().distance(victim.getLocation()) != 1) {
			if(mob.getLocation().distance(victim.getLocation()) <= 3 && mob.getWalkingQueue().isMoving() && victim.getWalkingQueue().isMoving()) {
				Following.combatFollow(mob, victim);
				return true;
			}
			if(mob.isPlayer()) {
				Following.combatFollow(mob, victim);
				return false;
			} else {
				Following.npcFollow(mob.getNpc(), victim);
			}
			return false;
		}
		if(Combat.diagonal(mob.getLocation(), victim.getLocation())) {
			if(!mob.getCombatState().isFrozen() && !mob.getWalkingQueue().isMoving() && !victim.getWalkingQueue().isMoving()) {
				int moveX = victim.getLocation().getX(), moveY = mob.getLocation().getY();
				World.getWorld().doPath(new AStarPathFinder(), mob, moveX, moveY);
			}
			return true;
		}
		return true;
	}

	private boolean performMeleeSpecials(final Player player, final Mob victim, int damage) {
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int id = wep != null ? wep.getId() : -1;
		if(player.getSpecialAmount() >= getSpecialDeduction(wep)) {
			switch(id) {
			case 11694://ags
				player.animate(7074);
				player.graphics(1222, 100 << 16);
				break;
			case 11696://bgs
				player.animate(7073);
				player.graphics(1223, 30 << 16);
				break;
			case 11700:
				player.animate(7070);
				player.graphics(2110);
				victim.graphics(2111);
				victim.getCombatState().setFrozenTime(20);
				break;
			case 1305: //d long
				player.graphics(248, 100 << 16);
				player.animate(1658);
				break;
			case 13899:
				player.animate(10502);
				break;
			case 13905:
				player.animate(10499);
				player.graphics(1835, 0);
				break;
			case 4153:
				player.animate(1667);
				player.graphics(340);
				break;
			case 13902:
				player.animate(10505);
				player.graphics(1840);
				if(victim != null && victim.isPlayer()) {
					//victim.getPlayer().getSkills().decreaseLevelToZero(3, (int) (victim.getPlayer().getSkills().getLevel(3) * 1.30));
				}
				break;
			case 4587: //d scim
				player.animate(15078);
				player.graphics(347, (100 << 16));
				break;
			case 11698://sgs
				player.animate(7071);
				player.graphics(1220);
				int hitpointsHeal = damage / 2;
				int prayerHeal = damage / 4;
				player.getSkills().increaseLevelToMaximum(Skills.Hitpoints, hitpointsHeal);
				player.getSkills().increaseLevelToMaximum(Skills.Prayer, prayerHeal);
				break;
			case 14484:
				player.graphics(1950);
				player.animate(10961);
				final int hit = Misc.random(damage);
				World.getWorld().submit(new Tickable(1) {
					@Override
					public void execute() {
						int second = hit / 2, third = second / 2, fourth = third + 1; 
						if(hit == 0) second = third = 0;
						if(third < 1) fourth = 0;
						victim.hit(hit);
						victim.hit(second);
						final int thirdFinal = third, fourthFinal = fourth;
						World.getWorld().submit(new Tickable(1) {
							@Override
							public void execute() {
								victim.hit(thirdFinal);
								victim.hit(fourthFinal);
								this.stop();
							}
						});
						victim.addEnemyHit(player, hit + second + third + fourth);
						IdentifierManager.get("combat_after_effect").identify(player, victim, null, FightType.MELEE);
						if(victim.isPlayer()) {
							if(!victim.getPlayer().getHeadIcons().isSkulled() || victim.getPlayer().getHeadIcons().isSkulled() && !victim.getPlayer().getHeadIcons().getSkulledOn().equalsIgnoreCase(player.getUsername())) {
								player.getHeadIcons().setSkulled(true);
								player.getHeadIcons().setSkulledOn(victim.getPlayer().getUsername());
							}
						}
						this.stop();
					}
				});
				break;
			case 1215:
			case 1231:
			case 5680:
			case 5698:
			case 13465:
			case 13466:
			case 13467:
			case 13468:
			case 13976:
				player.animate(1062);
				player.graphics(252, (100 << 16));
				World.getWorld().submit(new Tickable(1) {
					@Override
					public void execute() {
						int damage = damage(player, FightType.MELEE);
						int attackBonus = getAttackBonus(player);
						int defenceBonus = getDefenceBonus(player, victim);
						int daBonus = attackBonus;
						if(Misc.random(daBonus) * 1.11 < Misc.random(defenceBonus)) {
							damage = 0;
						}
						double theHit = (int) (Misc.random(damage < 1 ? 1 : damage) * 1.25);
						victim.hit((int) theHit);
						victim.addEnemyHit(player, (int) theHit);
						this.stop();
					}
				});
				break;
			case 19784:
				player.animate(14788);
				victim.graphics(2795);
				break;
			case 10887:
				player.animate(5870);
				player.graphics(1027);
				break;
			}
			player.deductSpecial(getSpecialDeduction(wep));
			return true;
		}
		return false;
	}

	public int getAttackBonus(Mob mob) {
		CombatState state = mob.getCombatState();
		if (mob.isPlayer()) {
			Player player = mob.getPlayer();
			double bonus = player.getSkills().getLevel(Skills.Attack);
			/*if(player.getPrayer().getTurmoil() != null && player.getPrayer().usingPrayer(1, Prayer.TURMOIL)) {
				bonus *= 0.15;
				double boost = 0;
				if(player.getPrayer().getTurmoil().getAttackBoost() > 0) {
					boost = player.getPrayer().getTurmoil().getAttackBoost();
				}
				bonus *= 0.15 + boost;
				if(state != null) {
					if(state.getVictim() != null) {
						if(state.getVictim().isPlayer()) {
							bonus += state.getVictim().getPlayer().getSkills().getLevel(Skills.ATTACK) * (0.15 + boost);
						}
					}
				}
			}*/
			if(player.getPrayer().usingPrayer(0, Prayer.CLARITY_OF_THOUGHT)) {
				bonus *= 1.05;
			} else if(player.getPrayer().usingPrayer(0, Prayer.IMPROVED_REFLEXES)) {
				bonus *= 1.10;				
			} else if(player.getPrayer().usingPrayer(0, Prayer.INCREDIBLE_REFLEXES)) {
				bonus *= 1.15;				
			} else if(player.getPrayer().usingPrayer(0, Prayer.CHIVALRY)) {
				bonus *= 1.15;				
			} else if(player.getPrayer().usingPrayer(0, Prayer.PIETY)) {
				bonus *= 1.20;				
			} else if(player.getPrayer().getTurmoil() != null && player.getPrayer().usingPrayer(1, Prayer.TURMOIL)) {
				bonus *= 1.15;
			}
			CombatStyle style = player.getSettings().getCombatStyle();
			CombatType type = player.getSettings().getCombatType();
			int styleBonus = 1;
			double accuracy = 1;
			if(style == CombatStyle.STAB) {
				styleBonus = player.getBonuses().getBonus(Bonuses.STAB_ATTACK);
			} else if(style == CombatStyle.SLASH) {
				styleBonus = player.getBonuses().getBonus(Bonuses.SLASH_ATTACK);
			} else if(style == CombatStyle.CRUSH) {
				styleBonus = player.getBonuses().getBonus(Bonuses.CRUSH_ATTACK);
			} else {
				styleBonus = player.getBonuses().getBonus(Bonuses.CRUSH_ATTACK);
			}
			if(type == CombatType.ACCURATE) {
				accuracy = 1.15;
			} else if(type == CombatType.CONTROLLED) {
				accuracy = 1.10;
			}
			bonus += styleBonus * accuracy;
			if(player.getEquipment().voidSet(1)) {
				bonus *= 1.20;
			}
			/*if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
				if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_ATTACK)) {
					bonus = bonus - (bonus / 10);
				}
			}*/
			if(player.getPrayer().usingPrayer(1, Prayer.LEECH_ATTACK)) {
				bonus = bonus + (bonus * 0.05);
			}
			if(bonus < 1) {
				bonus = 1;
			}
			bonus *= 2.00;
			return (int) bonus;
		} else if(mob.isNPC()) {
			return mob.getNpc().getDefinition().getAttackBonus();
		}
		return 1;
	}
}
