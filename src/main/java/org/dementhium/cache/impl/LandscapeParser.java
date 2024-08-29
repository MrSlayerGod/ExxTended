package org.dementhium.cache.impl;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.dementhium.cache.Cache;
import org.dementhium.cache.CacheFile;
import org.dementhium.util.BufferUtils;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class LandscapeParser {

	public static void parse(int regionId) throws IOException {
		/*int x = (regionId >> 8) * 64;
		int y = (regionId & 0xff) * 64;
		CacheFile landscapeMap = Cache.INSTANCE.getContainer(5).getFileForName("m" + ((x >> 3) / 8) + "_" + ((y >> 3) / 8));
		CacheFile objectMap = Cache.INSTANCE.getContainer(5).getFileForName("l" + ((x >> 3) / 8) + "_" + ((y >> 3) / 8));
		if (landscapeMap == null && objectMap == null) {
			System.out.println("Map [" + x + ", " + y + "] was not found in the cache!");
			return;
		}
		if (landscapeMap != null) {
			landscapeMap.decompress();
		}
		if (objectMap != null) {
			objectMap.decompress(new int[] {0, 0, 0, 0});
		}
		ByteBuffer str1 = null, str2 = null;
		if (landscapeMap != null && landscapeMap.isDecompressed()) {
			str2 = landscapeMap.getData();
		}
		if (objectMap != null && objectMap.isDecompressed()) {
			str1 = objectMap.getData();
		}
		byte[][][] landscapeData = new byte[4][64][64];
		if (str2 != null) {
			for (int z = 0; z < 4; z++) {
				for (int localX = 0; localX < 64; localX++) {
					for (int localY = 0; localY < 64; localY++) {
						while (true) {
							int v = str2.get() & 0xff;
							if (v == 0) {
								break;
							} else if (v == 1) {
								str2.get();
								break;
							} else if (v <= 49) {
								str2.get();
							} else if (v <= 81) {
								landscapeData[z][localX][localY] = (byte) (v - 49);
							}
						}
					}
				}
			}
			for (int z = 0; z < 4; z++) {
				for (int localX = 0; localX < 64; localX++) {
					for (int localY = 0; localY < 64; localY++) {
						if ((landscapeData[z][localX][localY] & 1) == 1) {
							int height = z;
							if ((landscapeData[1][localX][localY] & 2) == 2) {
								height--;
							}
							if (height >= 0 && height <= 3) {
								//region.addClip(localX, localY, height, 0x200000);
							}
						}
					}
				}
			}
		}
		if (str1 != null) {
			int objectId = -1;
			int incr;
			while ((incr = BufferUtils.readSmart2(str1)) != 0) {
				objectId += incr;
				int location = 0;
				int incr2;
				while ((incr2 = BufferUtils.readSmart(str1)) != 0) {
					location += incr2 - 1;
					int localX = (location >> 6 & 0x3f), localY = (location & 0x3f);
					@SuppressWarnings("unused")
					int height = location >> 12, objectData = str1.get() & 0xff, type = objectData >> 2, direction = objectData & 0x3;
					if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
						continue;
					}
					if ((landscapeData[1][localX][localY] & 2) == 2) {
						height--;
					}
					if (height >= 0 && height <= 3) {
						//region.addObject(objectId, localX, localY, height, type, direction, true);
						// Region.addObject(objectId, Tile.locate(absX + localX, absY + localY, height), type, direction, true);
					}
				}
			}
		}*/
	}

}
