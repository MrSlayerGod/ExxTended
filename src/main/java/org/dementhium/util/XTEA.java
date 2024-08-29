package org.dementhium.util;

import java.nio.ByteBuffer;

/**
 * @author Lazaro
 */
public class XTEA {
	
    private static final int DELTA = 0x9E3779B9;
    private static final int NUM_ROUNDS = 32;
    private final int[] key;

    public XTEA(int[] key) {
        this.key = key;
    }

    private void decipher(int[] block) {
        long sum = (long) DELTA * NUM_ROUNDS;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[1] -= (((block[0] << 4) ^ (block[0] >> 5)) + block[0]) ^ (sum + key[((int) ((sum >> 11) & 3))]);
            sum -= DELTA;
            block[0] -= (((block[1] << 4) ^ (block[1] >> 5)) + block[0]) ^ (sum + key[((int) (sum & 3))]);
        }
    }

    public byte[] decrypt(byte[] data, int offset, int length) {
        int numBlocks = (length - offset) / 8;
        int[] block = new int[2];
        for (int i = 0; i < numBlocks; i++) {
            block[0] = data[i + offset] >> 24 | data[i + 1 + offset] >> 16 | data[i + 2 + offset] >> 8 | data[i + 3 + offset];
            block[1] = data[i + 4 + offset] >> 24 | data[i + 5 + offset] >> 16 | data[i + 6 + offset] >> 8 | data[i + 7 + offset];
            decipher(block);
            data[i + offset] = (byte) (block[0] >> 24);
            data[i + 1 + offset] = (byte) (block[0] >> 16);
            data[i + 2 + offset] = (byte) (block[0] >> 8);
            data[i + 3 + offset] = (byte) block[0];
            data[i + 4 + offset] = (byte) (block[1] >> 24);
            data[i + 5 + offset] = (byte) (block[1] >> 16);
            data[i + 6 + offset] = (byte) (block[1] >> 8);
            data[i + 7 + offset] = (byte) block[1];
        }
        return data;
    }

    private void encipher(int[] block) {
        long sum = 0;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[0] += (((block[1] << 4) ^ (block[1] >> 5)) + block[1]) ^ (sum + key[((int) (sum & 3))]);
            sum += DELTA;
            block[1] += (((block[0] << 4) ^ (block[0] >> 5)) + block[0]) ^ (sum + key[((int) ((sum >> 11) & 3))]);
        }
    }

    public void encrypt(ByteBuffer buffer) {
        int[] block = new int[2];
        for (int i = 0; i + 8 < buffer.limit(); i += 8) {
            block[0] = buffer.getInt(i);
            block[1] = buffer.getInt(i + 4);
            encipher(block);
            buffer.putInt(i, block[0]).putInt(i + 4, block[1]);
        }
        buffer.rewind();
    }
}
