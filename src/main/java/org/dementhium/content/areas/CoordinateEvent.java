package org.dementhium.content.areas;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public abstract class CoordinateEvent {

	private Mob mob;
	
	private int x;
	private int y;
	private int lengthX;
	private int lengthY;

	public boolean areaObject = false;

	public CoordinateEvent(Mob mob, int x, int y) {
		this.mob = mob;
		this.x = x;
		this.y = y;
		areaObject = false;
	}
	
	public CoordinateEvent(Mob mob, int x, int y, int lengthX, int lengthY) {
		this.mob = mob;
		this.x = x;
		this.y = y;
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		areaObject = true;
	}


	public boolean atArea() {
		Location loc = mob.getLocation();
		return loc.getX() == x && loc.getY() == y;
	}

	public boolean inArea() {
		Location loc = mob.getLocation();
		return loc.getX() >= x - lengthX && loc.getY() >= y - lengthY && loc.getX() <= x + lengthX && loc.getY() <= y + lengthY;
	}

	public abstract void execute();
}
