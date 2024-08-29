package org.dementhium.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapData {

	private final static Map<Short, int[]> mapData = new HashMap<Short, int[]>();

	public MapData() {
		final File packedFile = new File("./mapdata/packed.dat");
		if (!packedFile.exists())
			pack();
		else
			load();
	}

	public static void load() {
		try {
			final DataInputStream in = new DataInputStream(new FileInputStream(
					"./mapdata/packed.dat"));
			while (in.available() != 0) {
				final short area = in.readShort();
				final int[] parts = new int[4];
				for (int j = 0; j < 4; j++) {
					parts[j] = in.readInt();
				}
				getMapData().put(area, parts);
			}
		} catch (IOException e) {
			final File Failedpacked = new File("./mapdata/packed.dat");
			if (Failedpacked.exists())
				Failedpacked.delete();
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void pack() {
		System.out.println("Packing mapdata...");
		try {
			final DataOutputStream out = new DataOutputStream(
					new FileOutputStream("./mapdata/packed.dat"));
			final File unpacked = new File("./mapdata/unpacked/");
			final File[] Data = unpacked.listFiles();
			for (File region : Data) {
				final String name = region.getName();
				if (!name.contains(".txt"))
					continue;
				final short regionId = Short.parseShort(name
						.replace(".txt", ""));
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] Key = new int[4];
				for (int j = 0; j < 4; j++) {
					Key[j] = Integer.parseInt(in.readLine());
					out.writeInt(Key[j]);
				}
				getMapData().put(regionId, Key);
				in.close();
			}
			out.flush();
			out.close();
			System.out.println("Complete.");
		} catch (IOException e) {
			final File Failedpacked = new File("./mapdata/packed.dat");
			if (Failedpacked.exists())
				Failedpacked.delete();
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Map<Short, int[]> getMapData() {
		return mapData;
	}

}
