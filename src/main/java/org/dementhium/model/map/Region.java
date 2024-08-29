package org.dementhium.model.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.dementhium.model.Location;
import org.dementhium.model.GroundItemManager.GroundItem;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

public class Region {

	public static final int REGION_SIZE = 64;

	private static Map<Integer, Region> regionCache = new HashMap<Integer, Region>();

	public static List<GroundItem> getLocalItems(Location tile) {
		return getLocalItems(tile, 32);
	}

	public static List<GroundItem> getLocalItems(Location tile, int depth) {
		Location[] regionTiles = getRegionTiles(tile, depth);
		List<GroundItem> items = new ArrayList<GroundItem>();
		for(Location t : regionTiles) {
			if(t.containsItems()) {
				for(GroundItem item : t.getItems()) {
					items.add(item);
				}
			}
		}
		return items;
	}

	public static List<NPC> getLocalNPCs(Location tile) {
		return getLocalNPCs(tile, 16);
	}

	public static List<NPC> getLocalNPCs(Location tile, int depth) {
		Location[] regionTiles = getRegionTiles(tile, depth);
		List<NPC> npcs = new ArrayList<NPC>();
		for(Location t : regionTiles) {
			if(t.containsNPCs()) {
				for(NPC npc : t.getNPCs()) {
					npcs.add(npc);
				}
			}
		}
		return npcs;
	}

	public static List<Player> getLocalPlayers(Location tile) {
		return getLocalPlayers(tile, 16);
	}

	public static List<Player> getLocalPlayers(Location tile, int depth) {
		Location[] regionTiles = getRegionTiles(tile, depth);
		List<Player> players = new ArrayList<Player>();
		try {
		for(Location t : regionTiles) {
			if(t.containsPlayers()) {
				for(Player player : t.getPlayers()) {
					players.add(player);
				}
			}
		}
		} catch (Exception e) {
		}
		return players;
	}

	public static Location[] getRegionTiles(Location tile, int depth) {
		int baseX = tile.getX();
		int baseY = tile.getY();
		int z = tile.getZ();

		List<Location> regionTiles = new ArrayList<Location>(depth * 2);
		for (int x = -depth + 1; x < depth; x++) {
			for (int y = -depth + 1; y < depth; y++) {
				regionTiles.add(Location.create(baseX + x, baseY + y, z));
			}
		}
		return regionTiles.toArray(new Location[0]);
	}

	private static void clipSolid(int x, int y, int z) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 6) << 6);
		int localY = y - ((y >> 6) << 6);
		region.clippingMasks[z][localX][localY] = 1073873152;
	}

	public static void addClipping(int x, int y, int z, int shift) {
		int hash = (x / REGION_SIZE) << 8 | (y / REGION_SIZE);
		Region region = regionCache.get(hash);
		if(region != null) {
			int localX = x - ((x >> 6) << 6);
			int localY = y - ((y >> 6) << 6);
			region.clippingMasks[z][localX][localY] |= shift;
		}
	}

	public static Region forCoords(int x, int y) {
		// x = 45 y = 81 For NEX
		x /= REGION_SIZE;
		y /= REGION_SIZE;
		int hash = x << 8 | y;
		Region region = regionCache.get(hash);
		if (region == null) {
			region = new Region(x, y, REGION_SIZE);
			region.load();
			regionCache.put(hash, region);
		}
		return region;
	}

	public static Region forLocation(Location other) {
		return forCoords(other.getX(), other.getY());
	}

	public static int getClippingMask(int x, int y, int z) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 6) << 6);
		int localY = y - ((y >> 6) << 6);
		return region.clippingMasks[z][localX][localY];
	}

	public static Region getRegion(int x, int y, int z) {
		return forCoords(x, y);
	}

	public static Location getLocation(int x, int y, int z) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 6) << 6);
		int localY = y - ((y >> 6) << 6);
		return region.getLocalLocation(localX, localY, z);
	}

	public static void preLoad() {
		/*
		 * Varrock west bank
		 */
		clipSolid(3181, 3444, 0);
		clipSolid(3181, 3442, 0);
		clipSolid(3181, 3440, 0);
		clipSolid(3181, 3438, 0);
		clipSolid(3181, 3436, 0);
		clipSolid(3190, 3443, 0);
		clipSolid(3190, 3441, 0);
		clipSolid(3190, 3439, 0);
		clipSolid(3190, 3437, 0);
		clipSolid(3190, 3435, 0);
		/*
		 * Varrock east bank
		 */
		clipSolid(3251, 3419, 0);
		clipSolid(3252, 3419, 0);
		clipSolid(3253, 3419, 0);
		clipSolid(3254, 3419, 0);
		clipSolid(3255, 3419, 0);
		clipSolid(3256, 3419, 0);
		/*
		 * Edgeville bank
		 */
		clipSolid(3097, 3495, 0);
		clipSolid(3095, 3493, 0);
		clipSolid(3095, 3491, 0);
		clipSolid(3095, 3489, 0);
	}

	private int[][][] clippingMasks;
	private int size;
	private Location[][][] tiles;
	private int x;
	private int y;

	public Region(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		clippingMasks = new int[4][size][size];
		tiles = new Location[4][size][size];
	}

	public Location getBaseLocation(int z) {
		return Location.create(((x) - 1) << 6, ((y) - 1) << 6, z);
	}

	public int[][][] getClippingMasks() {
		return clippingMasks;
	}

	public Location getLocalLocation(int x, int y, int z) {
		Location tile = tiles[z][x][y];
		if (tile == null) {
			tile = new Location(this.x << 6 | x, this.y << 6 | y, z);
			tiles[z][x][y] = tile;
		}
		return tile;
	}

	public int getSize() {
		return size;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x << 8 | y;
	}

	public int getClipping(int x, int y, int z) {
		x = x - ((x >> 6) << 6);
		y = y - ((y >> 6) << 6);
		return clippingMasks[z][x][y];
	}

	private void load() {
		try {
			File file = new File("data/objects/maps/", "m" + x + "_" + y + ".dat.gz");
			if (!file.exists()) {
				System.out.println("Could not find map [m" + x + "_" + y + "]");
				return;
			}
			InputStream in = new GZIPInputStream(new FileInputStream(file));
			while (true) {
				try {
					int localX = read(in);
					int localY = read(in);
					int z = read(in);
					clippingMasks[z][localX][localY] = ((read(in) & 0xff) << 24) | ((read(in) & 0xff) << 16) | ((read(in) & 0xff) << 8) | (read(in) & 0xff);
				} catch (IOException e) {
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error load map [x=" + x + ", y=" + y + "]");
		}
	}

	private byte read(InputStream in) throws IOException {
		int val = in.read();
		if (val == -1) {
			throw new IOException();
		}
		return (byte) val;
	}
}
