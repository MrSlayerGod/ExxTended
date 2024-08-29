package org.dementhium.cache;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This distributes all cache data. Named 'filestore' in Method's cache downloader
 *
 * @author 'Mystic Flow 
 * @author `Discardedx2
 */
public class CacheContainer {

	public static final int COMPRESSION_NONE = 0, COMPRESSION_GZIP = 2, COMPRESSION_BZIP2 = 1;

	/**
	 * The container of our file
	 */
	private final int container;

	/**
	 * The input file
	 */
	private final RandomAccessFile containerFile;

	/**
	 * The file store
	 */
	private final RandomAccessFile mainFileStore;

	/**
	 * Our file descriptor table
	 */
	private RS2FileDescriptorTable fileDescriptor;

	/**
	 * The constructor for our CacheContainer
	 * @param container The container of our file 
	 * @param randomAccessFile The RandomAccessFile we use for reading data off the cache
	 */
	public CacheContainer(int container, RandomAccessFile mainFileStore, RandomAccessFile containerFile) {
		this.container = container;
		this.containerFile = containerFile;
		this.mainFileStore = mainFileStore;
	}

	/**
	 * Creates a new allocated buffer
	 * @param file The file we request
	 * @return The file buffer
	 */
	public CacheFile getFile(int file) {
		try {
			ByteBuffer fileBuffer = containerFile.getChannel().map(FileChannel.MapMode.READ_ONLY, file * 6, 6);
			int length = ((fileBuffer.get() & 0xFF) << 16) | ((fileBuffer.get() & 0xFF) << 8) | (fileBuffer.get() & 0xFF);
			int position = ((fileBuffer.get() & 0xFF) << 16) | ((fileBuffer.get() & 0xFF) << 8) | (fileBuffer.get() & 0xFF);
			if (length == 0) {
				throw new IOException("Empty file " + file);
			}
			ByteBuffer buffer = ByteBuffer.allocate(length);
			int remaining = length;
			int offset = 0;
			while(remaining > 0) {
				int amount = remaining;
				if (amount > 512) {
					amount = 512;
				}
				ByteBuffer mainBuffer = mainFileStore.getChannel().map(FileChannel.MapMode.READ_ONLY, position * 520, remaining + 8);
				int expectedFile = mainBuffer.getShort() & 0xFFFF;
				int expectedOffset = mainBuffer.getShort() & 0xFFFF;
				position = ((mainBuffer.get() & 0xFF) << 16) | ((mainBuffer.get() & 0xFF) << 8) | (mainBuffer.get() & 0xFF);
				int expectedContainer = mainBuffer.get() & 0xff;
				if(expectedFile != file) {
					throw new IOException("Unexpected file!");
				}
				if(expectedOffset != offset) {
					throw new IOException("Unexpected offset!");
				}
				if(container != expectedContainer) {
					throw new IOException("Unexpected container!");
				}
				byte[] fileData = new byte[amount];
				mainBuffer.get(fileData, 0, amount);
				buffer.put(fileData);
				remaining -= amount;
				offset++;
			}
			buffer.flip();
			int compression = buffer.get() & 0xff;
			int fileLength = buffer.getInt();
			return new CacheFile(container, file, compression, fileLength, buffer);
		} catch(Exception e) {
			return null;
		}
	}


	/**
	 * The length our file
	 * @return The length of our file
	 * @throws IOException If an I/O error occurs
	 */
	public long length() throws IOException {
		try {
			return containerFile.length() / 6;
		} catch (IOException e) {
			throw e;
		}
	}

	public CacheFile getFileForName(String fileName) {
		if (fileDescriptor == null) {
			return null;
		}
		RS2FileDescriptor entry = fileDescriptor.getDescriptorMap().get(RS2FileDescriptorTable.hashName(fileName));
		if (entry == null) {
			return null;
		}
		return getFile(entry.getId());
	}

	public void setFileDescriptor(RS2FileDescriptorTable fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
	}

	public RS2FileDescriptorTable getFileDescriptor() {
		return fileDescriptor;
	}


}
