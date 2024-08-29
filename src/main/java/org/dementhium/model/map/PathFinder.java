package org.dementhium.model.map;

import java.util.List;

import org.dementhium.model.Location;

/**
 * @author Graham
 */
public interface PathFinder {
    public List<Location> findPath(Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear);
}
