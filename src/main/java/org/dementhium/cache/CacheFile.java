package org.dementhium.cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

import org.dementhium.cache.bzip.CBZip2InputStream;

/**
 *
 * @author Defyboy
 */
public class CacheFile {

	public static final int COMPRESSION_NONE = 0, COMPRESSION_GZIP = 2, COMPRESSION_BZIP2 = 1;

	private int compression;

	private int length;

	private ByteBuffer data;

	private int container;

	private int fileId;

	private boolean decompressed;

	private byte[][] subFiles = null;

	public CacheFile(int container, int fileId, int compression, int length, ByteBuffer data) {
		this.container = container;
		this.fileId = fileId;
		this.compression = compression;
		this.length = length;
		this.data = data;
	}

	public int getCompression() {
		return compression;
	}

	public int getLength() {
		return length;
	}

	public ByteBuffer getData() {
		return data;
	}

	public int getContainer() {
		return container;
	}

	public int getFileId() {
		return fileId;
	}

	public void decompress() throws IOException {
		decompress(null);
	}

	public void decompress(int[] key) throws IOException {
		data.rewind();
        int compression = data.get() & 0xff;
        int length = data.getInt();
        int decompressedLength = compression != COMPRESSION_NONE ? data.getInt() : length;
        byte[] out = new byte[decompressedLength];
        byte[] inputData = new byte[length];
        data.get(inputData);
        InputStream input = new ByteArrayInputStream(inputData);
        switch (compression) {
            case COMPRESSION_GZIP:
                input = new GZIPInputStream(input);
                break;
            case COMPRESSION_BZIP2:
                input = new CBZip2InputStream(input);
                break;
        }
        for (int i = 0; i < decompressedLength; i++) {
            int val = input.read();
            if (val == -1) {
                throw new IOException("EOF");
            }
            out[i] = (byte) val;
        }
        data = ByteBuffer.wrap(out);
        data.rewind();
		decompressed = true;
	}

	public boolean isDecompressed() {
		return decompressed;
	}

	public byte[] getBytes() {
		return getBytes(!decompressed ? (compression != COMPRESSION_NONE ? 9 : 5) : 0);
	}

	public byte[] getBytes(int offset) {
		data.position(offset);
		byte[] array = new byte[data.limit() - offset];
		data.get(array);
		data.rewind();
		return array;
	}

	public byte[][] getSubFiles() {
		if (subFiles != null) {
			return subFiles;
		}
		int subFileCount = Cache.INSTANCE.getContainer(container).getFileDescriptor().getEntries()[fileId].getSubFiles().length;
		if (subFileCount == 0) {
			throw new UnsupportedOperationException("This file does not contain any sub files!");
		}
		byte[] dataArray = getBytes();
		int length = dataArray.length;
		int i_19_ = dataArray[--length] & 0xff;
		length -= i_19_ * (subFileCount * 4);
		data.position(length);
		int[] subWritePos = new int[subFileCount];
		for (int i_21_ = 0; i_21_ < i_19_; i_21_++) {
			int i_22_ = 0;
			for (int subIndex = 0; subIndex < subFileCount; subIndex++) {
				i_22_ += data.getInt();
				subWritePos[subIndex] += i_22_;
			}
		}
		
		byte[][] subData = new byte[subFileCount][];
		for (int subIndex = 0; subIndex < subFileCount; subIndex++) {
			subData[subIndex] = new byte[subWritePos[subIndex]];
			subWritePos[subIndex] = 0;
		}
		int readPos = 0;
		data.position(length);
		for (int i_27_ = 0; i_27_ < i_19_; i_27_++) {
			int i_28_ = 0;
			for (int subId = 0; subId < subFileCount; subId++) {
				i_28_ += data.getInt();
				System.arraycopy(dataArray, readPos, subData[subId], subWritePos[subId], i_28_);
				readPos += i_28_;
				subWritePos[subId] += i_28_;
			}
		}
		subFiles = subData;
		return subData;
	}

}
