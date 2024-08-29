package org.dementhium.model.combat;

import java.util.Random;

import org.dementhium.content.skills.Prayer;
import org.dementhium.model.Entity;
import org.dementhium.model.Mob;
import org.dementhium.model.Item;
import org.dementhium.model.combat.Combat.CombatStyle;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.impl.MagicCombat;
import org.dementhium.model.combat.impl.MeleeCombat;
import org.dementhium.model.combat.impl.RangedCombat;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.util.Constants;

/**
 * 
 * @author 'Mystic Flow
 * 
 */
public abstract class CombatAction {

	public static CombatAction forType(Mob mob, FightType type) {
		if(mob.isNPC()) {
			if(mob.getNpc().getCustomCombatAction() != null) {
				return mob.getNpc().getCustomCombatAction();
			}
		}
		switch(type) {
		case MELEE: return MeleeCombat.getAction();
		case RANGE: return RangedCombat.getAction();
		case MAGIC: return MagicCombat.getAction();
		}
		return null;
	}


	protected final Random r = new Random();


	public void appendExperience(Player player, int damage) {

	}

	public int damage(Mob mob, FightType type) {
		CombatState state = mob.getCombatState();
		if(state.getVictim() == null) {
			return 0;
		}
		Entity victim = state.getVictim();
		if(mob.isPlayer()) {
			Player player = mob.getPlayer();
			double prayerMultiplier = 1;
			int level = player.getSkills().getLevel(type.getSkill());
			if(type == FightType.MELEE) {
				if (player.getPrayer().usingPrayer(0, Prayer.BURST_OF_STRENGTH)) {
					prayerMultiplier = 1.05;
				} else if (player.getPrayer().usingPrayer(0, Prayer.SUPERHUMAN_STRENGTH)) {
					prayerMultiplier = 1.1;
				} else if (player.getPrayer().usingPrayer(0, Prayer.ULTIMATE_STRENGTH)) {
					prayerMultiplier = 1.15;
				} else if (player.getPrayer().usingPrayer(0, Prayer.CHIVALRY)) {
					prayerMultiplier = 1.18;
				} else if (player.getPrayer().usingPrayer(0, Prayer.PIETY)) {
					prayerMultiplier = 1.23;
				}
			} else if(type == FightType.RANGE) {
				if (player.getPrayer().usingPrayer(0, Prayer.SHARP_EYE)) {
					prayerMultiplier = 1.05;
				} else if (player.getPrayer().usingPrayer(0, Prayer.HAWK_EYE)) {
					prayerMultiplier = 1.10;
				} else if (player.getPrayer().usingPrayer(0, Prayer.EAGLE_EYE)) {
					prayerMultiplier = 1.15;
				}
			} else if (type == FightType.MAGIC) {
				if (player.getPrayer().usingPrayer(0, Prayer.MYSTIC_WILL)) {
					prayerMultiplier = 1.05;
				} else if (player.getPrayer().usingPrayer(0, Prayer.MYSTIC_LORE)) {
					prayerMultiplier = 1.10;
				} else if (player.getPrayer().usingPrayer(0, Prayer.MYSTIC_MIGHT)) {
					prayerMultiplier = 1.15;
				}
			}
			int bonus = 1;
			if(type == FightType.MELEE) {
				bonus += player.getBonuses().getBonus(Bonuses.STRENGTH);
			} else if(type == FightType.RANGE) {
				bonus += player.getBonuses().getBonus(Bonuses.RANGED);
			} else if (type == FightType.MAGIC) {
				bonus += player.getBonuses().getBonus(Bonuses.MAGIC);
			}

			double[] turmoilBonus = null;

			if(player.getPrayer().usingPrayer(1, Prayer.TURMOIL) && player.getPrayer().getTurmoil() != null && type == FightType.MELEE) {
				turmoilBonus = new double[2];

				turmoilBonus[0] = level * (0.23 + player.getPrayer().getTurmoil().getStrengthBoost());

				if(victim.isPlayer()) {
					turmoilBonus[1] = victim.getPlayer().getSkills().getLevel(Skills.STRENGTH) * (0.10 + player.getPrayer().getTurmoil().getStrengthBoost());
				}
			}

			double averageStrength = level * prayerMultiplier;

			if(player.getSettings().getCombatType() == Combat.CombatType.AGGRESSIVE) {
				averageStrength += 3;
			} else {
				averageStrength += 1;
			}
			if(turmoilBonus != null) {
				averageStrength += (turmoilBonus[0] + turmoilBonus[1]);
			}
			if(player.getEquipment().voidSet(1) && type == FightType.MELEE) {
				averageStrength *= 1.20;
			} else if(player.getEquipment().voidSet(2) && type == FightType.RANGE) {
				averageStrength *= 1.20;
			} else if(player.getEquipment().voidSet(3) && type == FightType.MAGIC) {
				averageStrength *= 1.20;
			}
			if(type == FightType.RANGE) {
				if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
					if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_RANGE)) {
						averageStrength -= averageStrength / 10;
					}
				}
				if(player.getPrayer().usingPrayer(1, Prayer.LEECH_RANGE)) {
					averageStrength += averageStrength * 0.05;
				}
			}
			if(type == FightType.MAGIC) {
				if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
					if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_MAGIC)) {
						averageStrength -= averageStrength / 10;
					}
				}
				if(player.getPrayer().usingPrayer(1, Prayer.LEECH_MAGIC)) {
					averageStrength += averageStrength * 0.05;
				}
			}
			if(type == FightType.MELEE) {
				if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
					if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_STRENGTH)) {
						averageStrength -= averageStrength / 10;
					}
				}
				if(player.getPrayer().usingPrayer(1, Prayer.LEECH_STRENGTH)) {
					averageStrength += averageStrength * 0.05;
				}
			}
			int maxHit = (int) (13 + ((averageStrength + 8) * (bonus + 64) / 64) * 0.93); //note from mystic: I'm not the best at math so I'll try to fix this as we go along lol
			if(player.getEquipment().barrowsSet(2)) {
				maxHit += ((player.getSkills().getLevelForXp(Skills.HITPOINTS) * 10) - player.getSkills().getHitPoints()) * 0.30;
			}
			if(player.hitCap) {
				return 9001;
			}
			if(victim.isPlayer()) {
				if(victim.getPlayer().hitCap) {
					return 0;
				}
			}
			return maxHit;
		}
		return mob.getNpc().getDefinition().getMaximumMeleeHit();
	}

	public int getDefenceBonus(Mob attacker, Mob victim) {
		if (victim.isPlayer()) {
			Player player = victim.getPlayer();
			int bonus = player.getSkills().getLevel(Skills.DEFENCE);
			if(player.getPrayer().usingPrayer(1, Prayer.TURMOIL) && player.getPrayer().getTurmoil() != null) {
				bonus += (0.15 + player.getPrayer().getTurmoil().getDefenceBoost());
				if(attacker.isPlayer()) {
					bonus += attacker.getPlayer().getSkills().getLevel(Skills.DEFENCE) * (0.15 + player.getPrayer().getTurmoil().getDefenceBoost());
				}
			}
			if(attacker.isPlayer()) {
				Player pl = attacker.getPlayer();
				CombatStyle style = pl.getSettings().getCombatStyle();
				int styleBonus = 1;
				if(this instanceof MeleeCombat) {
					if(style == CombatStyle.STAB) {
						styleBonus = player.getBonuses().getBonus(Bonuses.STAB_DEFENCE);
					} else if(style == CombatStyle.SLASH) {
						styleBonus = player.getBonuses().getBonus(Bonuses.SLASH_DEFENCE);
					} else if(style == CombatStyle.CRUSH) {
						styleBonus = player.getBonuses().getBonus(Bonuses.CRUSH_DEFENCE);
					} else {
						styleBonus = player.getBonuses().getBonus(Bonuses.CRUSH_DEFENCE);
					}
				} else if(this instanceof RangedCombat) {
					styleBonus = player.getBonuses().getBonus(Bonuses.RANGED_DEFENCE);
				}
				bonus += styleBonus;
				if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
					if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_DEFENCE)) {
						bonus -= bonus / 10;
					}
				}
				if(player.getPrayer().usingPrayer(1, Prayer.LEECH_DEFENCE)) {
					bonus += bonus * 0.05;
				}
				if(bonus < 1) {
					bonus = 1;
				}
			}
			return bonus;
		}
		if(victim.getNpc().getDefinition().getDefenceBonus() < 1) {
			return 1;
		}
		return victim.getNpc().getDefinition().getDefenceBonus();
	}

	protected int getSpecialDeduction(Item item) {
		if(item == null || item.getId() == -1) {
			return 0;
		}
		switch(item.getId()) {
		case 11694: //godswords
		case 11698:
		case 14484:
		case 13905:
		case 4153:
		case 4151:
			return Constants.SPEC_BAR_HALF;
		case 11700:
			return 600;
		case 11696:
			return Constants.SPEC_BAR_FULL;
		case 1215:
		case 13899:
		case 1231:
		case 5680:
		case 5698:
		case 13465:
		case 13466:
		case 13467:
		case 13468:
		case 13976:
			return 250;
		case 13902:
			return 350;
		case 861:
		case 4587:
			return 550;
		case 11235:
			return 650;
		case 19784://korasi
			return 750;
		case 10887://anchor
			return 500;
		}
		return 0;
	}

	public int nextInt(float f) {
		try {
			return r.nextInt((int) f);
		} catch(IllegalArgumentException e) {
			return 0;
		}
	}

	public int calculateHit(int damage, float average) {
		try {
			return (int) (nextInt(damage - average) + average);
		} catch(IllegalArgumentException e) {
			return 0;
		}
	}

	public abstract CombatHit hit(Mob mob, Mob victim);

	public abstract boolean canAttack(Mob mob, Mob victim);
}
