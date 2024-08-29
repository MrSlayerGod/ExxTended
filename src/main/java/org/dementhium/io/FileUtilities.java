package org.dementhium.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
/**
 * 
 * @author 'Mystic Flow
 *
 */
public class FileUtilities {

	public static final int BUFFER = 1024;

	public boolean exists(String name) {
		File file = new File(name);
		try {
			return file.exists();
		} finally {
			file = null;
		}
	}

	public ByteBuffer fileBuffer(String name) throws IOException {
		FileInputStream in = new FileInputStream(name);
		byte[] data = new byte[BUFFER];
		int read;
		ByteBuffer buffer = ByteBuffer.allocate(in.available() + 1);
		while((read = in.read(data, 0, BUFFER)) != -1) {
			buffer.put(data, 0, read);
		}
		buffer.flip();
		in.close();
		in = null;
		return buffer;
	}

	public void writeBufferToFile(String name, ByteBuffer buffer) throws IOException {
		FileOutputStream out = new FileOutputStream(name);
		out.write(buffer.array(), 0, buffer.remaining());
		out.flush();
		out.close();
	}

}
