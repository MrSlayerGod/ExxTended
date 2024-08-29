package org.dementhium.content.skills;

import org.dementhium.event.Event;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

public class Prayer {

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1,
	CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
	ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7,
	RAPID_RESTORE = 8, RAPID_HEAL = 9, PROTECT_ITEM = 10,
	HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13,
	ULTIMATE_STRENGTH = 14, INCREDIBLE_REFLEXES = 15,
	PROTECT_FROM_MAGIC = 16, PROTECT_FROM_MISSILES = 17,
	PROTECT_FROM_MELEE = 18, EAGLE_EYE = 19, MYSTIC_MIGHT = 20,
	RETRIBUTION = 21, REDEMPTION = 22, SMITE = 23, CHIVALRY = 24,
	PIETY = 25;

	public static final int CURSE_PROTECT_ITEM = 0, SAP_WARRIOR = 1,
	SAP_RANGER = 2, SAP_MAGE = 3, SAP_SPIRIT = 4, BERSERKER = 5,
	DEFLECT_SUMMONING = 6, DEFLECT_MAGIC = 7, DEFLECT_MISSILES = 8,
	DEFLECT_MELEE = 9, LEECH_ATTACK = 10, LEECH_RANGE = 11,
	LEECH_MAGIC = 12, LEECH_DEFENCE = 13, LEECH_STRENGTH = 14,
	LEECH_ENERGY = 15, LEECH_SPECIAL_ATTACK = 16, WRATH = 17,
	SOUL_SPLIT = 18, TURMOIL = 19;

	private final Player player;

	private boolean[][] onPrayers;
	private boolean usingQuickPrayer;
	private boolean[][] quickPrayers = { new boolean[30], new boolean[20] };
	private boolean ancientcurses;
	private boolean drainingprayer;
	private boolean quickPrayersOn;

	private Turmoil turmoil;

	public Prayer(Player player) {
		this.player = player;
		boolean[][] onPrayers = { new boolean[30], new boolean[20] };
		this.onPrayers = onPrayers;
	}

	public boolean usingCorrispondingPrayer(FightType style) {
		int book = ancientcurses ? 1 : 0;
		switch (book) {
		case 0:
			switch(style) {
			case MELEE:
				return onPrayers[0][PROTECT_FROM_MELEE];
			case RANGE:
				return onPrayers[0][PROTECT_FROM_MISSILES];
			case MAGIC:
				return onPrayers[0][PROTECT_FROM_MAGIC];
			}
		case 1:
			switch (style) {
			case MELEE:
				return onPrayers[1][DEFLECT_MELEE];
			case RANGE:
				return onPrayers[1][DEFLECT_MISSILES];
			case MAGIC:
				return onPrayers[1][DEFLECT_MAGIC];
			}
		default:
			return false;
		}
	}

	public boolean usingPrayer(int book, int prayerId) {
		return this.onPrayers[book][prayerId];
	}

	public boolean needsProtectAgainstDamage() {
		return this.onPrayers[0][PROTECT_FROM_MELEE]
		                         || this.onPrayers[0][PROTECT_FROM_MISSILES]
		                                              || this.onPrayers[0][PROTECT_FROM_MAGIC];
	}

	private final static int[][] prayerLvls = {
		// normal prayer book
		{ 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 35,
			37, 40, 43, 44, 45, 46, 49, 52, 60, 65, 70, 74, 77 },
			// ancient prayer book
			{ 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84,
				86, 89, 92, 95 } };

	private final static int[][][] closePrayers = { { // normal prayer book
		{ 0, 5, 13 }, // Skin prayers 0
		{ 1, 6, 14 }, // Strength prayers 1
		{ 2, 7, 15 }, // Attack prayers 2
		{ 3, 11, 20, 28 }, // Range prayers 3
		{ 4, 12, 21, 29 }, // Magic prayers 4
		{ 8, 9, 26 }, // Restore prayers 5
		{ 10 }, // Protect item prayers 6
		{ 17, 18, 19 }, // Protect prayers 7
		{ 16 }, // Other protect prayers 8
		{ 22, 23, 24 }, // Other special prayers 9
		{ 25, 27 }, // Other prayers 10
	}, { // ancient prayer book
		{ 0 }, // Protect item prayers 0
		{ 1, 2, 3, 4 }, // sap prayers 1
		{ 5 }, // other prayers 2
		{ 7, 8, 9, 17, 18 }, // protect prayers 3
		{ 6 }, // other protect prayers 4
		{ 10, 11, 12, 13, 14, 15, 16 }, // leech prayers 5
		{ 19 }, // other prayers
	} };

