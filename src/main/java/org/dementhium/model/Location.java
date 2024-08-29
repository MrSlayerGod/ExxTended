package org.dementhium.model;

import java.util.LinkedList;
import java.util.List;

import org.dementhium.model.GroundItemManager.GroundItem;
import org.dementhium.model.map.Directions;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow
 */
public class Location {
	
	public static double distanceFormula(int x, int y, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
	}

	public static Location create(int x, int y, int z) {
		return Region.getLocation(x, y, z);
	}

	public static Location create(int[] coordinates) {
		return create(coordinates[0], coordinates[1], coordinates[2]);
	}

	private List<GroundItem> items;
	private List<NPC> npcs;
	private List<Player> players;
	private final int x, y, z;

	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(Entity entity) {
		if(entity.isPlayer()) {
			getPlayers().add(entity.getPlayer());
		} else if(entity.isNPC()) {
			getNPCs().add(entity.getNpc());
		} else if(entity.isGroundItem()) {
			getItems().add(entity.getGroundItem());
		}
	}

	public boolean containsItems() {
		return items != null && items.size() > 0;
	}

	public boolean containsNPCs() {
		return npcs != null && npcs.size() > 0;
	}
	
	public boolean containsNPCs(Mob exclude) {
		if(npcs == null) {
			return false;
		}
		if(exclude != null) {
			int size = npcs.size();
			if(npcs.contains(exclude)) {
				size--;
			}
			return size > 0;
		}
		return npcs.size() > 0;
	}

	public boolean containsPlayers() {
		return players != null && players.size() > 0;
	}

	public boolean differentMap(Location other) {
		return distanceFormula(getRegionX(), getRegionY(), other.getRegionX(), other.getRegionY()) >= 4;
	}

	public int distance(Location other) {
		return (int) distanceFormula(x, y, other.x, other.y);
	}
	
	public boolean atGe() {
		return x >= 3136 && x <= 3200 && y >= 3399 && y <= 3516;
	}
	
	public boolean atGeLobby() {
		return x >= 3148 && x <= 3181 && y >= 3473 && y <= 3506 || x >= 3264 && x <= 3279 && y >= 3672 && y <= 3695;
	}
	
	public boolean atBountyHunter() {
		return x >= 3081 && x <= 3201 && y >= 3647 && y <= 3773 || x >= 3133 && x <= 3273 && y >= 3382 && y <= 3518;
	}
	
	public boolean atBountyCraters() {
		return x >= 3132 && x <= 3201 && y >= 3647 && y <= 3707;
	}

	public int getWildernessLevel() {
		if(atGe() || atGeLobby() || y >= 3520 && y <= 3524) {
			return 0;
		}
		if (y > 3520 && y < 4000) {
			return (((int) (Math.ceil((y) - 3520D) / 8D) + 1));
		}
		return 0;
	}
	
	public boolean withinDistance(Location other, int dist) {
		if(other.z != z) {
			return false;
		}
		return distance(other) <= dist;
	}
	
	public boolean withinDistance(Location other) {
		if (other.z != z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}
	
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Location) {
			Location other = (Location) object;
			return x == other.x && y == other.y && other.z == z;
		}
		return false;
	}

	public int getItemCount() {
		return items != null ? items.size() : 0;
	}

	public List<GroundItem> getItems() {
		if (items == null) {
			items = new LinkedList<GroundItem>();
		}
		return items;
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalX(Location base) {
		return x - ((base.getRegionX() - 6) << 3);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getLocalY(Location base) {
		return y - ((base.getRegionY() - 6) << 3);
	}

	public int getNPCCount() {
		return npcs != null ? npcs.size() : 0;
	}

	public List<NPC> getNPCs() {
		if (npcs == null) {
			npcs = new LinkedList<NPC>();
		}
		return npcs;
	}

	public int getRegionX() {
		return x >> 3;
	}

	public int getRegionY() {
		return y >> 3;
	}

	public int getPlayerCount() {
		return players != null ? players.size() : 0;
	}

	public List<Player> getPlayers() {
		if (players == null) {
			players = new LinkedList<Player>();
		}
		return players;
	}

	public Location getLocation(Directions.WalkingDirection direction) {
		switch (direction) {
		case SOUTH:
			return Location.create(x, y - 1, z);
		case WEST:
			return Location.create(x - 1, y, z);
		case NORTH:
			return Location.create(x, y + 1, z);
		case EAST:
			return Location.create(x + 1, y, z);
		case SOUTH_WEST:
			return Location.create(x - 1, y - 1, z);
		case NORTH_WEST:
			return Location.create(x - 1, y + 1, z);
		case SOUTH_EAST:
			return Location.create(x + 1, y - 1, z);
		case NORTH_EAST:
			return Location.create(x + 1, y + 1, z);
		default:
			return null;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}

	public void remove(Entity entity) {
		if(entity.isPlayer()) {
			getPlayers().remove(entity.getPlayer());
		} else if(entity.isNPC()) {
			getNPCs().remove(entity.getNpc());
		} else if(entity.isGroundItem()) {
			getItems().remove(entity.getGroundItem());
		}
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "," + z + "] [lX " + getLocalX() + ", lY " + getLocalY() + "] " + "[rX " + getRegionX() + ", rY " + getRegionY() + "]  players= " + getPlayerCount() + " npcs= " + getNPCCount() + " items= " + getItemCount();
	}

	public Location transform(int diffX, int diffY, int diffZ) {
		return Location.create(x + diffX, y + diffY, z + diffZ);
	}

	public boolean withinRange(Location t) {
		return withinRange(t, 15);
	}

	public boolean withinRange(Location t, int distance) {
		return t.z == z && distance(t) <= distance;
	}

	public int getDistance(Location pos) {
		return distance(pos);
	}
	
	public int get12BitsHash() {
		return (0x1f & getLocalY()) | (getZ() << 10) | (0x3e5 & ((getLocalX() << 5)));
	}

	public int get18BitsHash() {
		int regionId = ((getRegionX() / 8) << 8) + (getRegionY() / 8);
		return (((regionId & 0xff) * 64) >> 6) | (getZ() << 16) | ((((regionId >> 8) * 64) >> 6) << 8);
	}

	public int get30BitsHash() {
		return y | z << 28 | x << 14;
	}
}
