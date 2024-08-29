package org.dementhium.model.map;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dementhium.model.Location;
import org.dementhium.model.WalkingQueue;

/**
 * @author Graham Edgecombe
 */
public class AStarPathFinder implements PathFinder {
    /**
     * Represents a node used by the A* algorithm.
     *
     * @author Graham Edgecombe
     */
    private static class Node {
        /**
         * The cost.
         */
        private int cost = 1000;
        /**
         * The parent node.
         */
        private Node parent = null;
        /**
         * The x coordinate.
         */
        private final int x;
        /**
         * The y coordinate.
         */
        private final int y;

        /**
         * Creates a node.
         *
         * @param x The x coordinate.
         * @param y The y coordinate.
         */
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getCost() {
            return cost;
        }

        /**
         * Gets the parent node.
         *
         * @return The parent node.
         */
        public Node getParent() {
            return parent;
        }

        /**
         * Gets the X coordinate.
         *
         * @return The X coordinate.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the Y coordinate.
         *
         * @return The Y coordinate.
         */
        public int getY() {
            return y;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        /**
         * Sets the parent.
         *
         * @param parent The parent.
         */
        public void setParent(Node parent) {
            this.parent = parent;
        }
    }
    
    private static final int MAX_TRIES = 10816;
    
    private Set<Node> closed = new HashSet<Node>();
    private Node current;
    private Node[][] nodes = new Node[104][104];
    private Set<Node> open = new HashSet<Node>();
    private boolean success = false;
    
    public AStarPathFinder() {
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    /**
     * Estimates a distance between the two points.
     *
     * @param src The source node.
     * @param dst The distance node.
     * @return The distance.
     */
    public int estimateDistance(Node src, Node dst) {
        int deltaX = src.getX() - dst.getX();
        int deltaY = src.getY() - dst.getY();
        return Math.abs(deltaX) + Math.abs(deltaY);
    }

    private void examineNode(Node n) {
        int heuristic = estimateDistance(current, n);
        int nextStepCost = current.getCost() + heuristic;
        if (nextStepCost < n.getCost()) {
            open.remove(n);
            closed.remove(n);
        }
        if (!open.contains(n) && !closed.contains(n)) {
            n.setParent(current);
            n.setCost(nextStepCost);
            open.add(n);
        }
    }

    public List<Location> findPath(Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear) {
        if (srcX < 0 || srcY < 0 || srcX >= 104 || srcY >= 104 || dstX < 0 || dstY < 0 || dstX >= 104 || dstY >= 104) {
            return null;
        }
        if (srcX == dstX && srcY == dstY) {
            success = true;
            return null;
        }
        Location location = Location.create((base.getRegionX() - 6) << 3, (base.getRegionY() - 6) << 3, base.getZ());
        Node src = nodes[srcX][srcY];
        Node dest = nodes[dstX][dstY];
        src.setCost(0);
        open.add(src);
        int loops = 0;
        while (open.size() > 0 && loops++ < MAX_TRIES) {
            current = getLowestCost();
            if (current == dest) {
                break;
            }
            open.remove(current);
            closed.add(current);
            int x = current.getX(), y = current.getY();
            int absX = location.getX() + x, absY = location.getY() + y;
            // south
            if (y > 0 && (Region.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                Node n = nodes[x][y - 1];
                examineNode(n);
            }
            // west
            if (x > 0 && (Region.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0) {
                Node n = nodes[x - 1][y];
                examineNode(n);
            }
            // north
            if (y < 104 - 1 && (Region.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                Node n = nodes[x][y + 1];
                examineNode(n);
            }
            // east
            if (x < 104 - 1 && (Region.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0) {
                Node n = nodes[x + 1][y];
                examineNode(n);
            }
            // south-west
            if (x > 0 && y > 0 && (Region.getClippingMask(absX - 1, absY - 1, z) & 0x43a40000) == 0 && (Region.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0 && (Region.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                Node n = nodes[x - 1][y - 1];
                examineNode(n);
            }
            // north-west
            if (x > 0 && y < 104 - 1 && (Region.getClippingMask(absX - 1, absY + 1, z) & 0x4e240000) == 0 && (Region.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0 && (Region.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                Node n = nodes[x - 1][y + 1];
                examineNode(n);
            }
            // south-east
            if (x < 104 - 1 && y > 0 && (Region.getClippingMask(absX + 1, absY - 1, z) & 0x60e40000) == 0 && (Region.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0 && (Region.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                Node n = nodes[x + 1][y - 1];
                examineNode(n);
            }
            // north-east
            if (x < 104 - 1 && y < 104 - 1 && (Region.getClippingMask(absX + 1, absY + 1, z) & 0x78240000) == 0 && (Region.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0 && (Region.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                Node n = nodes[x + 1][y + 1];
                examineNode(n);
            }
        }
        if (dest.getParent() == null) {
            if (moveNear) {
                boolean foundPath = false;
                int curCost = 1000;
                int curX = 0;
                int curY = 0;
                for (int depth = 1; depth < 10; depth++) {
                    for (int x = dstX - depth; x <= dstX + depth; x++) {
                        for (int y = dstY - depth; y <= dstY + depth; y++) {
                            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                                Node n = nodes[x][y];
                                int cost = n.getCost();
                                if (cost < curCost) {
                                    curCost = cost;
                                    curX = x;
                                    curY = y;
                                    foundPath = true;
                                }
                            }
                        }
                    }
                    if (foundPath) {
                        open.clear();
                        closed.clear();
                        for (int x = 0; x < 104; x++) {
                            for (int y = 0; y < 104; y++) {
                                nodes[x][y] = new Node(x, y);
                            }
                        }
                        return findPath(base, srcX, srcY, curX, curY, base.getZ(), 1, running, ignoreLastStep, false);
                    }
                }
                return null;
            }
            return null;
        }
        LinkedList<Location> p = new LinkedList<Location>();
        Node n = !ignoreLastStep ? dest : dest.getParent();
        int add = 0;
        while (n != null && n != src) {
        	if(++add >= WalkingQueue.SIZE) {
        		break;
        	}
            int absX = location.getX() + n.getX();
            int absY = location.getY() + n.getY();
            p.addFirst(Location.create(absX, absY, z));
            n = n.getParent();
        }
        success = true;
        return p;
    }

    private Node getLowestCost() {
        Node curLowest = null;
        for (Node n : open) {
            if (curLowest == null) {
                curLowest = n;
            } else {
                if (n.getCost() < curLowest.getCost()) {
                    curLowest = n;
                }
            }
        }
        return curLowest;
    }

    public boolean isSuccessful() {
        return success;
    }
}