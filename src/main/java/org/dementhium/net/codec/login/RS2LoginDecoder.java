package org.dementhium.net.codec.login;

import java.io.IOException;

import org.dementhium.model.World;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.net.GameSession;
import org.dementhium.net.codec.DefaultGameDecoder;
import org.dementhium.util.BufferUtils;
import org.dementhium.util.Constants;
import org.dementhium.util.Misc;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public final class RS2LoginDecoder extends ReplayingDecoder<LoginState> {

	public RS2LoginDecoder() {
		checkpoint(LoginState.PRE_STAGE);
	}

	/*private int nameHash;

	private long serverKey;*/ //TODO

	private GameSession session;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, LoginState state) throws Exception {
		if(state == LoginState.LOBBY_FINALIZATION || state == LoginState.LOGIN_FINALIZATION) {
			session = new GameSession(channel);
		}
		switch(state) {
		/*case READ_NAME_HASH:
			nameHash = buffer.readByte() & 0xFF;
			System.out.println(nameHash);
			serverKey = serverKeyGenerator.nextLong();
			MessageBuilder response = new MessageBuilder();
			response.writeByte((byte) 0);
			response.writeLong(serverKey);
			channel.write(response);
			checkpoint(LoginState.PRE_STAGE);
			break;*/
			case PRE_STAGE:
				if(readableBytes(buffer) >= 3) {
					int loginType = buffer.readByte() & 0xFF;
					int loginPacketSize = buffer.readShort() & 0xFFFF;
					if (loginPacketSize != readableBytes(buffer)) {
						throw new IOException("Mismatched login packet INVENTORY_SIZE.");
					}
					int clientVersion = buffer.readInt();
					System.out.println("cli ver: "+clientVersion);
					if (false && clientVersion != Constants.REVISION) {
						System.out.println("ClientVer != Revision -> "+clientVersion+" vs "+Constants.REVISION);
						throw new IOException("Incorrect revision read");
					}
					if(loginType == 16 || loginType == 18) {
						checkpoint(LoginState.LOGIN_FINALIZATION);
					} else if(loginType == 19) {
						checkpoint(LoginState.LOBBY_FINALIZATION);
					} else {
						System.out.println("to VALHALLA");
						throw new IOException("Incorrect login type");
					}
				}
				break;
			case LOBBY_FINALIZATION:
				if(buffer.readable()) {
					buffer.readByte();
					buffer.readByte(); // display
					for (int i = 0; i < 24; i++) {
						buffer.readByte();
					}
					BufferUtils.readRS2String(buffer); // settings
					buffer.readInt();
					for (int i = 0; i < 34; i++) {
						buffer.readInt();
					}
					buffer.readShort();
					int rsaHeader = buffer.readByte();
					if (rsaHeader != 11) {
						throw new IOException("Invalid RSA header.");
					}
					buffer.readLong();
					buffer.readLong();
					String name = Misc.longToString(buffer.readLong());
					String password = BufferUtils.readRS2String(buffer);
					buffer.skipBytes(readableBytes(buffer));
					session.setInLobby(true);

					World.getWorld().load(session, new PlayerDefinition(name, password));
					ctx.getPipeline().replace("decoder", "decoder", new DefaultGameDecoder(session));
					System.out.println("LOBBY FINALIZED");
				}
				break;
			case LOGIN_FINALIZATION:
				System.out.println("LOGIN FINALIZATION START");
				if(buffer.readable()) {
					buffer.readUnsignedByte();
					session.setDisplayMode(buffer.readUnsignedByte());
					buffer.readUnsignedShort();
					buffer.readUnsignedShort();
					buffer.readUnsignedByte();
					buffer.skipBytes(24);
					BufferUtils.readRS2String(buffer);
					buffer.readInt();
					int size = buffer.readUnsignedByte();
					buffer.skipBytes(size);
					buffer.skipBytes(6 + (33 * 4) + 8 + 2 + 14);
					if(buffer.readUnsignedByte() != 11) {
						throw new IOException("Invalid RSA header.");
					}
					buffer.readLong();
					buffer.readLong();
					long l = buffer.readLong();
					String name = Misc.formatPlayerNameForProtocol(Misc.longToString(l));
					String password = BufferUtils.readRS2String(buffer);

					session.setInLobby(false);
					World.getWorld().load(session, new PlayerDefinition(name, password));
					System.out.println("FINALIZATION FINALIZED");
					ctx.getPipeline().replace("decoder", "decoder", new DefaultGameDecoder(session));
				}
				break;
		}

		return null;
	}

	public int readableBytes(ChannelBuffer buffer) {
		return buffer.writerIndex() - buffer.readerIndex();
	}

}
