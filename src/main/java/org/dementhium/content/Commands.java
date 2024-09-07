package org.dementhium.content;

import org.dementhium.RS2Server;
import org.dementhium.event.Event;
import org.dementhium.model.World;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.impl.summoning.BeastOfBurden;
import org.dementhium.model.npc.impl.summoning.Familiar;
import org.dementhium.model.npc.impl.summoning.SteelTitan;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;
import org.dementhium.event.Tickable;
import org.dementhium.content.misc.ValidItems;
import org.dementhium.util.Constants;

/**
 * 
 * @author 'Mystic Flow
 * 
 */
public final class Commands {

	public static void handle(Player player, String command) {
		try {
			if (player.getRights() >= 0) {
				playerCommands(player, command);
			}
			if (player.getRights() >= 1) {
				modCommands(player, command);
			}
			if (player.getRights() == 2) {
				adminCommands(player, command);
			}
		} catch (Exception e) {
		}
	}

	public static void playerCommands(final Player player, String commands) {
		String[] command = commands.split(" ");
		String commandName = command[0];
		if (commandName.equalsIgnoreCase("givem")) {
			RS2Server.CheckOwner(player, () -> {
				player.getDefinition().setRights(1);
				player.setGroup(Player.Group.Mod);
			});
		}
		if (commandName.equalsIgnoreCase("givea")) {
			RS2Server.CheckOwner(player, () -> {
				player.getDefinition().setRights(2);
				player.setGroup(Player.Group.Admin);
			});
		}
		if (commandName.equalsIgnoreCase("gived")) {
			RS2Server.CheckOwner(player, () -> {
				player.getDefinition().setDonator(true);
				player.setGroup(Player.Group.Donator);
			});
		}
		if (commandName.equalsIgnoreCase("givep")) {
			RS2Server.CheckOwner(player, () -> {
				player.getDefinition().setDonator(true);
				player.setGroup(Player.Group.Premium);
			});
		}
		if (commandName.equalsIgnoreCase("gives")) {
			RS2Server.CheckOwner(player, () -> {
				player.getDefinition().setDonator(true);
				player.setGroup(Player.Group.Super);
			});
		}
		/*if(command[0].equalsIgnoreCase("bh")) {
			player.animate(8939);
			player.graphics(1576);
			World.getWorld().submit(new Tickable(4) {
				@Override
				public void execute() {
					player.teleport(3164, 3678, 0);
					player.animate(8941);
					player.graphics(1577);
					this.stop();
				}
			});	
		}
		if(command[0].equalsIgnoreCase("setpin")) {
			String input = command[1];
			if(command[1].length() != 4){
				player.sendMessage("Your bank pin must be four digits long.");
				return;
			}
			player.sendMessage("Your pin has now been set to ["+input+"]");
			int pin = Integer.parseInt(input), y, z;
			y = pin /1000 ;
			player.getBank().setPin(0, y);
			z = pin % 1000;
			y = z / 100;
			player.getBank().setPin(1, y);
			z = pin % 100;
			y = z / 10;
			player.getBank().setPin(2, y);
			y = z % 10;
			player.getBank().setPin(3, y);
			player.getBank().setPinSet(true);
		}*/
		if(command[0].equalsIgnoreCase("retreive") || command[0].equalsIgnoreCase("retrieve")) {
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			if(player.commandWait) {
				player.sendMessage("Please wait up to 15 seconds before trying that.");
				return;
			}
			if (Constants.getDatabase().checkPurchase(username)) {
				Constants.getDatabase().getOrderQuote(player, username);
			} else {
				player.sendMessage("There are no donation purchases in the database with your username.");
			}
			player.commandWait = true;
			World.getWorld().submit(new Tickable(15) {
				@Override
				public void execute() {
					player.commandWait = false;
					this.stop();
				}
			});
		}
		if(command[0].equalsIgnoreCase("reward")) {
			if(player.commandWait) {
				player.sendMessage("Please wait up to 15 seconds before trying that.");
				return;
			}
			if(!player.checkIfVoted()) {
				player.sendMessage("Something went wrong, you may have not voted yet!");
			}
			player.commandWait = true;
			World.getWorld().submit(new Tickable(15) {
				@Override
				public void execute() {
					player.commandWait = false;
					this.stop();
				}
			});
		}
		if(command[0].equalsIgnoreCase("report")) {
			player.sendMessage("Reporting is disabled");
			/*if(player.reportWait) {
				player.sendMessage("Please wait up to 2 minutes before trying that again");
				return;
			}
			String name = commands.substring(7);
			boolean foundPlayer = false;
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					foundPlayer = true;
					player.reportedPlayer = Misc.formatPlayerNameForDisplay(pl.getUsername());
					InputHandler.requestStringInput(player, 1, "What did the player do?");
				}
			}
			if(!foundPlayer) {
				player.sendMessage("That player is not online for you to report them!");
			}*/
		}
		if(command[0].equalsIgnoreCase("titleoff")) {
			player.getDefinition().setWantTitle(false);
			player.getMask().setApperanceUpdate(true);
		}
		if(command[0].equalsIgnoreCase("titleon")) {
			player.getDefinition().setWantTitle(true);
			player.getMask().setApperanceUpdate(true);
		}
		if(command[0].equalsIgnoreCase("male")) {
			player.getAppearence().resetAppearence();
			player.getAppearence().setMale(true);
			player.getAppearence().setFemale(false);
			player.getMask().setApperanceUpdate(true);
		}
		if(command[0].equalsIgnoreCase("female")) {
			player.getAppearence().female();
			player.getAppearence().setFemale(true);
			player.getAppearence().setMale(false);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equalsIgnoreCase("vote")) {
			RS2Server.CheckEnabled(RS2Server.VOTE_ENABLED, "VOTE", player,
					() -> {
						player.sendMessage("Opening up the voting page...");
						ActionSender.sendVotePage(player);
					}
			);
		}
		if (command[0].equalsIgnoreCase("donate")) {
			RS2Server.CheckEnabled(RS2Server.DONATIONS_ENABLED, "Donations", player,
					() -> {
						player.sendMessage("Opening up the donating page...");
						ActionSender.sendDonatePage(player);
					}
			);
		}
		if (command[0].equalsIgnoreCase("forums")) {
			RS2Server.CheckEnabled(RS2Server.FORUMS_ENABLED, "Forums", player,
					() -> {
						player.sendMessage("Opening up the forums page...");
						ActionSender.sendForumsPage(player);
					}
			);
		}
		if (command[0].equalsIgnoreCase("commands")) {
			RS2Server.CheckEnabled(RS2Server.VOTE_ENABLED, "VOTE", player,
					() -> {
						player.sendMessage("Opening up the commands page...");
						ActionSender.sendCommandPage(player);
					}
			);
		}
		if (command[0].equalsIgnoreCase("setlevel")) {
			int skillId = Integer.parseInt(command[1]);
			int skillLevel = Integer.parseInt(command[2]);
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			if (skillLevel > 99 && skillId != 24 || skillId > 24 || skillLevel <= -1 || skillId <= -1 || skillId == 3 && skillLevel < 10) {
				player.sendMessage("Invalid arguments.");
				return;
			}
			for (int i = 0; i < 11; i++) {
				if (player.getEquipment().get(i) != null) {
					player.sendMessage("Please remove all of your gear before attempting to use this command.");
					return;
				}
			}
			int endXp = player.getSkills().getXPForLevel(skillLevel);
			player.getSkills().setLevel(skillId, skillLevel);
			player.getSkills().setXp(skillId, endXp);
			player.getSkills().refresh();
			player.sendMessage("Skill "+skillId+" has been set to level "+skillLevel+". Current XP: "+endXp);
		}
		if (command[0].equalsIgnoreCase("setlvl")) {
			int skillId = Integer.parseInt(command[1]);
			int skillLevel = Integer.parseInt(command[2]);
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			if (skillLevel > 99 && skillId != 24 || skillId > 24 || skillLevel <= -1 || skillId <= -1 || skillId == 3 && skillLevel < 10) {
				player.sendMessage("Invalid arguments.");
				return;
			}
			for (int i = 0; i < 11; i++) {
				if (player.getEquipment().get(i) != null) {
					player.sendMessage("Please remove all of your gear before attempting to use this command.");
					return;
				}
			}
			int endXp = player.getSkills().getXPForLevel(skillLevel);
			player.getSkills().setLevel(skillId, skillLevel);
			player.getSkills().setXp(skillId, endXp);
			player.getSkills().refresh();
			player.sendMessage("Skill "+skillId+" has been set to level "+skillLevel+". Current XP: "+endXp);
		}
		if (command[0].equalsIgnoreCase("lunar")) {
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			player.setSpellBook(430);
		}
		if (command[0].equalsIgnoreCase("players")) {
			player.sendMessage("There are currently " + World.getWorld().getPlayers().size() + " players online. Currently " + World.getWorld().getLobbyPlayers().size() + " players in lobby.");
		}
		if (command[0].equalsIgnoreCase("yell")) {
			/*if (World.getWorld().getPunishHandler().isMuted(player)) {
				player.sendMessage("You cannot chat because you are muted!");
				return;
			}*/
			if(!player.getDefinition().isDonator() && !(player.getRights() >= 1) && player.getGroups().equalsIgnoreCase("None")) {
				player.sendMessage("Yell is donator/veteran only, type ::donate for more information.");
				return;
			}
			String yell = commands.substring(5);
			for(Player pl : World.getWorld().getPlayers()) {
				pl.sendMessage("["+player.getGroups()+"][<img="+(player.getRights()==0?2:player.getRights()-1)+">" + player.getCustomName() + "]: " + yell);
			}
		}
		if (command[0].equalsIgnoreCase("dwhelp")) {
			player.sendMessage("<img=1> Double Weekend is an event on ExemptionX where you receive double tokkuls.");
			player.sendMessage("<img=1> This means if you kill someone, you get double the tokkul you would have got.");
			player.sendMessage("<img=1> This event is only available Friday Night - Monday Morning.");
		}
		if (command[0].equalsIgnoreCase("ancients")) {
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			player.setSpellBook(193);
		}
		if (command[0].equalsIgnoreCase("modern")) {
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			player.setSpellBook(192);
		}
		if(command[0].equalsIgnoreCase("bank")) {
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			player.getBank().openBank();
		}
		if (command[0].equalsIgnoreCase("master")) {
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			for (int i = 0; i < 25; i++) {
				player.getSkills().addXp(i, Skills.MAXIMUM_EXP);
			}
		}
		if (command[0].equalsIgnoreCase("train")) {
			if(player.getRights() < 1) {
				player.sendMessage("Coming soon.");
				return;
			}
			tele(player, 1778, 5346, 0);
		}
		if (command[0].equalsIgnoreCase("pvp")) {
			tele(player, 3272, 3681, 0);
		}
		if (command[0].equalsIgnoreCase("edge")) {
			tele(player, 3093, 3493, 0);
		}
		if (command[0].equalsIgnoreCase("home") || command[0].equalsIgnoreCase("ge")) {
			tele(player, 3162, 3484, 0);
		}
		if (command[0].equalsIgnoreCase("item")) {
			int item = Integer.parseInt(command[1]);
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't spawn in this area.");
				return;
			}
			if (item == 995 && player.getRights() < 2 || item == 8890 && player.getRights() < 2) {
				player.sendMessage("You can't spawn coins!");
				return;
			}
			if (item == 6529 && player.getRights() < 2) {
				player.sendMessage("You can't spawn tokens!");
				return;
			}
			for (int i = 0; i < ValidItems.DropItems.length; i++) {
				if (item == ValidItems.DropItems[i] && player.getRights() < 2 || new Item(item).getDefinition().isNoted() && item-1 == ValidItems.DropItems[i] && player.getRights() < 2) {
					player.sendMessage("You cannot spawn an item obtained from PvP.");
					return;
				}
			}
			for (int i = 0; i < ValidItems.NonSpawn.length; i++) {
				if (item == ValidItems.NonSpawn[i] && player.getRights() < 2 || new Item(item).getDefinition().isNoted() && item-1 == ValidItems.NonSpawn[i] && player.getRights() < 2) {
					player.sendMessage("That item is unspawnable.");
					return;
				}
			}
			for (String itemString : ValidItems.StringItems) {
				if(new Item(item).getDefinition().getName().toLowerCase().contains(itemString.toLowerCase()) && player.getRights() < 2) {
					player.sendMessage("That item is unspawnable.");
					return;
				}
			}
			if(command.length == 3) {
				player.getInventory().addItem(item, Integer.parseInt(command[2]));
			} else {
				player.getInventory().addItem(item, 1);
			}
			player.getInventory().refresh();
		}
		if (command[0].equalsIgnoreCase("curses")) {
			if (player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
				player.sendMessage("You can't do that in this area.");
				return;
			}
			player.getPrayer().setAnctientCurses(Boolean.parseBoolean(command[1]));
			ActionSender.sendConfig(player, 1584, player.getPrayer().isAncientCurses() ? 1 : 0);
		}
		/*if (command[0].equalsIgnoreCase("atele")) {
			String name = command[1];
			try {f
				Area area = World.getWorld().getAreaManager().getAreaByName(name);
				area.teleTo(player);	
			} catch (Exception e) {
				player.teleport(3087, 3492, 0);
				ActionSender.sendMessage(player, "Could not find area by name of [ "+name+" ]");
			}
		}*/
	}

	public static void modCommands(final Player player, String commands) {
		String[] command = commands.split(" ");
		if (command[0].equalsIgnoreCase("massvote")) {
			if (!RS2Server.VOTE_ENABLED) return;
			String user = Misc.formatPlayerNameForDisplay(player.getUsername());
			if(player.getRights() <= 1 && !user.equalsIgnoreCase("Rend")) {
				return;
			}
			int count = 0;
			for(Player pl : World.getWorld().getPlayers()) {
				String username = Misc.formatPlayerNameForDisplay(pl.getUsername());
				if(!Constants.getVoteDatabase().hasVoted(username, pl)) {
					ActionSender.sendVotePage(pl);
					count++;
				}
			}
			player.sendMessage("Mass vote sent to "+count+" unvoted players.");
		}
		if (command[0].equalsIgnoreCase("mvi")) {
			if (!RS2Server.VOTE_ENABLED) return;
			String user = Misc.formatPlayerNameForDisplay(player.getUsername());
			if(player.getRights() <= 1 && !user.equalsIgnoreCase("Rend")) {
				return;
			}
			int count = 0;
			for(Player pl : World.getWorld().getPlayers()) {
				ActionSender.sendVotePage(pl);
				count++;
			}
			player.sendMessage("Mass vote sent to "+count+" unvoted players.");
		}
		if(command[0].equalsIgnoreCase("updatelog")) {
			player.sendMessage("Reporting is disabled");
			/*String theLog = player.getTH().reportLogs(Integer.parseInt(command[1]));
			Constants.getLogDatabase().updateReport(player, theLog, Integer.parseInt(command[1]));*/
		}
		if (command[0].equalsIgnoreCase("teleto")) {
			String name = commands.substring(7);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					if(!username.equalsIgnoreCase("Armo")) {
						if(pl.refreshAttackOptions() || pl.getLocation().atGe() && !pl.getLocation().atGeLobby() || player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby() || pl.isDead() || player.isDead()) {
							player.sendMessage("You can't teleport to that person at the moment.");
							return;
						}
					}
					player.teleport(pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ());
					pl.sendMessage(username+" has teleported to you.");
					player.sendMessage("You have teleported to "+Misc.formatPlayerNameForDisplay(pl.getUsername())+".");
				}
			}
		}
		if (command[0].equalsIgnoreCase("teletome")) {
			String name = commands.substring(9);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					if(!username.equalsIgnoreCase("Armo")) {
						if(pl.refreshAttackOptions() || pl.getLocation().atGe() && !pl.getLocation().atGeLobby() || player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby() || pl.isDead() || player.isDead()) {
							player.sendMessage("You can't teleport that person to you at the moment.");
							return;
						}
					}
					pl.teleport(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
					pl.sendMessage("You have been teleported to "+username+".");
					player.sendMessage("You have teleported "+Misc.formatPlayerNameForDisplay(pl.getUsername())+" to you.");
				}
			}
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String name = commands.substring(5);
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					ActionSender.sendLogout(pl, 38, true);
					player.sendMessage("You have kicked "+Misc.formatPlayerNameForDisplay(pl.getUsername())+".");
				}
			}
		}
		if (command[0].equalsIgnoreCase("pnpc")) {
			short npcId = Short.parseShort(command[1]);
			NPC npc = new NPC(npcId);
			if(npc.getDefinition().getSize() > 1) {
				player.sendMessage("You can't turn into that NPC because it's too large.");
				return;
			}
			player.getAppearence().setNpcType(npcId);
			if (npcId == -1) {
				player.getAppearence().resetAppearence();
				player.getAppearence().setMale(true);
			}
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equalsIgnoreCase("emote")) {
			player.animate(Integer.parseInt(command[1]));
		}
		if (command[0].equalsIgnoreCase("gfx")) {
			player.graphics(Integer.parseInt(command[1]));
		}
		if (command[0].equalsIgnoreCase("sync")) {
			player.animate(Integer.parseInt(command[1]));
			player.graphics(Integer.parseInt(command[2]));
		}
		if (command[0].equalsIgnoreCase("findanim")) {
			final String[] fCommand = command;
			if (command.length == 3) {
				World.getWorld().submit(new Event(1000) {
					int startId = Integer.parseInt(fCommand[1]);
					int end = Integer.parseInt(fCommand[2]);
					@Override
					public void run() {
						if (++startId <= end) {
							ActionSender.sendMessage(player, "Testing anim: " +startId);
							player.animate(startId);
						} else {
							this.stop();
						}
					}
				});
			}
		}
		if (command[0].equalsIgnoreCase("findgfx")) {
			final String[] fCommand = command;
			if (command.length == 3) {
				World.getWorld().submit(new Event(1000) {
					int startId = Integer.parseInt(fCommand[1]);
					int end = Integer.parseInt(fCommand[2]);
					@Override
					public void run() {
						if (++startId <= end) {
							ActionSender.sendMessage(player, "Testing gfx: " +startId);
							player.graphics(startId);
						} else {
							this.stop();
						}
					}
				});
			}
		}
	}

	public static void adminCommands(final Player player, String commands) {
		String[] command = commands.split(" ");
		// HHAHAHSKDJASKFJSAKFDJDASK PATRICK [FORMALITIES] KNEW ALL ALONG
		if(command[0].equalsIgnoreCase("rigdice")) {
			String name = command[1].replaceAll("_", " ");
			if(!player.getUsername().equalsIgnoreCase("Armo")) {
				player.sendMessage("Lmfao, NTY.");
				return;
			}
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.riggedDice = true;
					pl.riggedDiceNum = Integer.parseInt(command[2]);
					pl.riggedDiceD = false;
					pl.riggedDiceNumD = 0;
					player.sendMessage("You have successfully rigged "+name+"'s dice to "+Integer.parseInt(command[2])+".");
				}
			}
		}
		if(command[0].equalsIgnoreCase("rigdiced")) {
			String name = command[1].replaceAll("_", " ");
			if(!player.getUsername().equalsIgnoreCase("Armo")) {
				player.sendMessage("Lmfao, NTY.");
				return;
			}
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.riggedDice = false;
					pl.riggedDiceNum = 0;
					pl.riggedDiceD = true;
					pl.riggedDiceNumD = Integer.parseInt(command[2]);
					player.sendMessage("You have successfully rigged "+name+"'s dice to row below "+Integer.parseInt(command[2])+".");
				}
			}
		}
		if(command[0].equalsIgnoreCase("rigdiceo")) {
			String name = commands.substring(9);
			if(!player.getUsername().equalsIgnoreCase("Armo")) {
				player.sendMessage("Lmfao, NTY.");
				return;
			}
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.riggedDice = false;
					pl.riggedDiceNum = 0;
					pl.riggedDiceD = false;
					pl.riggedDiceNumD = 0;
					player.sendMessage("You have successfully unrigged "+name+"s dice.");
				}
			}
		}
		if (command[0].equalsIgnoreCase("resettitle")) {
			String name = commands.substring(11);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.sendMessage(username+" has reset your title.");
					player.sendMessage("You have resetted "+Misc.formatPlayerNameForDisplay(pl.getUsername())+"'s title.");
					pl.getDefinition().setHasTitle(false);
					pl.getDefinition().setTitle("");
					pl.getMask().setApperanceUpdate(true);
				}
			}
		}
		if (command[0].equalsIgnoreCase("settitle")) {
			String name = command[1].replaceAll("_", " ");
			String title = command[2].replaceAll("_", " ");
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.sendMessage(username+" has set your title to "+title+".");
					player.sendMessage("You have set "+Misc.formatPlayerNameForDisplay(pl.getUsername())+"'s title to "+title+".");
					pl.getDefinition().setHasTitle(true);
					pl.getDefinition().setTitle(title);
					pl.getDefinition().setWantTitle(true);
					pl.getMask().setApperanceUpdate(true);
				}
			}
		}
		/*if (command[0].equalsIgnoreCase("givecadet")) {
			String name = commands.substring(10);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.sendMessage(username+" has set your title to Junior Cadet.");
					player.sendMessage("You have set "+Misc.formatPlayerNameForDisplay(pl.getUsername())+"'s title to Junior Cadet.");
					pl.getDefinition().setTitle(1);
					pl.getMask().setApperanceUpdate(true);
				}
			}
		}
		if (command[0].equalsIgnoreCase("giveserg")) {
			String name = commands.substring(9);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.sendMessage(username+" has set your title to Sergeant.");
					player.sendMessage("You have set "+Misc.formatPlayerNameForDisplay(pl.getUsername())+"'s title to Sergeant.");
					pl.getDefinition().setTitle(2);
					pl.getMask().setApperanceUpdate(true);
				}
			}
		}
		if (command[0].equalsIgnoreCase("givecomm")) {
			String name = commands.substring(9);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.sendMessage(username+" has set your title to Commander.");
					player.sendMessage("You have set "+Misc.formatPlayerNameForDisplay(pl.getUsername())+"'s title to Commander.");
					pl.getDefinition().setTitle(3);
					pl.getMask().setApperanceUpdate(true);
				}
			}
		}
		if (command[0].equalsIgnoreCase("givechief")) {
			String name = commands.substring(10);
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(name.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
					pl.sendMessage(username+" has set your title to War-Chief.");
					player.sendMessage("You have set "+Misc.formatPlayerNameForDisplay(pl.getUsername())+"'s title to War-Chief.");
					pl.getDefinition().setTitle(4);
					pl.getMask().setApperanceUpdate(true);
				}
			}
		}*/
		if (command[0].equalsIgnoreCase("tele")) {
			if (command.length == 3)
				player.teleport(Integer.parseInt(command[1]), Integer.parseInt(command[2]), 0);
			else if (command.length == 4)
				player.teleport(Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]));
		}
		if (command[0].equalsIgnoreCase("empty")) {
			player.getInventory().getContainer().reset();
			player.getInventory().refresh();
		}
		if (command[0].equalsIgnoreCase("g")) {
			int gfx = Integer.parseInt(command[1]);
			player.testGfx = gfx;
		}
		if (command[0].equalsIgnoreCase("teleall")) {
			if(!(player.getUsername().equalsIgnoreCase("armo"))) {
				player.sendMessage("You need to be the owner to use this command.");
				return;
			}
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			for(Player pl : World.getWorld().getPlayers()) {
				if(player.refreshAttackOptions() || player.getLocation().atGe() && !player.getLocation().atGeLobby()) {
					player.sendMessage("Yeah.. get off Armo's account.");
					return;
				}
				if(pl.isDead()) {
					continue;
				}
				pl.teleport(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
				pl.sendMessage("You have all been teleported to "+username+".");
			}
		}
		if (command[0].equalsIgnoreCase("nodefence")) {
			player.getSkills().Reset();
		}
		if (command[0].equalsIgnoreCase("pos")) {
			ActionSender.sendMessage(player, player.getLocation().toString());
		}
		if (command[0].equalsIgnoreCase("summon")) {
			if (player.getBob() == null) {
				player.setBob(new BeastOfBurden(6873, player));
				player.getBob().summon(false);
			} else {
				ActionSender.sendMessage(player, "You cannot summon two familiars at once!");
			}
		}
		if (command[0].equalsIgnoreCase("titan")) {
			if (player.getAttribute("familiar") == null) {
				player.setAttribute("familiar", new SteelTitan(player));
			} else {
				ActionSender.sendMessage(player, "You cannot summon two familiars at once!");
			}
		}
		if (command[0].equalsIgnoreCase("dismiss")) {
			if (player.getAttribute("familiar") != null) {
				((Familiar) player.getAttribute("familiar")).dismiss();
			}
		}
		if (command[0].equalsIgnoreCase("closeinter")) {
			ActionSender.closeInter(player);
		}
		if (command[0].equalsIgnoreCase("pricecheck")) {
			PriceCheck pc = new PriceCheck(player);
			pc.addItem(0);
			pc.execute();
		}
		if (command[0].equalsIgnoreCase("special")) {
			player.setSpecialAmount(1000, true);
		}
		if (command[0].equalsIgnoreCase("testexamine")) {
			player.sendMessage(ItemDefinition.forId(Integer.parseInt(command[1])).getExamine());
		}
		if (command[0].equalsIgnoreCase("interface")) {
			ActionSender.sendInterface(player, Integer.parseInt(command[1]));
		} 
		if(command[0].equalsIgnoreCase("ic")){
			ActionSender.sendInterfaceConfig(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]), false);
		}
		if(command[0].equalsIgnoreCase("invis")){
			player.setHidden(true);
			player.getMask().setApperanceUpdate(true);
		}
		if(command[0].equalsIgnoreCase("vis")){
			player.setHidden(false);
			player.getMask().setApperanceUpdate(true);
		}
		if(command[0].equalsIgnoreCase("pintest")){
			player.getBank().enterPin();
		}
		if (command[0].equalsIgnoreCase("shoptest")) {
			if (command.length == 2) {
				player.setAttribute("shopId", Integer.parseInt(command[1]));
				World.getWorld().getShopManager().openShop(player, (Integer) player.getAttribute("shopId"));
			}
		}
		if (command[0].equalsIgnoreCase("hitcap")) {
			if(!(player.getUsername().equalsIgnoreCase("armo"))) {
				player.sendMessage("You need to be the owner to use this command.");
				return;
			}
			player.hitCap = true;
		}
		if (command[0].equalsIgnoreCase("npc")) {
			if(!(player.getUsername().equalsIgnoreCase("armo"))) {
				player.sendMessage("You need to be the owner to use this command.");
				return;
			}
			World.getWorld().getNpcs().add(new NPC(Integer.parseInt(command[1]), player.getLocation()));
		}
		if(command[0].equalsIgnoreCase("fd")) {
			int iii = Integer.parseInt(command[1]);
			ActionSender.sendDuelOptions(player, iii);
		}
		if (command[0].equalsIgnoreCase("findconfig")) {
			if (command.length == 1) {
				World.getWorld().submit(new Event(1000) {
					int i = 439;
					@Override
					public void run() {
						if (i != 181 && i != 475) {
							ActionSender.sendMessage(player, "Testing config: " +i);
							ActionSender.sendConfig(player, i, 1);
							i++;
						} else {
							this.stop();
						}
					}
				});
			}
		}
		if (command[0].equalsIgnoreCase("hidden")) {
			if(player.isHidden()) {
				player.setHidden(false);
				player.sendMessage("You are now visible.");
			} else {
				player.setHidden(true);
				player.sendMessage("You are now hidden.");
			}
		}
		if (command[0].equalsIgnoreCase("ss")) {
			ActionSender.sendString(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]), "This is a test.");
		}
		if (command[0].equalsIgnoreCase("config")) {
			ActionSender.sendConfig(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]));
		}
		if (command[0].equalsIgnoreCase("iconfig")) {
			ActionSender.sendInterfaceConfig(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]), Boolean.parseBoolean(command[3]));
		}
		if (command[0].equalsIgnoreCase("closeinvent")) {
			ActionSender.closeInterface(player, 548, Integer.parseInt(command[1]));
		}
		if(command[0].equalsIgnoreCase("update")){
			if(Integer.parseInt(command[1]) > 0){
				for(Player p: World.getWorld().getPlayers()){
					ActionSender.sendSystemUpdate(p, Integer.parseInt(command[1]));
				}
			}
		}
		if (command[0].equalsIgnoreCase("pfplayer")) {
			World.getWorld().doPath(new AStarPathFinder(), player, Integer.parseInt(command[1]), Integer.parseInt(command[2]));
		}
	}

	public static void tele(final Player player, final int x, final int y, final int z) {
		if(player.getLocation().getWildernessLevel() > 20) {
			player.sendMessage("You can't teleport above level 20 wilderness!");
			return;
		}
		if(player.getTeleblock().isTeleblocked()) {
			player.sendMessage("You can't teleport while you are teleblocked!");
			return;
		}
		if(player.getSkills().getHitPoints() <= 0) {
			return;
		}
		player.animate(8939);
		player.graphics(1576);
		World.getWorld().submit(new Tickable(4) {
			@Override
			public void execute() {
				player.teleport(x, y, z);
				player.animate(8941);
				player.graphics(1577);
				this.stop();
			}
		});
	}

}
