package org.dementhium.content.areas.impl;

import org.dementhium.content.areas.Area;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;

/**
 * Represents a circular area in the world.
 * 
 * @author Stephen
 */
public class CircularArea extends Area {

	private Location center;

	public CircularArea() {
		this("NO_AREA", new int[] { 0, 0, 0, 0 }, -1, -1, -1, false, false, true);
	}

	public CircularArea(String name, int[] coords, int radius, int centerX, int centerY, boolean isPvpZone,
			boolean isPlusOneZone, boolean canTele) {
		super(name, coords, radius, centerX, centerY, isPvpZone, isPlusOneZone, canTele);
		center = Location.create(centerX, centerY, 0);
	}

	/**
	 * Not sure about this, i think it should work though.
	 */
	@Override
	public boolean contains(Location pos) {
		if (center == null || radius == 0) {
			center = Location.create(centerX, centerY, 0);
		}
		return (center.getDistance(pos) < radius);
		
	}

	public void spawnItemsOnPoints() {
		RectangularArea rect = new RectangularArea("bob", new int[] { 3145, 3172, 3185, 3506 }, -1, -1, -1, false, false, true);
		for (Location pos : rect.generatePointsWithin()) {
			if (contains(pos)) {
				World.getWorld().getGroundItemManager().sendGlobalGroundItem(World.getWorld().getGroundItemManager().create(null, new Item(995, 100000), pos), false);
			}
		}
	}
}
