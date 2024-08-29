package org.dementhium.net;

import java.io.IOException;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.codec.handshake.HandshakeMessage;
import org.dementhium.net.message.Message;
import org.dementhium.task.impl.MessageExecutionTask;
import org.dementhium.task.impl.MessageReceivedTask;
import org.dementhium.task.impl.SessionLogoutTask;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public final class DementhiumHandler extends SimpleChannelHandler {

	private GameSession session;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {

	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (session != null) {
			Player player = session.getPlayer();
			if(player != null) {
				if(player.getCombatState().getLastAttacker() == null && player.getSkills().getHitPoints() > 0) {
					World.getWorld().submit(new SessionLogoutTask(player));
				} else {
					player.logoutDelay = 60;
				}
			}
			session = null;
		}
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		if(e.getCause() instanceof IOException) {
			if(ctx.getChannel().isConnected()) {
				ctx.getChannel().close();
			}
		} else {
			e.getCause().printStackTrace();
		}
	}

	@Override
	public final void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Object message = e.getMessage();
		Channel channel = e.getChannel();
		if(!channel.isConnected()) {
			return;
		}
		if(message instanceof Message) {
			World.getWorld().submit(new MessageReceivedTask(session.getPlayer(), new MessageExecutionTask(session.getPlayer(), (Message) message)));
		} else if(message instanceof GameSession) {
			session = (GameSession) message;
		} else if(message instanceof HandshakeMessage) {
			HandshakeMessage handshakeMessage = (HandshakeMessage) message;
			channel.write(handshakeMessage.getMessage());
		}
	}

}
