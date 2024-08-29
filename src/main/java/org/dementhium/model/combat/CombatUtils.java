package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.Item;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class CombatUtils {

	public static boolean isDeepEnoughInWild(Mob mob, Mob victim){
		if(mob.isPlayer() && victim.isPlayer()) {
			int mobRange = mob.getPlayer().getSkills().getCombatLevel() + mob.getLocation().getWildernessLevel();
			int mobRange2 = mobRange - (mob.getLocation().getWildernessLevel() * 2);//Multiply by two because we have to remove what we added before!
			int victimRange = victim.getPlayer().getSkills().getCombatLevel() + victim.getLocation().getWildernessLevel();
			int victimRange2 = victimRange - (victim.getLocation().getWildernessLevel() * 2);
			double levelRangeMob = (0.1 * mob.getPlayer().getSkills().getCombatLevel()) + 5 + mob.getLocation().getWildernessLevel();
			double levelRangeVictim = (0.1 * victim.getPlayer().getSkills().getCombatLevel()) + 5 + victim.getLocation().getWildernessLevel();
			int levelRange = Math.abs(victim.getPlayer().getSkills().getCombatLevel() - mob.getPlayer().getSkills().getCombatLevel());
			if(!mob.inWilderness() && !mob.getLocation().atGe() || !victim.inWilderness() && !victim.getLocation().atGe()){
				return false;
			}
			if(mob.getLocation().atGe() && victim.getLocation().atGe()) {
				if(levelRange > levelRangeMob) {
						ActionSender.sendMessage(mob.getPlayer(), "You need to move deeper into the Wilderness to attack this player");
						mob.requestWalk(victim.getLocation().getX() - 1, victim.getLocation().getY());
						return false;
				}
				if(levelRange > levelRangeVictim) {
						ActionSender.sendMessage(mob.getPlayer(), "That player is not deep enough in the Wilderness for you to attack.");
						mob.requestWalk(victim.getLocation().getX() - 1, victim.getLocation().getY());
						return false;
				}
			}
			if(victim.getPlayer().getSkills().getCombatLevel() > mobRange || victim.getPlayer().getSkills().getCombatLevel() < mobRange2){
				if(!mob.getLocation().atGe() && !victim.getLocation().atGe()) {
					ActionSender.sendMessage(mob.getPlayer(), "You need to move deeper into the Wilderness to attack this player");
					mob.requestWalk(victim.getLocation().getX() - 1, victim.getLocation().getY());
					return false;
				}
			}
			if(mob.getPlayer().getSkills().getCombatLevel() > victimRange || mob.getPlayer().getSkills().getCombatLevel() < victimRange2){
				if(!mob.getLocation().atGe() && !victim.getLocation().atGe()) {
					ActionSender.sendMessage(mob.getPlayer(), "That player is not deep enough in the Wilderness for you to attack.");
					mob.requestWalk(victim.getLocation().getX() - 1, victim.getLocation().getY());
					return false;
				}
			}
			if(mob.inWilderness() && victim.inWilderness() ||  mob.getLocation().atGe() && victim.getLocation().atGe()){
				return true;
			}
			return false;
		}
		return true;
	}


	public static double specialPower(int sword) {
		switch(sword) {
		case 19784: return 1.29876;
		case 11694: return 1.34375;
		case 11696: return 1.1825;
		case 11698:
		case 11700: return 1.175;
		case 3101:
		case 3204:
		case 1215:
		case 1231:
		case 5680:
		case 5698: return 1.1;
		case 1305: return 1.15;
		case 1434: return 1.45;
		case 11235: return 1.45;
		}
		return 1.1;
	}

	public static int getAttackAnimation(Mob mob) {
		if(mob.isPlayer()) {
			Player player = mob.getPlayer();
			Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
			if(weapon != null) {
				String name = weapon.getDefinition().getName();
				if (name.contains("knife"))
					return 929;
				if (name.contains("dart"))
					return 582;
				if (name.contains("maul")) {
					if(name.startsWith("Granite"))
						return 1665; //Granite Maul anim
					return 2661;
				}
				if (name.contains("scimitar") && !name.equals("Dragon scimitar")) {
					if(player.getSettings().getCombatType() == Combat.CombatType.CONTROLLED)
						return 12028;
					return 12029;
				}
				if (name.equals("Dragon scimitar")) {
					return 15071;
				}
				switch(weapon.getId()) {
				case -1:
					if(player.getSettings().getCombatType() == Combat.CombatType.AGGRESSIVE) {
						return 423; // kick
					} else {
						return 422; // punch
					}
				case 19784: //korasi'
					return 12029;
				case 4726:
					return 2080;
				case 4718:
					if (player.getSettings().getCombatStyle() == Combat.CombatStyle.CRUSH)
						return 12003;
					return 12002;
				case 14484:
					return 393;

				case 15241:
					return 12152;

				case 13879:
				case 13880:
				case 13881:
				case 13882:
					return 10501;

				case 6526:
				case 6908:
				case 6910:
				case 6912:
				case 6914:
				case 13867:
				case 13869:
				case 13941:
				case 13943:
				case 18355:
					return 419;

				case 4068:
				case 4503:
				case 4508:
				case 18705:
					if (player.getSettings().getCombatType() == Combat.CombatType.CONTROLLED)
						return 12310;
					return 12311;
				case 11696:
				case 11694:
				case 11698:
				case 11700:
				case 11730:
				case 1307:
				case 1309:
				case 1311:
				case 1313:
				case 1315:
				case 1317:
				case 1319:
					if (player.getSettings().getCombatType() == Combat.CombatType.DEFENSIVE)
						return 7049;
					else if (player.getSettings().getCombatStyle() == Combat.CombatStyle.CRUSH)
						return 7048;
					return 7041;

				case 18349:
					if (player.getSettings().getCombatType() == Combat.CombatType.CONTROLLED)
						return 13048;
					return 13049;

				case 18351:
				case 16403:
					if (player.getSettings().getCombatType() == Combat.CombatType.CONTROLLED)
						return 13049;
					return 13048;

				case 14679:
					return 401;

				case 13899:
				case 13901:
				case 13923:
				case 13925:

					if (player.getSettings().getCombatType() == Combat.CombatType.CONTROLLED)
						return 13049;
					return 13048;

				case 13902:
				case 13904:
				case 13926:
				case 13928:
				case 17039:
					return 401;

				case 15486:

					if (player.getSettings().getCombatType() == Combat.CombatType.AGGRESSIVE)
						return 12029;
					else if (player.getSettings().getCombatType() == Combat.CombatType.DEFENSIVE)
						return 414;
					return 12028;

				case 11716:
					if (player.getSettings().getCombatStyle() == Combat.CombatStyle.STAB)
						return 12006;
					else if (player.getSettings().getCombatStyle() == Combat.CombatStyle.SLASH)
						return 12005;
					else if (player.getSettings().getCombatStyle() == Combat.CombatStyle.CRUSH)
						return 12009;
					return 12006;


				case 9174:
				case 9175:
				case 9176:
				case 9177:
				case 9178:
				case 9179:
				case 9180:
				case 9181:
				case 9182:
				case 9183:
				case 9184:
				case 9185:
				case 9186:
					return 4230;
				case 1265:
				case 1266:
				case 1267:
				case 1268:
				case 1269:
				case 1270:
				case 1271:
				case 1272:
				case 1273:
				case 1274:
				case 1275:
				case 1276:
					return 401;
				case 4755:
					return 2062;
				case 10887:
					return 5865;
				case 4151:
				case 15441:
				case 15442:
				case 15443:
				case 15444:
					return 11968; // Whip
				case 1215:
				case 1231:
				case 5680:
				case 5698:
				case 13465:
				case 13467:
				case 13976:
				case 13978:
					return 402; // Dragon daggers
				case 4214:
				case 6724:
				case 4212:
				case 4827:
				case 11235:
				case 841:
				case 843:
				case 849:
				case 853:
				case 856:
				case 861:
				case 839:
				case 845:
				case 847:
				case 851:
				case 855:
				case 859:
				case 20171:
					return 426; // Bows
				case 18357:
					return 4230; // Crossbows
				case 4734:
					return 2075; // Karil x-bow
				case 6528:
					return 2661; // Obby maul
				case 1434:
					if (player.getSettings().getCombatStyle() == Combat.CombatStyle.STAB)
						return 400;
					return 401;
				case 1305:
					if (player.getSettings().getCombatStyle() == Combat.CombatStyle.STAB)
						return 12310;
					return 12311;
				}
			}
		} else {
			return 1403;
		}
		return 422;
	}

}
