package org.dementhium.content.areas;

import org.dementhium.model.Location;
import org.dementhium.model.player.Player;

/**
 * Represents an area in the world.
 * @author Stephen
 */
public abstract class Area {
    public final String name;
    public final int swX, swY, nwX, nwY, radius, centerX, centerY;
    public transient int length, width;
    private final boolean isPvpZone, isPlusOne, canTele;
    
    public Area(String name, int[] coords, int radius, int centerX, int centerY, boolean isPvpZone, boolean isPlusOne, boolean canTele) {//perhaps something like
        this.name = name;
        this.swX = coords[0];
        this.swY = coords[1];
        this.nwX = coords[2];
        this.nwY = coords[3];
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.isPvpZone = isPvpZone;
        this.isPlusOne = isPlusOne;
        this.canTele = canTele;
        this.length = nwX - swX;
        this.width = nwY - swY;
    }
    
    public Area() {
        this.name = "";
        this.nwX = -1;
        this.nwY = -1;
        this.swX = -1;
        this.swY = -1;
        this.radius = -1;
        this.centerX = -1;
        this.centerY = -1;
        this.isPvpZone = false;
        this.isPlusOne = false;
        this.canTele = true;
        this.length = nwX - swX;
        this.width = nwY - swY;
    }
    
    public abstract boolean contains(Location pos);

    public boolean isPvpZone() {
        return isPvpZone;
    }

    public boolean isPlusOneZone() {
        return isPlusOne;
    }

    public boolean canTele() {
        return canTele;
    }

    public String getName() {
        return name;
    }

	public void teleTo(Player player) {
			player.teleport(swX + ((nwX - swX) / 2), swY + ((nwY - swY) / 2), 0);
		
	}

}
