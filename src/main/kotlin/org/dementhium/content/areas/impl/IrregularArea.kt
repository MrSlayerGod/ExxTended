package org.dementhium.content.areas.impl

import org.dementhium.content.areas.Area
import org.dementhium.model.Location

class IrregularArea(
    name: String, coords: IntArray, radius: Int, centerX: Int, centerY: Int, isPvpZone: Boolean,
    isPlusOneZone: Boolean, canTele: Boolean
) : Area(name, coords, radius, centerX, centerY, isPvpZone, isPlusOneZone, canTele) {

    val locations = mutableSetOf<Area>()

    override fun contains(pos: Location): Boolean = locations.any { it.contains(pos) }
}