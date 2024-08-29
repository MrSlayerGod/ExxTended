package org.dementhium.model.map;

import java.util.ArrayDeque;
import java.util.Deque;

import org.dementhium.model.Location;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PathState {
	
	private Deque<Location> points = new ArrayDeque<Location>();
	private boolean routeFound = true;
	
	
	public Deque<Location> getPoints() {
		return points;
	}
	
	public void routeFailed() {
		this.routeFound = false;
	}
	
	public boolean isRouteFound() {
		return routeFound;
	}
}
