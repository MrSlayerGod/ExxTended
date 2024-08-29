package org.dementhium.model.map;

import java.util.List;

import org.dementhium.model.Location;

public class DoublePathFinder implements PathFinder {
	
    public static final DoublePathFinder INSTANCE = new DoublePathFinder();

    public List<Location> findPath(Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear) {
        if (srcX < 0 || srcY < 0 || srcX >= 104 || srcY >= 104 || dstX < 0 || dstY < 0 || srcX >= 104 || srcY >= 104) {
            return null;
        }
        if (srcX == dstX && srcY == dstY) {
            return null;
        }
        List<Location> path = PrimitivePathFinder.INSTANCE.findPath(base, srcX, srcY, dstX, dstY, z, radius, running, ignoreLastStep, moveNear, true);
        if (path == null) {
            path = new AStarPathFinder().findPath(base, srcX, srcY, dstX, dstY, z, radius, running, ignoreLastStep, moveNear);
        }
        return path;
    }
}
