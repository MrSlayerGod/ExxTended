package org.dementhium.content.clans

import org.dementhium.content.clans.Clan.Companion.NO_CLAN
import org.dementhium.model.player.Player
import org.dementhium.net.ActionSender
import org.dementhium.util.Misc

class Clan @JvmOverloads constructor (val owner: String = "", var name: String = "") {
    var joinReq: Int = 0
    var talkReq: Int = 0
    var kickReq: Int = 7
        private set
    var ranks = mutableMapOf<String, Byte>()

    @Transient
    private var members = mutableListOf<Player>()

    @Transient
    var isLootsharing: Boolean = false

    init {
        setTransient()
    }

    fun forEachMember(block: (Player) -> Unit) = whenValidClan { members.forEach(block) }

    fun setTransient() {
        isLootsharing = false
        if (kickReq == 0) {
            kickReq = 7
        }
    }

    fun rankUser(name: String, rank: Int) {
        ranks.putIfAbsent(name, rank.toByte())
    }

    fun getRank(player: Player): Byte {
        if (isInvalidClan()) return -1
        if (Misc.formatPlayerNameForProtocol(player.username) == owner || player.rights == 2) {
            return 7
        }
        return ranks[player.username] ?: 0
    }

    fun canJoin(player: Player): Boolean {
        if (isInvalidClan()) return false
        return getRank(player) >= joinReq
    }

    fun canTalk(player: Player): Boolean {
        if (isInvalidClan()) return false
        return getRank(player) >= talkReq
    }

    fun toggleLootshare() {

        if (isInvalidClan()) return

        isLootsharing = !isLootsharing

        val message = buildString {
            append("Lootshare has been ")
            append(if (isLootsharing) "en" else "dis")
            append("ed.")
        }

        forEachMember { member ->
            ActionSender.sendMessage(member, message)
            ActionSender.sendConfig(member, 1083, if (isLootsharing) 1 else 0)
        }
    }

    fun addMember(member: Player) {
        if (isInvalidClan()) return
        members.add(member)
    }

    fun getMembers(): List<Player> {
        if (isInvalidClan()) return emptyList()
        return members
    }

    fun removeMember(player: Player) {
        if (isInvalidClan()) return
        members.remove(player)
    }

    companion object {
        val NO_CLAN = Clan("", "Chat disabled")
    }
}



fun Clan?.isValidClan() = this != null && this != NO_CLAN

fun Clan?.isInvalidClan() = this == null || this == NO_CLAN

fun Clan?.whenValidClan(block: (Clan) -> Unit) = apply {
    if (isValidClan()) block(this as Clan)
}

fun Clan?.whenInvalidClan(block: (Clan?) -> Unit)= apply {
    if (isInvalidClan()) block(this)
}