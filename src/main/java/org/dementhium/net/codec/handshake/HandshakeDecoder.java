package org.dementhium.net.codec.handshake;

import java.security.SecureRandom;

import org.dementhium.net.codec.login.RS2LoginDecoder;
import org.dementhium.net.codec.ondemand.OnDemandDecoder;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.util.Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public final class HandshakeDecoder extends FrameDecoder {

    private static final SecureRandom serverKeyGenerator = new SecureRandom();

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if(ctx.getPipeline().get(HandshakeDecoder.class) != null) {
            ctx.getPipeline().remove(this);
        }
        int opcode = buffer.readByte() & 0xFF;
        MessageBuilder response = new MessageBuilder();
        System.out.println(channel.getRemoteAddress());
        switch(opcode) {
            case HandshakeConstants.JS5_REQUEST:
                int version = buffer.readInt();
                System.out.println("CLIENT JS5 VER -> "+version+" vs SERVER JS5 VER -> "+Constants.REVISION);
                if(version != Constants.REVISION) {
                    response.writeByte((byte) 6);
                } else {
                    System.out.println("Passed version inspection");
                    response.writeByte((byte) 0);
                    ctx.getPipeline().addBefore("encoder", "decoder", new OnDemandDecoder());
                }
                break;

            case HandshakeConstants.LOGIN_REQUEST:
                buffer.readByte(); //name hash
                ctx.getPipeline().addBefore("encoder", "decoder", new RS2LoginDecoder());
                response.writeByte((byte) 0);
                response.writeLong(serverKeyGenerator.nextLong()); //TODO Shouldn't be here.
                break;
        }
        return new HandshakeMessage(opcode, response.toMessage());
    }

}
