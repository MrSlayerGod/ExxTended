package org.dementhium.util;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * This is for streaming or whatever
 * 
 * @author 'Mystic Flow
 *
 */
public class BufferUtils {

	public static void writeRS2String(ChannelBuffer buffer, String string) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte((byte) 0);
	}

	public static void writeRS2String(ByteBuffer buffer, String string) {
		buffer.put(string.getBytes());
		buffer.put((byte) 0);
	}

	public static String readRS2String(ChannelBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while(buffer.readable() && (b = buffer.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}

	public static String readRS2String(ByteBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while(buffer.remaining() > 0 && (b = buffer.get()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}

    public static int readSmart(ByteBuffer buffer) {
        int value = buffer.get() & 0xff;
        if (value < 128)
            return value;
        int value2 = buffer.get() & 0xff;
        return (value << 8 | value2) - 32768;
    }

    public static int readSmart2(ByteBuffer buffer) {
        int value = 0;
        int i;
        for (i = readSmart(buffer); i == 32767; i = readSmart(buffer))
            value += 32767;
        value += i;
        return value;
    }

}
