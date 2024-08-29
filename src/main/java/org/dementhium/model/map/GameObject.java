package org.dementhium.model.map;

import org.dementhium.model.Location;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public class GameObject {

	private int id;
	private int type, rotation;
	private Location location;

	public GameObject(int id, int x, int y, int z, int type, int rotation) {
		this.id = id;
		this.location = Location.create(x, y, z);
		this.type = type;
		this.rotation = rotation;
	}

	public GameObject(int id, Location location, int type, int rotation) {
		this.id = id;
		this.location = location;
		this.type = type;
		this.rotation = rotation;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return location.getX();
	}

	public int getY() {
		return location.getY();
	}

	public int getZ() {
		return location.getZ();
	}

	public int getType() {
		return type;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(byte rotation) {
		this.rotation = rotation;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
}
