package org.dementhium.content.clans

import org.dementhium.model.player.Player
import org.dementhium.net.message.Message.PacketType
import org.dementhium.net.message.MessageBuilder
import org.dementhium.util.Misc
import org.dementhium.util.TextUtils
import java.util.*

object ClanMessage {
    var messageCounter: Int = 1
    val random: Random = Random()
    var id: Int = 0

    fun sendClanChatMessage(fromPlayer: Player, toPlayer: Player?, roomName: String?, message: String) {
        val messageCounter = nextUniqueId
        val bldr = MessageBuilder(64, PacketType.VAR_BYTE)
        with(bldr) {
            writeByte(0)
            writeRS2String(Misc.formatPlayerNameForDisplay((fromPlayer.username)))
            writeLong(Misc.stringToLong(roomName))
            writeShort(random.nextInt())
            val bytes = ByteArray(256)
            bytes[0] = message.length.toByte()
            val len = 1 + TextUtils.huffmanCompress(message, bytes, 1)
            writeMediumInt(messageCounter)
            writeByte(fromPlayer.rights.toByte())
            writeBytes(bytes, 0, len)
            if (toPlayer is Player) {
                toPlayer.connection.write(toMessage())
            } else {
                fromPlayer.connection.write(bldr.toMessage())
            }
        }

    }

    val nextUniqueId: Int
        get() {
            if (messageCounter >= 16000000) {
                messageCounter = 0
            }
            return messageCounter++
        }
}