package org.dementhium.content.areas.impl;

import java.util.ArrayList;

import org.dementhium.content.areas.Area;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;

/**
 * Represents an area in the shape of a rectangle.
 * 
 * @author Stephen
 */
public class RectangularArea extends Area {

	public RectangularArea() {

	} 

	public RectangularArea(String name, int[] coords, int radius, int centerX, int centerY, boolean isPvpZone,
			boolean isPlusOneZone, boolean canTele) {
		super(name, coords, radius, centerX, centerY, isPvpZone, isPlusOneZone, canTele);
	}

	/**
	 * This should definitely work.
	 */
	@Override
	public boolean contains(Location pos) {
		return pos.getX() >= swX && pos.getX() <= nwX && pos.getY() >= swY
				&& pos.getY() <= nwY;
	}

	public void spawnItems() {
		System.out.println("spawning stuff");
		width = nwX - swX;
		length = nwY - swY;
		System.out.println(width);
		System.out.println(length);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < length; y++) {
				World.getWorld().getGroundItemManager().sendGlobalGroundItem(World.getWorld().getGroundItemManager().create(null, new Item(995, 100000), Location.create(swX + x, swY + y, 0)), false);
			}
		}
	}
	
	public ArrayList<Location> generatePointsWithin() {
		ArrayList<Location> locations = new ArrayList<Location>();
		for (int x = 0; x < nwX - swX; x++) {
			for (int y = 0; y < nwY - swY; y++) {
				locations.add(Location.create(swX + x, swY + y, 0));
			}
		}
		return locations;
	}
	
	public static RectangularArea create(String name, int x, int y, int x1, int y1, boolean isPvp, boolean isPlusOne, boolean canTele) {
		return new RectangularArea(name, new int[]{x,y,x1,y1}, -1, -1, -1, isPvp,isPlusOne,canTele);
	}
}