	public void startDrain() {
		if (drainingprayer)
			return;
		this.drainingprayer = true;
		final int drainrate = this.getDrainRate();
		World.getWorld().submit(new Event(drainrate) {
			@Override
			public void run() {
				if (player == null || !player.isOnline()) {
					stop();
					return;
				}
				if (!checkPrayer()) {
					closeAllPrayers();
					drainingprayer = false;
					stop();
					return;
				}
				if (player.isDead()) {
					drainingprayer = false;
					stop();
					return;
				}
				int newrate = getDrainRate();
				if (newrate == -1) {
					drainingprayer = false;
					stop();
					return;
				}
				if (newrate != drainrate) {
					drainingprayer = false;
					stop();
					if (hasPrayersOn())
						player.getSkills().drainPray(1);
					if (!checkPrayer()) {
						closeAllPrayers();
						drainingprayer = false;
						return;
					}
					startDrain();
					return;
				}
				if (!checkPrayer()) {
					closeAllPrayers();
					drainingprayer = false;
					stop();
					return;
				}
				player.getSkills().drainPray(1);
			}

		});
	}

	public int getDrainRate() {
		int rate = 0;
		int index = 0;
		int numberofprays = 0;
		for (boolean prayer : this.onPrayers[this.getPrayerBook()]) {
			if (prayer) {
				rate += drainRate(index);
				numberofprays++;
			}
			index++;
		}
		int bonushere = 0;
		if (rate == 0)
			return -1;
		rate = (int) (rate * (0.0035 * bonushere + 1) * 1000) / numberofprays
		- ((numberofprays - 1) * 600);
		return rate;
	}

