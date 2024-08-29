package org.dementhium.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.dementhium.event.Tickable;
import org.dementhium.util.Constants;
import org.dementhium.model.World;

/**
 * 
 * @author `Discardedx2
 * @author 'Mystic Flow
 */
public class ChannelBinder {

	private static final ExecutorService service = Executors.newCachedThreadPool();

	public static void bind(int port) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.setFactory(new NioServerSocketChannelFactory(service, service));
		bootstrap.setPipelineFactory(new DementhiumPipelineFactory());
		bootstrap.bind(new InetSocketAddress(port));
		System.out.println("Server bound to port - "+port);
		/*World.getWorld().submit(new Tickable(30) {
			public void execute() {
				Constants.getPlayersOnline().offline();
				Constants.getPlayersOnline().online();
			}
		});*/
	}

}
