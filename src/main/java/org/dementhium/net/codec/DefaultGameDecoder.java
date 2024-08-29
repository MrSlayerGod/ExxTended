package org.dementhium.net.codec;

import org.dementhium.net.GameSession;
import org.dementhium.net.message.Message;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.util.Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class DefaultGameDecoder extends FrameDecoder {

	private final GameSession session;

	private boolean playerExists;

	public DefaultGameDecoder(GameSession session) {
		super(true);
		this.session = session;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(buffer.readable()) {
			int opcode = buffer.readUnsignedByte();
			int length = Constants.PACKET_SIZES[opcode];
			if (opcode < 0 || opcode > 83) {
				buffer.discardReadBytes();
				return null;
			}
			if (length == -1) {
				if(buffer.readable()) {
					length = buffer.readUnsignedByte();
				}
			}
			if(length <= buffer.readableBytes() && length > 0) {
				byte[] payload = new byte[length];
				buffer.readBytes(payload, 0, length);
				Message message = new Message(opcode, PacketType.STANDARD, ChannelBuffers.wrappedBuffer(payload));
				if(!playerExists) {
					playerExists = true;
					return new Object[] {session, message};
				}
				return message;
			}
		}
		return null;
	}

}