	private static int drainRate(int Prayer) {// I got this perfected
		switch (Prayer) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 22:
			return 12;
		case 5:
		case 6:
		case 7:
		case 11:
		case 12:
		case 23:
			return 6;
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
			return 3;
		case 8:
			return 26;
		case 9:
		case 10:
		case 26:
			return 18;
		case 24:
			return 2;
		case 25:
		case 27:
		case 28:
		default:
			return 1;
		}
	}

	public int getHeadIcon() {
		int value = -1;
		if (this.usingPrayer(0, 16))
			value += 8;
		if (this.usingPrayer(0, 17))
			value += 3;
		else if (this.usingPrayer(0, 18))
			value += 2;
		else if (this.usingPrayer(0, 19))
			value += 1;
		else if (this.usingPrayer(0, 22))
			value += 4;
		else if (this.usingPrayer(0, 23))
			value += 6;
		else if (this.usingPrayer(0, 24))
			value += 5;
		else if (this.usingPrayer(1, 6)) {
			value += 16;
			if (this.usingPrayer(1, 8))
				value += 2;
			else if (this.usingPrayer(1, 7))
				value += 3;
			else if (this.usingPrayer(1, 9))
				value += 1;
		} else if (this.usingPrayer(1, 7))
			value += 14;
		else if (this.usingPrayer(1, 8))
			value += 15;
		else if (this.usingPrayer(1, 9))
			value += 13;
		else if (this.usingPrayer(1, 17))
			value += 20;
		else if (this.usingPrayer(1, 18))
			value += 21;
		return value;
	}

	public void switchSettingQuickPrayer() {
		if (!this.usingQuickPrayer) {
			ActionSender.sendBConfig(player, 181, 1);
			ActionSender.sendBConfig(player, 168, 6);
			ActionSender.sendAMask(player, 0, 20, 271, 42, 0, 2);
			this.usingQuickPrayer = true;
		} else {
			ActionSender.sendBConfig(player, 181, 0);
			ActionSender.sendBConfig(player, 149, 6);
			this.usingQuickPrayer = false;
		}
	}

	public void switchQuickPrayers() {
		if (!checkPrayer())
			return;
		if (this.quickPrayersOn) {
			this.closeAllPrayers();
			ActionSender.sendBConfig(player, 182, 0);
			this.quickPrayersOn = false;
		} else {
			ActionSender.sendBConfig(player, 182, 1);
			int index = 0;
			for (boolean prayer : this.quickPrayers[this.getPrayerBook()]) {
				if (prayer)
					this.usePrayer(index);
				index++;
			}
			this.recalculatePrayer();
			this.quickPrayersOn = true;
		}
	}

	public void switchPrayer(int prayerId, boolean b) {
		if (!usingQuickPrayer) {
			if (!checkPrayer()) {
				return;
			}
		}
		this.usePrayer(prayerId);
		this.recalculatePrayer();
	}

	public void closeAllPrayers() {
		boolean[][] onPrayers = { new boolean[30], new boolean[20] };
		this.onPrayers = onPrayers;
		ActionSender.sendBConfig(player, 182, 0);
		ActionSender.sendConfig(player, ancientcurses ? 1582 : 1395, 0);
		recalculatePrayer();
		player.getMask().setApperanceUpdate(true);
	}

	private boolean hasPrayersOn() {
		for (boolean prayer : this.onPrayers[this.getPrayerBook()])
			if (prayer == true)
				return true;
		return false;
	}

	private boolean checkPrayer() {
		if (this.player.getSkills().getLevel(5) == 0) {
			ActionSender.sendChatMessage(player, (byte) 0, "You have ran out of prayer points.");
			return false;
		}
		return true;
	}

	private boolean usePrayer(int prayerId) {
		if (prayerId < 0 || prayerId >= prayerLvls[this.getPrayerBook()].length)
			return false;
		if (player.getSkills().getLevelForXp(5) < prayerLvls[this.getPrayerBook()][prayerId]) {
			player.sendMessage("You need a level of "+ prayerLvls[this.getPrayerBook()][prayerId]  + " prayer to activate this.");
			return false;
		}
		if (!usingQuickPrayer) {
			if (onPrayers[this.getPrayerBook()][prayerId]) {
				if(prayerId == Prayer.TURMOIL) {
					if(turmoil != null) {
						turmoil = null;
					}
				}
				onPrayers[this.getPrayerBook()][prayerId] = false;
				player.getMask().setApperanceUpdate(true);
				return true;
			}
		} else {
			if (quickPrayers[this.getPrayerBook()][prayerId]) {
				quickPrayers[this.getPrayerBook()][prayerId] = false;
				return true;
			}
		}
		if (getPrayerBook() == 0) {
			switch (prayerId) {
			case 0:
			case 5:
			case 13:
				this.closePrayers(closePrayers[this.getPrayerBook()][0],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 1:
			case 6:
			case 14:
				this.closePrayers(closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 2:
			case 7:
			case 15:
				this.closePrayers(closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 3:
			case 11:
			case 20:
			case 28:
				this.closePrayers(closePrayers[this.getPrayerBook()][0],
						closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 4:
			case 12:
			case 21:
			case 29:
				this.closePrayers(closePrayers[this.getPrayerBook()][0],
						closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 8:
			case 9:
			case 26:
				this.closePrayers(closePrayers[this.getPrayerBook()][5]);
				break;
			case 10:
				this.closePrayers(closePrayers[this.getPrayerBook()][6]);
				break;
			case 17:
			case 18:
			case 19:
				this.closePrayers(closePrayers[this.getPrayerBook()][7],
						closePrayers[this.getPrayerBook()][9]);
				this.player.getMask().setApperanceUpdate(true);
				break;
			case 16:
				this.closePrayers(closePrayers[this.getPrayerBook()][8],
						closePrayers[this.getPrayerBook()][9]);
				this.player.getMask().setApperanceUpdate(true);
				break;
			case 22:
			case 23:
			case 24:
				this.closePrayers(closePrayers[this.getPrayerBook()][7],
						closePrayers[this.getPrayerBook()][8],
						closePrayers[this.getPrayerBook()][9]);
				this.player.getMask().setApperanceUpdate(true);
				break;
			case 25:
			case 27:
				this.closePrayers(closePrayers[this.getPrayerBook()][0],
						closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			default:
				return false;
			}
		} else {
			switch (prayerId) {
			case 0:
				player.animate(12567);
				player.graphics(2213);
				this.closePrayers(closePrayers[this.getPrayerBook()][0]);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				this.closePrayers(closePrayers[this.getPrayerBook()][5], closePrayers[this.getPrayerBook()][6]);
				break;
			case 5:
				player.animate(12589);
				player.graphics(2266);
				closePrayers(closePrayers[getPrayerBook()][2]);
				break;
			case 7:
			case 8:
			case 9:
			case 17:
			case 18:
				closePrayers(closePrayers[getPrayerBook()][3]);
				player.getMask().setApperanceUpdate(true);
				break;
			case 6:
				closePrayers(closePrayers[getPrayerBook()][4]);
				player.getMask().setApperanceUpdate(true);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
				closePrayers(closePrayers[this.getPrayerBook()][1], closePrayers[this.getPrayerBook()][6]);
				break;
			case 19: //turmoil
				player.animate(12565);
				player.graphics(2226);
				closePrayers(closePrayers[this.getPrayerBook()][5], closePrayers[this.getPrayerBook()][6]);
				player.getMask().setApperanceUpdate(true);
				turmoil = new Turmoil();
				break;
			default:
				return false;
			}
		}
		if (!usingQuickPrayer) {
			onPrayers[getPrayerBook()][prayerId] = true;
			startDrain();
		} else {
			quickPrayers[getPrayerBook()][prayerId] = true;
		}
		return true;
	}

	private void closePrayers(int[]... prayers) {
		for (int[] prayer : prayers)
			for (int prayerId : prayer)
				if (usingQuickPrayer)
					this.quickPrayers[this.getPrayerBook()][prayerId] = false;
				else
					this.onPrayers[this.getPrayerBook()][prayerId] = false;
	}

	private final static int[] prayerSlotValues = { 1, 2, 4, 262144, 524288, 8,
		16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216,
		4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072,
		33554432, 134217728, 67108864, 536870912, 268435456 };

	private void recalculatePrayer() {
		int value = 0;
		int index = 0;
		for (boolean prayer : (!usingQuickPrayer ? onPrayers[getPrayerBook()] : quickPrayers[getPrayerBook()])) {
			if (prayer)
				value += ancientcurses ? Math.pow(2, index) : prayerSlotValues[index];
				index++;
		}
		ActionSender.sendConfig(player, ancientcurses ? (usingQuickPrayer ? 1587 : 1582) : (usingQuickPrayer ? 1397 : 1395), value);
	}

	private int getPrayerBook() {
		return ancientcurses == false ? 0 : 1;
	}

	public boolean isAncientCurses() {
		return this.ancientcurses;
	}

	public boolean setAnctientCurses(boolean bool) {
		closeAllPrayers();
		return this.ancientcurses = bool;
	}

	public boolean setAncientBook(boolean bool) {
		return this.ancientcurses = bool;
	}

	public void switchPrayBook(boolean book) {
		this.ancientcurses = book;
		ActionSender.sendConfig(player, 1584, book ? 1 : 0);
		ActionSender.sendAMask(player, 0, 27, 271, 6, 0, 2);
		this.drainingprayer = false;
		closeAllPrayers();
	}

	public void setQuickPrayers() {
		ActionSender.sendBConfig(player, 181, 0);
		ActionSender.sendBConfig(player, 149, 6);
		this.player.getPrayer().usingQuickPrayer = false;
	}


	public void tick() {
		if(turmoil != null) {
			if(player.getCombatState().getVictim() != null) {
				turmoil.delay++;
			}
			if(turmoil.delay >= 20) {
				turmoil.attackBoost += 0.01;
				turmoil.defenceBoost += 0.01;
				turmoil.strengthBoost += 0.01;
				if(turmoil.strengthBoost >= 0.10) {
					turmoil.strengthBoost = 0.10;
				} 
				if(turmoil.attackBoost >= 0.15) {
					turmoil.attackBoost = 0.15;
				}
				if(turmoil.defenceBoost >= 0.15) {
					turmoil.defenceBoost = 0.15;
				}
				turmoil.delay = 0;
				turmoil.updateStrings();
			}
		}
	}

	public Turmoil getTurmoil() {
		return turmoil;
	}

	public class Turmoil {

		private double attackBoost = 0.0, defenceBoost = 0.0, strengthBoost = 0.0;

		private int delay;

		public double getAttackBoost() {
			return attackBoost;
		}

		public double getDefenceBoost() {
			return defenceBoost;
		}

		public double getStrengthBoost() {
			return strengthBoost;
		}

		public void updateStrings() {
			ActionSender.sendString(player, 271, 34, Misc.percentage(0.23 + attackBoost) + "%");
			ActionSender.sendString(player, 271, 33, Misc.percentage(0.15 + strengthBoost) + "%");
			ActionSender.sendString(player, 271, 32, Misc.percentage(0.15 + defenceBoost) + "%");
		}
	}
}
