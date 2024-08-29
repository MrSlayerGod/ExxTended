package org.dementhium.model.map;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.dementhium.model.Location;

public class PrimitivePathFinder implements PathFinder {
	
    public static PrimitivePathFinder INSTANCE = new PrimitivePathFinder();

    public static boolean canMove(Location source, Directions.WalkingDirection dir, boolean diagonalCheck) {
        return canMove(source, dir, diagonalCheck, false);
    }
	
	public static Point getNextStep(Location source, int toX, int toY, int height, int xLength, int yLength) {
    	int baseX = source.getLocalX(), baseY = source.getLocalY();
        int moveX = 0;
        int moveY = 0;
        if (baseX - toX > 0) {
            moveX--;
        } else if (baseX - toX < 0) {
            moveX++;
        }
        if (baseY - toY > 0) {
            moveY--;
        } else if (baseY - toY < 0) {
            moveY++;
        }
        if (canMove(source, baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
            return new Point(baseX + moveX, baseY + moveY);
        } else if (moveX != 0 && canMove(source, baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
            return new Point(baseX + moveX, baseY);
        } else if (moveY != 0 && canMove(source, baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
            return new Point(baseX, baseY + moveY);
        }
        return null;
    }
    
    public static Point getNextStep(Location source, int baseX, int baseY, int moveX, int moveY, int height, int xLength, int yLength) {
        if (canMove(source, baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
            return new Point(baseX + moveX, baseY + moveY);
        } else if (canMove(source, baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
            return new Point(baseX + moveX, baseY);
        } else if (canMove(source, baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
            return new Point(baseX, baseY + moveY);
		} else if (canMove(source, baseX, baseY, baseX - moveX, baseY - moveY, height, xLength, yLength)) {
            return new Point(baseX - moveX, baseY - moveY);
        } else if (canMove(source, baseX, baseY, baseX, baseY - moveY, height, xLength, yLength)) {
            return new Point(baseX, baseY - moveY);
        } else if (canMove(source, baseX, baseY, baseX - moveX, baseY, height, xLength, yLength)) {
            return new Point(baseX - moveX, baseY);
        }
        return null;
    }
    
    public static boolean canMove(Location base, int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
    	Location location = Location.create((base.getRegionX() - 6) << 3, (base.getRegionY() - 6) << 3, base.getZ());
        int diffX = endX - startX;
        int diffY = endY - startY;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = location.getX() + (endX - diffX);
            int currentY = location.getY() + (endY - diffY);
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((Region.getClippingMask(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0 || (Region.getClippingMask(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0 || (Region.getClippingMask(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((Region.getClippingMask(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0 || (Region.getClippingMask(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0 || (Region.getClippingMask(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((Region.getClippingMask(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0 || (Region.getClippingMask(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0 || (Region.getClippingMask(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((Region.getClippingMask(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0 || (Region.getClippingMask(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0 || (Region.getClippingMask(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((Region.getClippingMask(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((Region.getClippingMask(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((Region.getClippingMask(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((Region.getClippingMask(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }
    
    public static boolean canMove(Location source, Directions.WalkingDirection dir, boolean diagonalCheck, boolean npcCheck) {
        // TODO
        // if(!diagonalCheck)
        // Make it so they only get stuck on the following directions: NORTH,
        // SOUTH, EAST, WEST
        int absX = source.getX();
        int absY = source.getY();
        int z = source.getZ();
        switch (dir) {
            case SOUTH:
                if ((Region.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                    if (npcCheck && Location.create(absX, absY - 1, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case WEST:
                if ((Region.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0) {
                    if (npcCheck && Location.create(absX - 1, absY, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case NORTH:
                if ((Region.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                    if (npcCheck && Location.create(absX, absY + 1, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case EAST:
                if ((Region.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0) {
                    if (npcCheck && Location.create(absX + 1, absY, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case SOUTH_WEST:
                if ((Region.getClippingMask(absX - 1, absY - 1, z) & 0x43a40000) == 0 && (Region.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0 && (Region.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                    if (npcCheck && Location.create(absX - 1, absY - 1, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case NORTH_WEST:
                if ((Region.getClippingMask(absX - 1, absY + 1, z) & 0x4e240000) == 0 && (Region.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0 && (Region.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                    if (npcCheck && Location.create(absX - 1, absY + 1, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case SOUTH_EAST:
                if ((Region.getClippingMask(absX + 1, absY - 1, z) & 0x60e40000) == 0 && (Region.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0 && (Region.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                    if (npcCheck && Location.create(absX + 1, absY - 1, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
            case NORTH_EAST:
                if ((Region.getClippingMask(absX + 1, absY + 1, z) & 0x78240000) == 0 && (Region.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0 && (Region.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                    if (npcCheck && Location.create(absX + 1, absY + 1, z).containsNPCs()) {
                        return false;
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public List<Location> findPath(Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear) {
        return findPath(base, srcX, srcY, dstX, dstY, z, radius, running, ignoreLastStep, moveNear, false);
    }

    public List<Location> findPath(Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear, boolean nullOnFail) {
        if (srcX < 0 || srcY < 0 || srcX >= 104 || srcY >= 104 || dstX < 0 || dstY < 0 || srcX >= 104 || srcY >= 104) {
            return null;
        }
        if (srcX == dstX && srcY == dstY) {
            return null;
        }
        Location location = Location.create((base.getRegionX() - 6) << 3, (base.getRegionY() - 6) << 3, base.getZ());
        LinkedList<Location> path = new LinkedList<Location>();
        int curX = srcX;
        int curY = srcY;
        final int stepCount = running ? 6 : 3;
        for (int i = 0; i < stepCount; i++) {
        	Location curLoc = Location.create(location.getX() + curX, location.getY() + curY, location.getZ());
            Directions.WalkingDirection dstDir = Directions.directionFor(curX, curY, dstX, dstY);
            if (dstDir != null && canMove(curLoc, dstDir, false)) {
            	Location step = curLoc.transform(Directions.DIRECTION_DELTA_X[dstDir.intValue()], Directions.DIRECTION_DELTA_Y[dstDir.intValue()], 0);
                curX = step.getLocalX(base);
                curY = step.getLocalY(base);
                if (!ignoreLastStep || curX != dstX || curY != dstY) {
                    path.add(step);
                } else {
                    break;
                }
            } else if (nullOnFail) {
                return null;
            } else {
                break;
            }
        }
        return path;
    }
}
