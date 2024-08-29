package org.dementhium.cache;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import org.dementhium.net.message.Message;
import org.dementhium.net.message.MessageBuilder;

/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2
 *
 * @credits Lazaro - For the prepareFilePacket method from rt5e
 */
public final class Cache {
	
	/**
	 * The instance of our cache handler
	 */
	public static Cache INSTANCE = new Cache("data/cache/");

	
	private ByteBuffer mainFileBuffer;
	
	private int mainFileLength;
	
	/**
	 * The file store
	 */
	private CacheContainer fileStore;

	/**
	 * An array of containers
	 */
	private CacheContainer[] containers = new CacheContainer[34];

	/**
	 * Construct our new Cache instance
	 * @param directory The directory of our cache
	 */
	public Cache(String directory) {
		try {
			RandomAccessFile mainFileStore = new RandomAccessFile(directory + "main_file_cache.dat2", "rw");

			for(File f : new File(directory).listFiles()) {
				if(!f.isDirectory()) {
					int indexOf = f.getName().indexOf(".idx");
					if(indexOf > -1) {
						int container = Integer.parseInt(f.getName().substring(indexOf + 4));
						if(container == 255) {
							fileStore = new CacheContainer(container, mainFileStore, new RandomAccessFile(directory + "main_file_cache.idx255", "rw"));
						} else {
							containers[container] = new CacheContainer(container, mainFileStore, new RandomAccessFile(directory + "main_file_cache.idx" + container, "rw"));
						}
					}
				}
			}
			for (int i = 0; i < fileStore.length(); i++) {
				containers[i].setFileDescriptor(new RS2FileDescriptorTable(fileStore.getFile(i)));
			}
			int length = (int) fileStore.length();
			int size = length * 74 + 3;
			CRC32 crc = new CRC32();
			ByteBuffer mainFileBuffer = ByteBuffer.allocate(size);
			mainFileBuffer.put((byte) 0).putInt(size - 5).put((byte) length);
			for(int i = 0; i < length; i++) {
				ByteBuffer fileBuffer = fileStore.getFile(i).getData();
				fileBuffer.position(0);
				byte[] fileData = fileBuffer.array();
				crc.update(fileData);
				mainFileBuffer.putInt((int) crc.getValue());
				mainFileBuffer.putInt(containers[i].getFileDescriptor().getRevision());
				mainFileBuffer.put(Whirlpool.whirlpool(fileData, 0, fileData.length));
				crc.reset();
			}
			int bufferPosition = mainFileBuffer.position();
			mainFileBuffer.rewind();
			byte[] mainFileData = new byte[bufferPosition];
			mainFileBuffer.get(mainFileData);
			mainFileBuffer.rewind();
			mainFileBuffer.position(bufferPosition); 
			mainFileBuffer.put((byte) 10);
			mainFileBuffer.put(Whirlpool.whirlpool(mainFileData, 5, mainFileData.length - 5)); //TODO Fix this
			mainFileLength = size - 5;
			this.mainFileBuffer = mainFileBuffer;
			System.out.println("Fully loaded cache.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CacheFile getFile(int container, int file) {
		if(container == 255 && file == 255) {
			mainFileBuffer.rewind();
			return new CacheFile(255, 255, CacheFile.COMPRESSION_NONE, mainFileLength, mainFileBuffer);
		}
		if (container == 255) {
			return fileStore.getFile(file);
		}
		return containers[container].getFile(file);
	}

	public Message generateFile(int container, int file, int opcode) {
		CacheFile cacheFile;
		synchronized(this) {
			cacheFile = getFile(container, file);
		}
		int compression = cacheFile.getCompression();
		int length = cacheFile.getLength();
		int attributes = compression;
		if (opcode == 0) {
			attributes |= 0x80;
		}
		MessageBuilder outBuffer = new MessageBuilder();
		outBuffer.writeByte((byte) container);
		outBuffer.writeShort((short) file);
		outBuffer.writeByte((byte) attributes);
		outBuffer.writeInt(length);
		int realLength = compression != 0 ? length + 4 : length;
		ByteBuffer fileData = cacheFile.getData();
		fileData.position(5);
		for(int offset = 0; offset < realLength; offset++) {
			if (outBuffer.position() % 512 == 0) {
				outBuffer.writeByte((byte) 255);
			}
			outBuffer.writeByte((byte) fileData.get());
		}
		return outBuffer.toMessage();
	}

	public CacheContainer getContainer(int container) {
		return containers[container];
	}
	
	public static void init() {

	}
}
