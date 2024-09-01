package org.dementhium.content.clans

import org.dementhium.model.player.Player
import org.dementhium.net.message.Message.PacketType
import org.dementhium.net.message.MessageBuilder
import org.dementhium.util.Misc

/**
 * @author 'Mystic Flow
 */
object ClanPacket {
    fun sendClanList(toPlayer: Player, clan: Clan) = with(MessageBuilder(23, PacketType.VAR_SHORT)) {
        clan.whenValidClan {
            writeRS2String(Misc.formatPlayerNameForDisplay(clan.owner))
            writeByte(0)
            writeLong(Misc.stringToLong(clan.name))
            writeByte(clan.kickReq)
            writeByte(clan.getMembers().size)
            for (clanMember in clan.getMembers()) {
                writeRS2String(Misc.formatPlayerNameForDisplay(clanMember.username))
                writeByte(if (toPlayer.connection.isInLobby) 1 else 0) // Need to figure this out
                if (toPlayer.connection.isInLobby) {
                    writeRS2String(if (clanMember.connection.isInLobby) "Lobby" else "ExemptionX")
                }
                writeShort(2) // Status
                writeByte(clan.getRank(clanMember))
                writeRS2String(if (clanMember.connection.isInLobby) "Lobby" else "<col=00ff00>ExemptionX")
            }
        }
        toPlayer.connection.write(toMessage())
    }
}
