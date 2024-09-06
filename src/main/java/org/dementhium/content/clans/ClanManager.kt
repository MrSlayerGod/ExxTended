package org.dementhium.content.clans

import org.dementhium.io.XMLHandler
import org.dementhium.model.World
import org.dementhium.model.player.Player
import org.dementhium.net.ActionSender
import org.dementhium.util.Misc
import org.dementhium.util.tickable

class ClanManager {

    val clans: MutableMap<String, Clan> = try {
        XMLHandler.fromXML("data/xml/clans.xml")
    } catch (e: Exception) {
        e.printStackTrace()
        mutableMapOf<String, Clan>()
    }.also { clans ->
        clans.forEach { (clanName, clan) ->
            clan.setTransient()
        }
        println("Loaded " + clans.size + " clans.")
    }

    fun getClan(s: String): Clan = clans[Misc.formatPlayerNameForProtocol(s)] ?: Clan.NO_CLAN

    fun createClan(p: Player, name: String) {
        if (name == "") {
            return
        }
        val username = Misc.formatPlayerNameForProtocol(p.username)
        val clan = clans.computeIfAbsent(username) {
            Clan(username, name)
        }
        refresh(clan)
    }

    fun joinClan(p: Player, user: String) {
        p.sendMessage("Attempting to join channel...")
        val clan = getClan(p.username)
        World.getWorld().submit(
            tickable {
                getClan(p.username).whenInvalidClan {
                    p.sendMessage("The channel you tried to join does not exist.")
                    this.stop()
                }.whenValidClan {
                    if (clan.canJoin(p)) {
                        p.settings.currentClan = clan
                        clan.addMember(p)
                        refresh(clan)
                        ActionSender.sendConfig(p, 1083, if (clan.isLootsharing) 1 else 0)
                        val ccName = Misc.formatPlayerNameForDisplay(clan.name)
                        p.sendMessage("Now talking in the clan channel $ccName")
                        p.sendMessage("To talk, start each line of chat with the / symbol.")
                    } else {
                        p.sendMessage("You don't have a high enough rank to join this clan.")
                    }
                    this.stop()
                }
            }
        )
    }

    fun destroy(player: Player, username: String) = getClan(username).forEachMember { member ->
        ClanPacket.sendClanList(member, Clan.NO_CLAN)
    }.also {
        clans.remove(username)
    }


    fun refresh(clan: Clan) = clan.forEachMember { member ->
        ClanPacket.sendClanList(member, clan)
    }


    fun leaveClan(player: Player) {
        val clan = player.settings.currentClan
        clan.whenValidClan {
            clan.removeMember(player)
            refresh(clan)
            ClanPacket.sendClanList(player, Clan.NO_CLAN)
        }
        ActionSender.sendConfig(player, 1083, 0)
    }

    fun rankMember(player: Player, user: String?, rank: Int) {
        val clan = getClan(player.username)
        if (clan.isInvalidClan()) return
        clan.rankUser(user!!, rank)
        refresh(clan)
    }

    fun getClanName(user: String): String = getClan(user).name

    fun sendClanMessage(player: Player, text: String) {
        val clan = player.settings.currentClan ?: return
        clan.whenValidClan {
            clan.forEachMember { member ->
                ClanMessage.sendClanChatMessage(player, member, clan.name, text)
            }
        }
    }

    fun toggleLootshare(player: Player) {
        val clan = getClan(player.username)
        if (clan.isValidClan()) {
            clan.toggleLootshare()
        } else {
            player.sendMessage("You don't have a clan to active lootshare with.")
        }
    }
}