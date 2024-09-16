package org.dementhium.content.misc

import org.dementhium.model.Location
import org.dementhium.model.Mob
import org.dementhium.model.World
import org.dementhium.model.map.AStarPathFinder
import org.dementhium.model.npc.NPC

enum class Coordinate(
    inline val get: (Mob) -> Int
){
    X(get={ it.location.x }), Y(get={ it.location.y }), Z(get={ it.location.z })
}

/**
 *
 * @author 'Mystic Flow
 */
object Following {

    const val MaxNpcFollowDistance = 14

    @JvmStatic
    fun familiarFollow(npc: NPC, owner: Mob) {
        val firstX = (owner.location.x - npc.size()) - (npc.location.regionX - 6) * 8
        val firstY = (owner.location.y - npc.size()) - (npc.location.regionY - 6) * 8
        npc.turnTo(owner)
        if (!npc.location.withinRange(owner.location, owner.size())) {
            npc.walkingQueue.reset()
            npc.walkingQueue.addClippedWalkingQueue(firstX, firstY)
        }
    }

    @JvmStatic
    fun npcFollow(npc: NPC, owner: Mob) {
        val firstX = owner.location.x - (npc.location.regionX - 6) * 8
        val firstY = owner.location.y - (npc.location.regionY - 6) * 8
        val loc = Location(owner.location.x, owner.location.y, owner.location.z)
        npc.turnTo(owner)
        if (npc.combatState.isFrozen) {
            return
        }
        if (npc.originalLocation.distance(loc) > MaxNpcFollowDistance) {
            return
        }
        if (!npc.location.withinRange(owner.location, owner.size())) {
            npc.walkingQueue.reset()
            npc.walkingQueue.addClippedWalkingQueue(firstX, firstY)
        }
    }

    @JvmStatic
    fun combatFollow(mob: Mob, other: Mob) {
        mob.turnTo(other)
        if (mob.combatState.isFrozen) {
            return
        }

        if (!mob.location.withinDistance(other.location)) {
            return
        }

        fun calcNextCoord(coordinate: Coordinate): Int {
            val from = coordinate.get(mob)
            val to = coordinate.get(other)
            val toDelta = to - from
            val fromDelta = from - to
            val newCoordinate = if (toDelta > 1) {
                from + (toDelta - 1)
            } else {
                from - (fromDelta - 1)
            }
            val coordAttr = "lastFollow${coordinate.name}"
            val lastFollow = mob.getAttribute(coordAttr, -1)
            if (lastFollow != newCoordinate) {
                mob.setAttribute(coordAttr, newCoordinate)
            }
            return newCoordinate
        }
        if (mob.location.distance(other.location) != 1) {
            val newX = calcNextCoord(Coordinate.X)
            val newY = calcNextCoord(Coordinate.Y)
            World.getWorld().doPath(AStarPathFinder(), mob, newX, newY)

        }
    }
}