package org.dementhium.content

import org.dementhium.RS2Server
import org.dementhium.content.misc.ValidItems
import org.dementhium.event.Event
import org.dementhium.event.Tickable
import org.dementhium.model.Item
import org.dementhium.model.World
import org.dementhium.model.definition.ItemDefinition
import org.dementhium.model.map.AStarPathFinder
import org.dementhium.model.npc.NPC
import org.dementhium.model.npc.impl.summoning.BeastOfBurden
import org.dementhium.model.npc.impl.summoning.Familiar
import org.dementhium.model.npc.impl.summoning.SteelTitan
import org.dementhium.model.player.Player
import org.dementhium.model.player.skills.Skills
import org.dementhium.net.ActionSender
import org.dementhium.util.Constants
import org.dementhium.util.Misc
import java.util.*

/**
 *
 * @author 'Mystic Flow
 */
object Commands {
    fun handle(player: Player, command: String) {
        try {
            if (player.rights >= 0) {
                playerCommands(player, command)
            }
            if (player.rights >= 1) {
                modCommands(player, command)
            }
            if (player.rights == 2) {
                adminCommands(player, command)
            }
        } catch (e: Exception) {
        }
    }

    fun playerCommands(player: Player, commands: String) {
        val command = commands.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val commandName = command[0]
        if (commandName.equals("givem", ignoreCase = true)) {
            RS2Server.CheckOwner(player) {
                player.definition.rights = 1
                player.setGroup(Player.Group.Mod)
            }
        }
        if (commandName.equals("givea", ignoreCase = true)) {
            RS2Server.CheckOwner(player) {
                player.definition.rights = 2
                player.setGroup(Player.Group.Admin)
            }
        }
        if (commandName.equals("gived", ignoreCase = true)) {
            RS2Server.CheckOwner(player) {
                player.definition.isDonator = true
                player.setGroup(Player.Group.Donator)
            }
        }
        if (commandName.equals("givep", ignoreCase = true)) {
            RS2Server.CheckOwner(player) {
                player.definition.isDonator = true
                player.setGroup(Player.Group.Premium)
            }
        }
        if (commandName.equals("gives", ignoreCase = true)) {
            RS2Server.CheckOwner(player) {
                player.definition.isDonator = true
                player.setGroup(Player.Group.Super)
            }
        }
        if (command[0].equals("retreive", ignoreCase = true) || command[0].equals("retrieve", ignoreCase = true)) {
            val username = Misc.formatPlayerNameForDisplay(player.username)
            if (player.commandWait) {
                player.sendMessage("Please wait up to 15 seconds before trying that.")
                return
            }
            if (Constants.getDatabase().checkPurchase(username)) {
                Constants.getDatabase().getOrderQuote(player, username)
            } else {
                player.sendMessage("There are no donation purchases in the database with your username.")
            }
            player.commandWait = true
            World.getWorld().submit(object : Tickable(15) {
                override fun execute() {
                    player.commandWait = false
                    this.stop()
                }
            })
        }
        if (command[0].equals("reward", ignoreCase = true)) {
            if (player.commandWait) {
                player.sendMessage("Please wait up to 15 seconds before trying that.")
                return
            }
            if (!player.checkIfVoted()) {
                player.sendMessage("Something went wrong, you may have not voted yet!")
            }
            player.commandWait = true
            World.getWorld().submit(object : Tickable(15) {
                override fun execute() {
                    player.commandWait = false
                    this.stop()
                }
            })
        }
        if (command[0].equals("report", ignoreCase = true)) {
            player.sendMessage("Reporting is disabled")
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
        if (command[0].equals("titleoff", ignoreCase = true)) {
            player.definition.setWantTitle(false)
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("titleon", ignoreCase = true)) {
            player.definition.setWantTitle(true)
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("male", ignoreCase = true)) {
            player.appearence.resetAppearence()
            player.appearence.isMale = true
            player.appearence.isFemale = false
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("female", ignoreCase = true)) {
            player.appearence.female()
            player.appearence.isFemale = true
            player.appearence.isMale = false
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("vote", ignoreCase = true)) {
            RS2Server.CheckEnabled(
                RS2Server.VOTE_ENABLED, "VOTE", player
            ) {
                player.sendMessage("Opening up the voting page...")
                ActionSender.sendVotePage(player)
            }
        }
        if (command[0].equals("donate", ignoreCase = true)) {
            RS2Server.CheckEnabled(
                RS2Server.DONATIONS_ENABLED, "Donations", player
            ) {
                player.sendMessage("Opening up the donating page...")
                ActionSender.sendDonatePage(player)
            }
        }
        if (command[0].equals("forums", ignoreCase = true)) {
            RS2Server.CheckEnabled(
                RS2Server.FORUMS_ENABLED, "Forums", player
            ) {
                player.sendMessage("Opening up the forums page...")
                ActionSender.sendForumsPage(player)
            }
        }
        if (command[0].equals("commands", ignoreCase = true)) {
            RS2Server.CheckEnabled(
                RS2Server.VOTE_ENABLED, "VOTE", player
            ) {
                player.sendMessage("Opening up the commands page...")
                ActionSender.sendCommandPage(player)
            }
        }
        if (command[0].equals("setlevel", ignoreCase = true)) {
            val skillId = command[1].toInt()
            val skillLevel = command[2].toInt()
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            if (skillLevel > 99 && skillId != 24 || skillId > 24 || skillLevel <= -1 || skillId <= -1 || skillId == 3 && skillLevel < 10) {
                player.sendMessage("Invalid arguments.")
                return
            }
            for (i in 0..10) {
                if (player.equipment[i] != null) {
                    player.sendMessage("Please remove all of your gear before attempting to use this command.")
                    return
                }
            }
            val endXp = player.skills.getXPForLevel(skillLevel)
            player.skills.setLevel(skillId, skillLevel)
            player.skills.setXp(skillId, endXp.toDouble())
            player.skills.refresh()
            player.sendMessage("Skill $skillId has been set to level $skillLevel. Current XP: $endXp")
        }
        if (command[0].equals("setlvl", ignoreCase = true)) {
            val skillId = command[1].toInt()
            val skillLevel = command[2].toInt()
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            if (skillLevel > 99 && skillId != 24 || skillId > 24 || skillLevel <= -1 || skillId <= -1 || skillId == 3 && skillLevel < 10) {
                player.sendMessage("Invalid arguments.")
                return
            }
            for (i in 0..10) {
                if (player.equipment[i] != null) {
                    player.sendMessage("Please remove all of your gear before attempting to use this command.")
                    return
                }
            }
            val endXp = player.skills.getXPForLevel(skillLevel)
            player.skills.setLevel(skillId, skillLevel)
            player.skills.setXp(skillId, endXp.toDouble())
            player.skills.refresh()
            player.sendMessage("Skill $skillId has been set to level $skillLevel. Current XP: $endXp")
        }
        if (command[0].equals("lunar", ignoreCase = true)) {
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            player.setSpellBook(430)
        }
        if (command[0].equals("players", ignoreCase = true)) {
            player.sendMessage("There are currently " + World.getWorld().players.size() + " players online. Currently " + World.getWorld().lobbyPlayers.size() + " players in lobby.")
        }
        if (command[0].equals("yell", ignoreCase = true)) {
            /*if (World.getWorld().getPunishHandler().isMuted(player)) {
				player.sendMessage("You cannot chat because you are muted!");
				return;
			}*/
            if (!player.definition.isDonator && player.rights < 1 && player.groups.equals("None", ignoreCase = true)) {
                player.sendMessage("Yell is donator/veteran only, type ::donate for more information.")
                return
            }
            val yell = commands.substring(5)
            for (pl in World.getWorld().players) {
                pl.sendMessage("[" + player.groups + "][<img=" + (if (player.rights == 0) 2 else player.rights - 1) + ">" + player.customName + "]: " + yell)
            }
        }
        if (command[0].equals("dwhelp", ignoreCase = true)) {
            player.sendMessage("<img=1> Double Weekend is an event on ExemptionX where you receive double tokkuls.")
            player.sendMessage("<img=1> This means if you kill someone, you get double the tokkul you would have got.")
            player.sendMessage("<img=1> This event is only available Friday Night - Monday Morning.")
        }
        if (command[0].equals("ancients", ignoreCase = true)) {
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            player.setSpellBook(193)
        }
        if (command[0].equals("modern", ignoreCase = true)) {
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            player.setSpellBook(192)
        }
        if (command[0].equals("bank", ignoreCase = true)) {
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            player.bank.openBank()
        }
        if (command[0].equals("master", ignoreCase = true)) {
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            for (i in 0..24) {
                player.skills.addXp(i, Skills.MAXIMUM_EXP)
            }
        }
        if (command[0].equals("train", ignoreCase = true)) {
            if (player.rights < 1) {
                player.sendMessage("Coming soon.")
                return
            }
            tele(player, 1778, 5346, 0)
        }
        if (command[0].equals("pvp", ignoreCase = true)) {
            tele(player, 3272, 3681, 0)
        }
        if (command[0].equals("edge", ignoreCase = true)) {
            tele(player, 3093, 3493, 0)
        }
        if (command[0].equals("home", ignoreCase = true) || command[0].equals("ge", ignoreCase = true)) {
            tele(player, 3162, 3484, 0)
        }
        if (command[0].equals("item", ignoreCase = true)) {
            val item = command[1].toInt()
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't spawn in this area.")
                return
            }
            if (item == 995 && player.rights < 2 || item == 8890 && player.rights < 2) {
                player.sendMessage("You can't spawn coins!")
                return
            }
            if (item == 6529 && player.rights < 2) {
                player.sendMessage("You can't spawn tokens!")
                return
            }
            for (i in ValidItems.DropItems.indices) {
                if (item == ValidItems.DropItems[i] && player.rights < 2 || Item(item).definition.isNoted && item - 1 == ValidItems.DropItems[i] && player.rights < 2) {
                    player.sendMessage("You cannot spawn an item obtained from PvP.")
                    return
                }
            }
            for (i in ValidItems.NonSpawn.indices) {
                if (item == ValidItems.NonSpawn[i] && player.rights < 2 || Item(item).definition.isNoted && item - 1 == ValidItems.NonSpawn[i] && player.rights < 2) {
                    player.sendMessage("That item is unspawnable.")
                    return
                }
            }
            for (itemString in ValidItems.StringItems) {
                if (Item(item).definition.name.lowercase(Locale.getDefault())
                        .contains(itemString.lowercase(Locale.getDefault())) && player.rights < 2
                ) {
                    player.sendMessage("That item is unspawnable.")
                    return
                }
            }
            if (command.size == 3) {
                player.inventory.addItem(item, command[2].toInt())
            } else {
                player.inventory.addItem(item, 1)
            }
            player.inventory.refresh()
        }
        if (command[0].equals("curses", ignoreCase = true)) {
            if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                player.sendMessage("You can't do that in this area.")
                return
            }
            player.prayer.setAnctientCurses(command[1].toBoolean())
            ActionSender.sendConfig(player, 1584, if (player.prayer.isAncientCurses) 1 else 0)
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

    fun modCommands(player: Player, commands: String) {
        val command = commands.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (command[0].equals("massvote", ignoreCase = true)) {
            if (!RS2Server.VOTE_ENABLED) return
            val user = Misc.formatPlayerNameForDisplay(player.username)
            if (player.rights <= 1 && !user.equals("Rend", ignoreCase = true)) {
                return
            }
            var count = 0
            for (pl in World.getWorld().players) {
                val username = Misc.formatPlayerNameForDisplay(pl.username)
                if (!Constants.getVoteDatabase().hasVoted(username, pl)) {
                    ActionSender.sendVotePage(pl)
                    count++
                }
            }
            player.sendMessage("Mass vote sent to $count unvoted players.")
        }
        if (command[0].equals("mvi", ignoreCase = true)) {
            if (!RS2Server.VOTE_ENABLED) return
            val user = Misc.formatPlayerNameForDisplay(player.username)
            if (player.rights <= 1 && !user.equals("Rend", ignoreCase = true)) {
                return
            }
            var count = 0
            for (pl in World.getWorld().players) {
                ActionSender.sendVotePage(pl)
                count++
            }
            player.sendMessage("Mass vote sent to $count unvoted players.")
        }
        if (command[0].equals("updatelog", ignoreCase = true)) {
            player.sendMessage("Reporting is disabled")
            /*String theLog = player.getTH().reportLogs(Integer.parseInt(command[1]));
			Constants.getLogDatabase().updateReport(player, theLog, Integer.parseInt(command[1]));*/
        }
        if (command[0].equals("teleto", ignoreCase = true)) {
            val name = commands.substring(7)
            val username = Misc.formatPlayerNameForDisplay(player.username)
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    if (!username.equals("Armo", ignoreCase = true)) {
                        if (pl.refreshAttackOptions() || pl.location.atGe() && !pl.location.atGeLobby() || player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby() || pl.isDead || player.isDead) {
                            player.sendMessage("You can't teleport to that person at the moment.")
                            return
                        }
                    }
                    player.teleport(pl.location.x, pl.location.y, pl.location.z)
                    pl.sendMessage("$username has teleported to you.")
                    player.sendMessage("You have teleported to " + Misc.formatPlayerNameForDisplay(pl.username) + ".")
                }
            }
        }
        if (command[0].equals("teletome", ignoreCase = true)) {
            val name = commands.substring(9)
            val username = Misc.formatPlayerNameForDisplay(player.username)
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    if (!username.equals("Armo", ignoreCase = true)) {
                        if (pl.refreshAttackOptions() || pl.location.atGe() && !pl.location.atGeLobby() || player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby() || pl.isDead || player.isDead) {
                            player.sendMessage("You can't teleport that person to you at the moment.")
                            return
                        }
                    }
                    pl.teleport(player.location.x, player.location.y, player.location.z)
                    pl.sendMessage("You have been teleported to $username.")
                    player.sendMessage("You have teleported " + Misc.formatPlayerNameForDisplay(pl.username) + " to you.")
                }
            }
        }
        if (command[0].equals("kick", ignoreCase = true)) {
            val name = commands.substring(5)
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    ActionSender.sendLogout(pl, 38, true)
                    player.sendMessage("You have kicked " + Misc.formatPlayerNameForDisplay(pl.username) + ".")
                }
            }
        }
        if (command[0].equals("pnpc", ignoreCase = true)) {
            val npcId = command[1].toShort()
            val npc = NPC(npcId.toInt())
            if (npc.definition.size > 1) {
                player.sendMessage("You can't turn into that NPC because it's too large.")
                return
            }
            player.appearence.npcType = npcId
            if (npcId.toInt() == -1) {
                player.appearence.resetAppearence()
                player.appearence.isMale = true
            }
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("emote", ignoreCase = true)) {
            player.animate(command[1].toInt())
        }
        if (command[0].equals("gfx", ignoreCase = true)) {
            player.graphics(command[1].toInt())
        }
        if (command[0].equals("sync", ignoreCase = true)) {
            player.animate(command[1].toInt())
            player.graphics(command[2].toInt())
        }
        if (command[0].equals("findanim", ignoreCase = true)) {
            val fCommand = command
            if (command.size == 3) {
                World.getWorld().submit(object : Event(1000) {
                    var startId: Int = fCommand[1].toInt()
                    var end: Int = fCommand[2].toInt()
                    override fun run() {
                        if (++startId <= end) {
                            ActionSender.sendMessage(player, "Testing anim: $startId")
                            player.animate(startId)
                        } else {
                            this.stop()
                        }
                    }
                })
            }
        }
        if (command[0].equals("findgfx", ignoreCase = true)) {
            val fCommand = command
            if (command.size == 3) {
                World.getWorld().submit(object : Event(1000) {
                    var startId: Int = fCommand[1].toInt()
                    var end: Int = fCommand[2].toInt()
                    override fun run() {
                        if (++startId <= end) {
                            ActionSender.sendMessage(player, "Testing gfx: $startId")
                            player.graphics(startId)
                        } else {
                            this.stop()
                        }
                    }
                })
            }
        }
    }

    fun adminCommands(player: Player, commands: String) {
        val command = commands.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // HHAHAHSKDJASKFJSAKFDJDASK PATRICK [FORMALITIES] KNEW ALL ALONG
        if (command[0].equals("rigdice", ignoreCase = true)) {
            val name = command[1].replace("_".toRegex(), " ")
            if (!player.username.equals("Armo", ignoreCase = true)) {
                player.sendMessage("Lmfao, NTY.")
                return
            }
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    pl.riggedDice = true
                    pl.riggedDiceNum = command[2].toInt()
                    pl.riggedDiceD = false
                    pl.riggedDiceNumD = 0
                    player.sendMessage("You have successfully rigged " + name + "'s dice to " + command[2].toInt() + ".")
                }
            }
        }
        if (command[0].equals("rigdiced", ignoreCase = true)) {
            val name = command[1].replace("_".toRegex(), " ")
            if (!player.username.equals("Armo", ignoreCase = true)) {
                player.sendMessage("Lmfao, NTY.")
                return
            }
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    pl.riggedDice = false
                    pl.riggedDiceNum = 0
                    pl.riggedDiceD = true
                    pl.riggedDiceNumD = command[2].toInt()
                    player.sendMessage("You have successfully rigged " + name + "'s dice to row below " + command[2].toInt() + ".")
                }
            }
        }
        if (command[0].equals("rigdiceo", ignoreCase = true)) {
            val name = commands.substring(9)
            if (!player.username.equals("Armo", ignoreCase = true)) {
                player.sendMessage("Lmfao, NTY.")
                return
            }
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    pl.riggedDice = false
                    pl.riggedDiceNum = 0
                    pl.riggedDiceD = false
                    pl.riggedDiceNumD = 0
                    player.sendMessage("You have successfully unrigged " + name + "s dice.")
                }
            }
        }
        if (command[0].equals("resettitle", ignoreCase = true)) {
            val name = commands.substring(11)
            val username = Misc.formatPlayerNameForDisplay(player.username)
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    pl.sendMessage("$username has reset your title.")
                    player.sendMessage("You have resetted " + Misc.formatPlayerNameForDisplay(pl.username) + "'s title.")
                    pl.definition.setHasTitle(false)
                    pl.definition.setTitle("")
                    pl.mask.isApperanceUpdate = true
                }
            }
        }
        if (command[0].equals("settitle", ignoreCase = true)) {
            val name = command[1].replace("_".toRegex(), " ")
            val title = command[2].replace("_".toRegex(), " ")
            val username = Misc.formatPlayerNameForDisplay(player.username)
            for (pl in World.getWorld().players) {
                if (name.equals(Misc.formatPlayerNameForDisplay(pl.username), ignoreCase = true)) {
                    pl.sendMessage("$username has set your title to $title.")
                    player.sendMessage("You have set " + Misc.formatPlayerNameForDisplay(pl.username) + "'s title to " + title + ".")
                    pl.definition.setHasTitle(true)
                    pl.definition.setTitle(title)
                    pl.definition.setWantTitle(true)
                    pl.mask.isApperanceUpdate = true
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
        if (command[0].equals("tele", ignoreCase = true)) {
            if (command.size == 3) player.teleport(command[1].toInt(), command[2].toInt(), 0)
            else if (command.size == 4) player.teleport(command[1].toInt(), command[2].toInt(), command[3].toInt())
        }
        if (command[0].equals("empty", ignoreCase = true)) {
            player.inventory.container.reset()
            player.inventory.refresh()
        }
        if (command[0].equals("g", ignoreCase = true)) {
            val gfx = command[1].toInt()
            player.testGfx = gfx
        }
        if (command[0].equals("teleall", ignoreCase = true)) {
            if (!(player.username.equals("armo", ignoreCase = true))) {
                player.sendMessage("You need to be the owner to use this command.")
                return
            }
            val username = Misc.formatPlayerNameForDisplay(player.username)
            for (pl in World.getWorld().players) {
                if (player.refreshAttackOptions() || player.location.atGe() && !player.location.atGeLobby()) {
                    player.sendMessage("Yeah.. get off Armo's account.")
                    return
                }
                if (pl.isDead) {
                    continue
                }
                pl.teleport(player.location.x, player.location.y, player.location.z)
                pl.sendMessage("You have all been teleported to $username.")
            }
        }
        if (command[0].equals("nodefence", ignoreCase = true)) {
            player.skills.Reset()
        }
        if (command[0].equals("pos", ignoreCase = true)) {
            ActionSender.sendMessage(player, player.location.toString())
        }
        if (command[0].equals("summon", ignoreCase = true)) {
            if (player.bob == null) {
                player.bob = BeastOfBurden(6873, player)
                player.bob.summon(false)
            } else {
                ActionSender.sendMessage(player, "You cannot summon two familiars at once!")
            }
        }
        if (command[0].equals("titan", ignoreCase = true)) {
            if (player.getAttribute<Any?>("familiar") == null) {
                player.setAttribute("familiar", SteelTitan(player))
            } else {
                ActionSender.sendMessage(player, "You cannot summon two familiars at once!")
            }
        }
        if (command[0].equals("dismiss", ignoreCase = true)) {
            if (player.getAttribute<Any?>("familiar") != null) {
                (player.getAttribute<Any>("familiar") as Familiar).dismiss()
            }
        }
        if (command[0].equals("closeinter", ignoreCase = true)) {
            ActionSender.closeInter(player)
        }
        if (command[0].equals("pricecheck", ignoreCase = true)) {
            val pc = PriceCheck(player)
            pc.addItem(0)
            pc.execute()
        }
        if (command[0].equals("special", ignoreCase = true)) {
            player.setSpecialAmount(1000, true)
        }
        if (command[0].equals("testexamine", ignoreCase = true)) {
            player.sendMessage(ItemDefinition.forId(command[1].toInt()).examine)
        }
        if (command[0].equals("interface", ignoreCase = true)) {
            ActionSender.sendInterface(player, command[1].toInt())
        }
        if (command[0].equals("ic", ignoreCase = true)) {
            ActionSender.sendInterfaceConfig(player, command[1].toInt(), command[2].toInt(), false)
        }
        if (command[0].equals("invis", ignoreCase = true)) {
            player.isHidden = true
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("vis", ignoreCase = true)) {
            player.isHidden = false
            player.mask.isApperanceUpdate = true
        }
        if (command[0].equals("pintest", ignoreCase = true)) {
            player.bank.enterPin()
        }
        if (command[0].equals("shoptest", ignoreCase = true)) {
            if (command.size == 2) {
                player.setAttribute("shopId", command[1].toInt())
                World.getWorld().shopManager.openShop(
                    player,
                    (player.getAttribute<Any>("shopId") as Int)
                )
            }
        }
        if (command[0].equals("hitcap", ignoreCase = true)) {
            if (!(player.username.equals("armo", ignoreCase = true))) {
                player.sendMessage("You need to be the owner to use this command.")
                return
            }
            player.hitCap = true
        }
        if (command[0].equals("npc", ignoreCase = true)) {
            if (!(player.username.equals("armo", ignoreCase = true))) {
                player.sendMessage("You need to be the owner to use this command.")
                return
            }
            World.getWorld().npcs.add(NPC(command[1].toInt(), player.location))
        }
        if (command[0].equals("fd", ignoreCase = true)) {
            val iii = command[1].toInt()
            ActionSender.sendDuelOptions(player, iii)
        }
        if (command[0].equals("findconfig", ignoreCase = true)) {
            if (command.size == 1) {
                World.getWorld().submit(object : Event(1000) {
                    var i: Int = 439
                    override fun run() {
                        if (i != 181 && i != 475) {
                            ActionSender.sendMessage(player, "Testing config: $i")
                            ActionSender.sendConfig(player, i, 1)
                            i++
                        } else {
                            this.stop()
                        }
                    }
                })
            }
        }
        if (command[0].equals("hidden", ignoreCase = true)) {
            if (player.isHidden) {
                player.isHidden = false
                player.sendMessage("You are now visible.")
            } else {
                player.isHidden = true
                player.sendMessage("You are now hidden.")
            }
        }
        if (command[0].equals("ss", ignoreCase = true)) {
            ActionSender.sendString(player, command[1].toInt(), command[2].toInt(), "This is a test.")
        }
        if (command[0].equals("config", ignoreCase = true)) {
            ActionSender.sendConfig(player, command[1].toInt(), command[2].toInt())
        }
        if (command[0].equals("iconfig", ignoreCase = true)) {
            ActionSender.sendInterfaceConfig(player, command[1].toInt(), command[2].toInt(), command[3].toBoolean())
        }
        if (command[0].equals("closeinvent", ignoreCase = true)) {
            ActionSender.closeInterface(player, 548, command[1].toInt())
        }
        if (command[0].equals("update", ignoreCase = true)) {
            if (command[1].toInt() > 0) {
                for (p in World.getWorld().players) {
                    ActionSender.sendSystemUpdate(p, command[1].toInt())
                }
            }
        }
        if (command[0].equals("pfplayer", ignoreCase = true)) {
            World.getWorld().doPath(AStarPathFinder(), player, command[1].toInt(), command[2].toInt())
        }
    }

    fun tele(player: Player, x: Int, y: Int, z: Int) {
        if (player.location.wildernessLevel > 20) {
            player.sendMessage("You can't teleport above level 20 wilderness!")
            return
        }
        if (player.teleblock.isTeleblocked) {
            player.sendMessage("You can't teleport while you are teleblocked!")
            return
        }
        if (player.skills.hitPoints <= 0) {
            return
        }
        player.animate(8939)
        player.graphics(1576)
        World.getWorld().submit(object : Tickable(4) {
            override fun execute() {
                player.teleport(x, y, z)
                player.animate(8941)
                player.graphics(1577)
                this.stop()
            }
        })
    }
}