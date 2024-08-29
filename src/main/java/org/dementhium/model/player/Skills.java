package org.dementhium.model.player;

import org.dementhium.event.Tickable;
import org.dementhium.model.World;
import org.dementhium.net.ActionSender;

public class Skills {

	public static final int SKILL_COUNT = 25;
	public static final double MAXIMUM_EXP = 200000000;

	private final Player player;
	private final int level[] = new int[SKILL_COUNT];
	private final double xp[] = new double[SKILL_COUNT];
	private int HitPoints;
	private boolean sendDead = false;

	public static final String[] SKILL_NAME = { "Attack", "Defence",
		"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
		"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting", "Construction", "Hunter", "Summoning",
	"Dungeoneering" };

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
	HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
	WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
	CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
	AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
	RUNECRAFTING = 20, CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23,
	DUNGEONEERING = 24;

	public Skills(Player player) {
		this.player = player;
		for (int i = 0; i < SKILL_COUNT; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		HitPoints = 100;
	}

	public void hit(int hitDiff) {
		if(hitDiff > HitPoints)
			hitDiff = HitPoints;
		HitPoints -= hitDiff;
		if(HitPoints <= 0)
			sendDead();
		if(HitPoints > getMaxHitpoints()) {
			HitPoints = getMaxHitpoints();
		}
		ActionSender.sendConfig(player, 1240, HitPoints * 2);
	}

	public boolean isDead() {
		return HitPoints <= 0;
	}
	
	public int getMaxHitpoints() {
		return getLevelForXp(Skills.HITPOINTS) * 10;
	}

	public void sendDead() {
		if(!sendDead) {
			player.getWalkingQueue().reset();
			if(player.getCombatState().getLastAttacker() != null) {
				player.getCombatState().getLastAttacker().getCombatState().setLastAttacker(null);
				player.getCombatState().setLastAttacker(null);
			}
			sendDead = true;
			World.getWorld().submit(new Tickable(1) {
				public void execute() {
					player.animate(9055);
					World.getWorld().submit(new Tickable(4) {
						@Override
						public void execute() {
							for (int i = 0; i < SKILL_COUNT; i++)
								set(i, getLevelForXp(i));
							player.giveKiller();
							ActionSender.sendChatMessage(player, 0, "Oh dear, you are dead.");
							player.teleport(3162, 3484, 0);
							HitPoints = (getLevelForXp(3) * 10);
							ActionSender.sendConfig(player, 1240, HitPoints * 2);
							World.getWorld().submit(new Tickable(1) {
								public void execute() {
									player.setSpecialAmount(1000, true);
									player.getWalkingQueue().setRunEnergy(100);
									this.stop();
								}
							});
							player.getHeadIcons().setSkulled(false);
							player.getTeleblock().setTeleblocked(false);
							player.resetCombat();
							player.getCombatState().setFrozenTime(0);
							player.setAttribute("vengDelay", System.currentTimeMillis() - ((Long) player.getAttribute("vengDelay", 0L)) + 30000);
							player.setAttribute("vengeance", Boolean.FALSE);
							player.clearEnemyHits();
							sendDead = false;
							this.stop();
						}

					});
					this.stop();
				}
			});
		}
	}

	public void heal(int hitDiff) {
		if(isDead())
			return;
		boolean modify = false;
		if(HitPoints > getLevelForXp(3) * 10) {
			modify = true;
		}
		HitPoints += hitDiff;
		int max = getLevel(3) * 10;
		if(modify) {
			max = (getLevelForXp(3) * 10) + 100;
		}
		if (HitPoints > max) {
			HitPoints = max;
		}
		ActionSender.sendConfig(player, 1240, HitPoints * 2);
	}

	public void heal(int hitDiff, int max) {
		HitPoints += hitDiff;
		if (HitPoints > max) {
			HitPoints = max;
		}
		ActionSender.sendConfig(player, 1240, HitPoints * 2);
	}

	public void restorePray(int hitDiff) {
		level[5] += hitDiff;
		short max = (short) getLevelForXp(5);
		if (level[5] > max) {
			level[5] = max;
		}
		ActionSender.sendSkillLevel(player, 5);
	}

	public void drainPray(int drain) {
		level[5] -= drain;
		if (level[5] < 0) {
			level[5] = 0;
		}
		ActionSender.sendSkillLevel(player, 5);
	}

	public void drain(int skill, int drain) {
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		ActionSender.sendSkillLevel(player, skill);
	}
	
	public void Reset() {
		for (int i = 0; i < SKILL_COUNT; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		refresh();
	}

	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		int summoning = getLevelForXp(Skills.SUMMONING);
		summoning /= 8;
		return combatLevel + summoning;
	}

	public int getLevel(int skill) {
		return level[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl < (skill == 24 ? 121 : 100); lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return  skill == 24 ? 120 : 99;
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		ActionSender.sendSkillLevel(player, skill);
	}

	public void addXp(int skill, double exp) {
		int oldLevel = getLevelForXp(skill);
		int oldCombat = getCombatLevel();
		xp[skill] += exp;
		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			if (skill == 3) {
				heal(100*levelDiff);
			}
			if(oldCombat != getCombatLevel()) {
				player.getMask().setApperanceUpdate(true);
			}
		}
		ActionSender.sendSkillLevel(player, skill);
	}

	public void set(int skill, int val) {
		level[skill] = val;
		ActionSender.sendSkillLevel(player, skill);
	}

	public void setLevelAndXP(int skill, int level, int xp) {
		this.level[skill] = (short) level;
		this.xp[skill] = xp;
	}

	public void sendSkillLevels() {
		for (int i = 0; i < Skills.SKILL_COUNT; i++)
			ActionSender.sendSkillLevel(player, i);
	}

	public void refresh() {
		sendSkillLevels();
		ActionSender.sendConfig(player, 1240, HitPoints * 2);
		this.player.getMask().setApperanceUpdate(true);
	}

	public boolean isLevelBelowOriginal(int skill) {
		return level[skill] < getLevelForXp(skill);
	}

	public boolean isLevelBelowOriginalModification(int skill, int modification) {
		return level[skill] < (getLevelForXp(skill) + modification);
	}

	public void increaseLevelToMaximum(int skill, int modification) {
		if(isLevelBelowOriginal(skill)) {
			setLevel(skill, level[skill] + modification >= getLevelForXp(skill) ? getLevelForXp(skill) : level[skill] + modification);
		}
	}

	public void increaseLevelToMaximumModification(int skill, int modification) {
		if(isLevelBelowOriginalModification(skill, modification)) {
			setLevel(skill, level[skill] + modification >= (getLevelForXp(skill) + modification) ? (getLevelForXp(skill) + modification) : level[skill] + modification);
		}
	}

	public void decreaseLevelToMinimum(int skill, int modification) {
		if(level[skill] > 1) {
			setLevel(skill, level[skill] - modification <= 1 ? 1 : level[skill] - modification);
		}
	}

	public void decreaseLevelToZero(int skill, int modification) {
		if(level[skill] > 0) {
			setLevel(skill, level[skill] - modification <= 0 ? 0 : level[skill] - modification);
		}
	}

	public void decreaseLevelOnce(int skill, int amount) {
		if(level[skill] > (getLevelForXp(skill) - amount)) { //this stops resetting levels. EG if I have 87/95 str and it decreases 5, normally it would reset it to 90/95, however this line prevents it!
			if(level[skill] - amount <= (getLevelForXp(skill) - amount)) {
				level[skill] = (getLevelForXp(skill) - amount);
			} else {
				level[skill] -= amount;			
			}
			ActionSender.sendSkillLevel(player, skill);
		}
	}

	public void setLevel(int skill, int level) {
		this.level[skill] = level;
		ActionSender.sendSkillLevel(player, skill);
	}

	public void setHitPoints(int hitPoints) {
		HitPoints = hitPoints;
	}

	public int getHitPoints() {
		return HitPoints;
	}

}
