package org.dementhium.model.combat.impl;

import org.dementhium.content.misc.Following;
import org.dementhium.content.skills.Prayer;
import org.dementhium.event.Tickable;
import org.dementhium.model.Mob;
import org.dementhium.model.Item;
import org.dementhium.model.ProjectileManager;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.model.GroundItemManager;
import org.dementhium.util.Misc;

/**
 * 
 * @author `Discardedx2<the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class RangedCombat extends CombatAction {

	private static final CombatAction INSTANCE = new RangedCombat();

	public static CombatAction getAction() {
		return INSTANCE;
	}

	private RangedCombat() {
	}

	@Override
	public CombatHit hit(final Mob mob, final Mob victim) {
		mob.getPlayer().removeAttribute("spellQueued");
		if(checkArrows(mob.getPlayer())) {
			if(mob.getWalkingQueue().isMoving()) {
				mob.getWalkingQueue().reset();
			}
			mob.getCombatState().setSpellDelay(6);
			mob.getCombatState().setAttackDelay(mob.getPlayer().getAttackDelay());
			int hitDamage = damage(mob, FightType.RANGE);
			int rangedBonus = getRangedBonus(mob);
			int defenceBonus = getDefenceBonus(mob, victim);
			Item wep = mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON);
			Item ammo = mob.getPlayer().getEquipment().get(Equipment.SLOT_ARROWS);
			int wepId = wep == null ? -1 : wep.getId();
			int ammoId = ammo == null ? -1 : ammo.getId();
			Player player = mob.getPlayer();
			boolean cbowSpec = false;
			if (Misc.random(10) == 1 && ammoId == 9244 && wepId == 9185) {
				cbowSpec = true;
				hitDamage = Misc.random(hitDamage) + Misc.random(300);
				victim.graphics(756);
			}
			if(!cbowSpec) {
				if (Misc.random(rangedBonus) < Misc.random(defenceBonus)) {
					hitDamage = 0;
				}
				if (hitDamage > 0) {
					hitDamage = Misc.random(hitDamage);
				}
			}
			cbowSpec = false;
			if (mob.usingSpecial()) {
				if(wep != null) {
					if (player.getSpecialAmount() >= getSpecialDeduction(wep)) {
						if(wep != null) {
							if(ammo != null) {
								if(wep.getId() == 861 || wep.getId() == 11235) {
									if(ammo.getAmount() >= 2) {
										performRangedSpecials(player, victim);
									} else {
										player.sendMessage("You don't have enough ammo to perform this special attack.");
										mob.resetCombat();
										return null;
									}
								}
							}
						}
					} else {
						ActionSender.sendChatMessage(player, 0, "You do not have enough special attack to perform this action.");
						player.reverseSpecialActive();
						mob.resetCombat();
						return null;
					}
				} else {
					mob.resetCombat();
					return null;
				}
			} else {
				if (sendProperProjectile(mob, victim)) {
					mob.animate(mob.getAttackAnimation());
					final boolean animating = victim.isAnimating();
					World.getWorld().submit(new Tickable(2) {
						public void execute() {
							if(!animating && !victim.isAnimating()) {
								victim.animate(victim.getDefenceAnimation());
							}
							this.stop();
						}
					});
					if(wepId == 11235) {
						if(ammo.getAmount() >= 2) {
							performDbow(player, victim);
							return null;
						} else {
							player.sendMessage("You need atleast two arrows to fire this bow.");
							mob.resetCombat();
							return null;
						}
					}
				} else {
					mob.resetCombat();
					return null;
				}
			}
			return new CombatHit(mob, victim, hitDamage, 3, "drop_arrows", new Object[] {mob, victim});
		} else {
			mob.resetCombat();
		}
		return null;
	}

	@Override
	public boolean canAttack(Mob mob, Mob victim) {
		if (!mob.getLocation().withinDistance(victim.getLocation(), 8)) {
			if(mob.getFightType() == FightType.RANGE || mob.getFightType() == FightType.MAGIC) {
				if(mob.isPlayer()) {
					Following.combatFollow(mob, victim);
					return false;
				} else {
					Following.npcFollow(mob.getNpc(), victim);
				}
			}
		}
		return true;
	}

	private void performRangedSpecials(final Player player, final Mob victim) {
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
		final int id = wep != null ? wep.getId() : -1;
		final int arrow = player.getEquipment().get(Equipment.SLOT_ARROWS) == null ? -1 : player.getEquipment().get(Equipment.SLOT_ARROWS).getId();
		switch(id) {
		case 861:
		case 11235:
			if(id == 861) {
				player.animate(1074);
				player.graphics(256, (100 << 16));
				ProjectileManager.sendGlobalProjectile(player, victim, 249, 160, 140, 33);
			} else if(id == 11235) {
				player.animate(426);
				player.graphics(1111, (100 << 16));
			}
			World.getWorld().submit(new Tickable(1) {
				public void execute() {
					if(id == 861) {
						player.graphics(256, (100 << 16));
						ProjectileManager.sendGlobalProjectile(player, victim, 249, 160, 140, 53);
					} else if(id == 11235) {
						ProjectileManager.sendGlobalProjectile(player, victim, 1099, 200, 160, 51);
						ProjectileManager.sendGlobalProjectile(player, victim, 1120, 200, 160, 51);
						ProjectileManager.sendGlobalProjectile(player, victim, 1099, 160, 140, 65);
						ProjectileManager.sendGlobalProjectile(player, victim, 1120, 160, 140, 65);
					}
					this.stop();
				}
			});
			World.getWorld().submit(new Tickable(3) {
				public void execute() {
					int damage = damage(player, FightType.RANGE);
					int attackBonus = getRangedBonus(player);
					int defenceBonus = getDefenceBonus(player, victim);
					if(Misc.random(attackBonus) * 1.11 < Misc.random(defenceBonus)) {
						damage = 0;
					}
					double theHit = (int) (Misc.random(damage < 1 ? 1 : damage) * 1.25);
					victim.hit((int) theHit);
					victim.addEnemyHit(player, (int) theHit);
					if(id == 861 || id == 11235) {
						player.reverseSpecialActive();
						player.getEquipment().deleteItem(arrow, 1);
						World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(player, new Item(arrow, 1), victim.getLocation()), true);
					}
					this.stop();
				}
			});
			break;
		}
		player.deductSpecial(getSpecialDeduction(wep));
	}

	public int getGFXDbow(int arrow) {
		switch(arrow) {
		case 882://bronze arrows
			return 1104;
		case 884://iron arrows
			return 1105;
		case 886://steel arrows
			return 1106;
		case 888://mithril arrows
			return 1107;
		case 890://adamant arrows
			return 1108;
		case 892://rune arrows
			return 1109;
		case 11212://dragon arrows
			return 1111;
		}
		return -1;
	}

	public int getProjectileDbow(int arrow) {
		switch(arrow) {
		case 882://bronze arrows
			return 10;
		case 884://iron arrows
			return 11;
		case 886://steel arrows
			return 11;
		case 888://mithril arrows
			return 12;
		case 890://adamant arrows
			return 13;
		case 892://rune arrows
			return 15;
		case 11212://dragon arrows
			return 1120;
		}
		return -1;
	}	

	private void performDbow(final Player player, final Mob victim) {
		final int arrow = player.getEquipment().get(Equipment.SLOT_ARROWS) == null ? -1 : player.getEquipment().get(Equipment.SLOT_ARROWS).getId();
		player.graphics(getGFXDbow(arrow), (100 << 16));
		World.getWorld().submit(new Tickable(1) {
			public void execute() {
				if(checkArrows(player)) {
					ProjectileManager.sendGlobalProjectile(player, victim, getProjectileDbow(arrow), 200, 160, 51);
					ProjectileManager.sendGlobalProjectile(player, victim, getProjectileDbow(arrow), 160, 140, 65);
				}
				this.stop();
			}
		});
		World.getWorld().submit(new Tickable(3) {
			public void execute() {
				if(checkArrows(player)) {
					int damage = damage(player, FightType.RANGE);
					int attackBonus = getRangedBonus(player);
					int defenceBonus = getDefenceBonus(player, victim);
					if(Misc.random(attackBonus) * 1.11 < Misc.random(defenceBonus)) {
					damage = 0;
					}
					double theHit = (int) (Misc.random(damage < 1 ? 1 : damage) * 1.25);
					double theHit2 = (int) (Misc.random(damage < 1 ? 1 : damage) * 1.25);
					victim.hit((int) theHit);
					victim.hit((int) theHit2);
					victim.addEnemyHit(player, (int) (theHit + theHit2));
					player.getEquipment().deleteItem(arrow, 2);
					World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(player, new Item(arrow, 1), victim.getLocation()), true);
					if(Misc.random(1) == 0) {
						World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(player, new Item(arrow, 1), victim.getLocation()), true);
					}
				}
				this.stop();
			}
		});
	}

	private int getRangedBonus(Mob mob) {
		if (mob.isPlayer()) {
			Player player = mob.getPlayer();
			int bonus = 0;
			bonus += player.getSkills().getLevel(Skills.RANGE);
			double prayerBonus = 1.0;
			if (player.getPrayer().usingPrayer(0, Prayer.SHARP_EYE)) {
				prayerBonus = 1.05;
			} else if (player.getPrayer().usingPrayer(0, Prayer.HAWK_EYE)) {
				prayerBonus = 1.10;
			} else if (player.getPrayer().usingPrayer(0, Prayer.EAGLE_EYE)) {
				prayerBonus = 1.15;
			}
			bonus += player.getBonuses().getBonus(Bonuses.RANGED_ATTACK);
			if(player.getEquipment().voidSet(2)) {
				bonus *= 1.20;
			}
			if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
				if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_RANGE)) {
					bonus -= bonus / 10;
				}
			}
			if(player.getPrayer().usingPrayer(1, Prayer.LEECH_RANGE)) {
				bonus += bonus * 0.05;
			}
			bonus = (int) (bonus * prayerBonus);
			if(bonus < 1) {
				bonus = 1;
			}
			bonus *= 2.00;
			return bonus;
		}
		return 1;
	}

	/*
	 * Returns the arrows allowed to be used by the bow
	 */
	public int[] arrowsAllowed(int bow) {
		switch(bow) {
		case 839:
		case 841:
			return new int[] {882, 884};
		case 843:
		case 845:
			return new int[] {882, 884, 886};
		case 847:
		case 849:
			return new int[] {882, 884, 886, 888};
		case 851:
		case 853:
			return new int[] {882, 884, 886, 888, 890};
		case 859:
		case 861:
			return new int[] {882, 884, 886, 888, 890, 892};
		case 9185:
			return new int[] {9243, 9244};
		case 18357:
			return new int[] {9243, 9244};
		case 11235:
			return new int[] {882, 884, 886, 888, 890, 892, 11212};
		case 4734:
			return new int[] {4740};
		case 15241:
			return new int[] {15243};
		}
		return new int[] {-1};
	}
	
	/*
	 * Checks to see if the player can use the arrows with the bow
	 */
	public boolean canUseArrows(Player p) {
		int wep = p.getEquipment().get(Equipment.SLOT_WEAPON) == null ? -1 : p.getEquipment().get(Equipment.SLOT_WEAPON).getId();
		int arrows = p.getEquipment().get(Equipment.SLOT_ARROWS) == null ? -1 : p.getEquipment().get(Equipment.SLOT_ARROWS).getId();
		int[] allowed = arrowsAllowed(wep);
		for(int i : allowed)
			if(i == arrows)
				return true;
		return false;
	}
	
	public boolean checkArrows(Player p) {
		int wep = p.getEquipment().get(Equipment.SLOT_WEAPON) == null ? -1 : p.getEquipment().get(Equipment.SLOT_WEAPON).getId();
		int arrows = p.getEquipment().get(Equipment.SLOT_ARROWS) == null ? -1 : p.getEquipment().get(Equipment.SLOT_ARROWS).getId();
		if(wep >= 4210 && wep <= 4225 || wep == 20171 || wep >= 13879 && wep <= 13882) {
			return true;
		}
		if(arrows == -1) {
			p.sendMessage("You have no ammo in your quiver.");
			return false;
		}
		if(!canUseArrows(p)) {
			p.sendMessage("You can't use this ammo with your weapon.");
			return false;
		}
		return true;
	}

	private boolean sendProperProjectile(final Mob mob, final Mob victim) {
		if (mob.isPlayer()) {
			final Player player = mob.getPlayer();
			Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
			Item ammo = player.getEquipment().get(Equipment.SLOT_ARROWS);
			final int wepId = wep != null ? wep.getId() : -1;
			int ammoId = ammo != null ? ammo.getId() : -1;
			int ammoAmount = ammo != null ? ammo.getAmount() : -1;
			switch(wepId) {
				case 4212:// crystal bow full
				case 4213:// crystal bow 9/10
				case 4214:// ect.. degrading too 1/0
				case 4215:
				case 4216:
				case 4217:
				case 4218:
				case 4219:
				case 4220:
				case 4221:
				case 4222:
				case 4223:
					mob.graphics(250, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 249, false);
					break;
				case 20171://zaryte bow
					mob.graphics(2962, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 2950, false);
					break;

				case 13879:
				case 13880:
				case 13881:
				case 13882:
					mob.graphics(1836);
					ProjectileManager.sendDelayedProjectile(mob, victim, 1837, false);
					World.getWorld().submit(new Tickable(3) {
						public void execute() {
							if(Misc.random(1) == 0) {
								World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(player, new Item(wepId, 1), victim.getLocation()), true);
							}
							mob.getPlayer().getEquipment().deleteItem(wepId, 1);
							this.stop();
						}
					});
					break;
			}
			if(wepId >= 4210 && wepId <= 4225 || wepId == 20171 || wepId >= 13879 && wepId <= 13882) {
				return true;
			}
			if(ammoAmount > 0) {
				switch(wepId) {
				case 841://shortbow
				case 843://oak shortbow
				case 849://willow shortbow
				case 853://maple shortbow
				case 857://yew shortbow
				case 861://magic shortbow

				case 839://longbow
				case 845://oak longbow
				case 847://willow longbow
				case 851://maple longbow
				case 855://yew longbow
				case 859://magic longbow

					switch(ammoId) {
					case 882://bronze arrows
						mob.graphics(19, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 10, false);
						break;
					case 884://iron arrows
						mob.graphics(20, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 12, false);
						break;
					case 886://steel arrows
						mob.graphics(20, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 12, false);
						break;
					case 888://mithril arrows
						mob.graphics(21, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 13, false);
						break;
					case 890://adamant arrows
						mob.graphics(22, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 14, false);
						break;
					case 892://rune arrows
						mob.graphics(24, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 15, false);
						break;
					case 9706://training arrows
						mob.graphics(25, (100 << 16));
						ProjectileManager.sendDelayedProjectile(mob, victim, 25, false);
						break;
					}
					break;

				case 9185:
				case 18357:
					ProjectileManager.sendDelayedProjectile(mob, victim, 27, false);
					break;

					/**
					 * Knives (TODO)
					 */
				/*case 863://iron knife
					mob.graphics(220, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 214, true);
					break;
				case 864://bronze knife
					mob.graphics(221, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 215, true);
					break;
				case 865://steel knife
					mob.graphics(222, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 216, true);
					break;
				case 866://mithril knife
					mob.graphics(223, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 217, true);
					break;
				case 867://adamant knife
					mob.graphics(224, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 218, true);
					break;
				case 868://rune knife
					mob.graphics(225, (100 << 16));
					ProjectileManager.sendDelayedProjectile(mob, victim, 219, true);
					break;  TODO      */ 
				}
			} else {
				ActionSender.sendMessage(player, "There's no ammo left in your quiver.");
			}
		}
		return true;
	}

}
