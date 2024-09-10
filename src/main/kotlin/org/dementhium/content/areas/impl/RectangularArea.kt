package org.dementhium.content.areas.impl

import org.dementhium.content.areas.Area
import org.dementhium.model.Location

class RectangularArea : Area {

    constructor(): super()

    constructor(
        name: String, coords: IntArray, radius: Int, centerX: Int, centerY: Int, isPvpZone: Boolean,
        isPlusOneZone: Boolean, canTele: Boolean
    ) : super(name, coords, radius, centerX, centerY, isPvpZone, isPlusOneZone, canTele)

    override fun contains(pos: Location): Boolean = pos.x in swX..nwX && (pos.y in swY..nwY)

    companion object {
        fun create(
            name: String,
            x: Int, y: Int, x1: Int, y1: Int,
            isPvp: Boolean, isPlusOne: Boolean, canTele: Boolean
        ): RectangularArea {
            return RectangularArea(name, intArrayOf(x, y, x1, y1), -1, -1, -1, isPvp, isPlusOne, canTele)
        }
    }
}