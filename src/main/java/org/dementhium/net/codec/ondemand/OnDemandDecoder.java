package org.dementhium.net.codec.ondemand;

import org.dementhium.cache.Cache;
import org.dementhium.model.World;
import org.dementhium.net.message.Message;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public final class OnDemandDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(buffer.readableBytes() >= 4) {
			int priority = buffer.readByte() & 0xFF;
			int container = buffer.readByte() & 0xFF;
			int file = buffer.readShort() & 0xFFFF;
			switch(priority) {
			case 0:
			case 1:
				passToWorker(container, file, priority, channel);
				break;
			}
		}
		return null;
	}

	public void passToWorker(final int container, final int file, final int priority, final Channel channel) {
		World.getWorld().getExecutor().submitJs5(new Runnable() {
			@Override
			public void run() {
				Message response = Cache.INSTANCE.generateFile(container, file, priority);
				if(response != null) {
					channel.write(response);
				}
			}
		});
	}


}
