package org.dementhium.content.misc;

import org.dementhium.model.Mob;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.model.World;
import org.dementhium.net.ActionSender;
import org.dementhium.model.Location;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public class Following {

	public static void familiarFollow(NPC npc, Mob owner) {
		int firstX = (owner.getLocation().getX() - npc.size()) - (npc.getLocation().getRegionX() - 6) * 8;
		int firstY = (owner.getLocation().getY() - npc.size()) - (npc.getLocation().getRegionY() - 6) * 8;
		npc.turnTo(owner);
		if(!npc.getLocation().withinRange(owner.getLocation(), owner.size())) {
			npc.getWalkingQueue().reset();
			npc.getWalkingQueue().addClippedWalkingQueue(firstX, firstY);
		}
	}

	public static void npcFollow(NPC npc, Mob owner) {
		int firstX = owner.getLocation().getX() - (npc.getLocation().getRegionX() - 6) * 8;
		int firstY = owner.getLocation().getY() - (npc.getLocation().getRegionY() - 6) * 8;
		Location loc = new Location(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ());
		npc.turnTo(owner);
		if(npc.getCombatState().isFrozen()) {
			return;
		}
		if(npc.getOriginalLocation().distance(loc) > 14) {
			return;
		}
		if(!npc.getLocation().withinRange(owner.getLocation(), owner.size())) {
			npc.getWalkingQueue().reset();
			npc.getWalkingQueue().addClippedWalkingQueue(firstX, firstY);
		}
	}

	public static void combatFollow(Mob mob, Mob other) {
		int fromX = mob.getLocation().getX(), fromY = mob.getLocation().getY();
		int toX = other.getLocation().getX(), toY = other.getLocation().getY();
		mob.turnTo(other);
		if(mob.getCombatState().isFrozen()) {
			return;
		}
		if(!mob.getLocation().withinDistance(other.getLocation())) {
			return;
		}
		if(mob.getLocation().distance(other.getLocation()) != 1) {
			int newX = -1, newY = -1;
			if (toX - fromX > 1 && toY - fromY > 1) {
				newX = fromX + ((toX - fromX) - 1);
				newY = fromY + ((toY - fromY) - 1);
			} else if (toX - fromX > 1 && fromY - toY > 1) {
				newX = fromX + ((toX - fromX) - 1);
				newY = fromY - ((fromY - toY) - 1);
			} else if (fromX - toX > 1 && toY - fromY > 1) {
				newX = fromX - ((fromX - toX) - 1);
				newY = fromY + ((toY - fromY) - 1);
			} else if (fromX - toX > 1 && fromY - toY > 1) {
				newX = fromX - ((fromX - toX) - 1);
				newY = fromY - ((fromY - toY) - 1);
			} else if (toX - fromX > 1) {
				newX = fromX + ((toX - fromX) - 1);
				newY = fromY;
			} else if (fromX - toX > 1) {
				newX = fromX - ((fromX - toX) - 1);
				newY = fromY;
			} else if (toY - fromY > 1) {
				newX = fromX;
				newY = fromY + ((toY - fromY) - 1);
			} else if (fromY - toY > 1) {
				newX = fromX;
				newY = fromY - ((fromY - toY) - 1);
			}
			int lastFollowX = (Integer) mob.getAttribute("lastFollowX", -1);
			int lastFollowY = (Integer) mob.getAttribute("lastFollowY", -1);
			if(lastFollowX != newX || lastFollowY != newY) {
				mob.setAttribute("lastFollowX", newX);
				mob.setAttribute("lastFollowY", newY);
				int firstX = newX - (mob.getLocation().getRegionX() - 6) * 8;
				int firstY = newY - (mob.getLocation().getRegionY() - 6) * 8;
				World.getWorld().doPath(new AStarPathFinder(), mob, newX, newY);
				//mob.getWalkingQueue().reset();
				//mob.getWalkingQueue().addClippedWalkingQueue2(firstX, firstY);
				//mob.requestWalk(newX, newY);
			}
		}
	}

}
