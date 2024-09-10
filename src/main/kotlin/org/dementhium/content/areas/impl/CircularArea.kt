package org.dementhium.content.areas.impl

import org.dementhium.content.areas.Area
import org.dementhium.model.Location

class CircularArea @JvmOverloads constructor(
    name: String = "NO_CIRCULAR_AREA",
    coords: IntArray = intArrayOf(0, 0, 0, 0),
    radius: Int = -1,
    centerX: Int = -1,
    centerY: Int = -1,
    isPvpZone: Boolean = false,
    isPlusOneZone: Boolean = false,
    canTele: Boolean = true
) : Area(name, coords, radius, centerX, centerY, isPvpZone, isPlusOneZone, canTele) {

    private var center: Location = Location.create(centerX, centerY, 0)

    override fun contains(pos: Location): Boolean = (center.getDistance(pos) < radius)
}