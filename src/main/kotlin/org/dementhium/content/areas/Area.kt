package org.dementhium.content.areas

import org.dementhium.model.Location
import org.dementhium.model.player.Player

abstract class Area(
   @JvmField val name: String,
   @JvmField val coords: IntArray,
   @JvmField val radius: Int,
   @JvmField val centerX: Int,
   @JvmField val centerY: Int,
   @JvmField val isPvpZone: Boolean,
   @JvmField val isPlusOne: Boolean,
   @JvmField val canTele: Boolean
) {
    @JvmField val swX: Int = coords[0]
    @JvmField val swY: Int = coords[1]
    @JvmField val nwX: Int = coords[2]
    @JvmField val nwY: Int = coords[3]

    @Transient @JvmField
    var length: Int = nwX - swX

    @Transient @JvmField
    var width: Int = nwY - swY

    constructor(): this("",  intArrayOf(-1, -1, -1, -1), -1, -1, -1, false, false, true)

    abstract fun contains(pos: Location): Boolean

    fun canTele(): Boolean  = canTele
    fun getName() = name

    fun teleTo(player: Player) {
        player.teleport(swX + ((nwX - swX) / 2), swY + ((nwY - swY) / 2), 0)
    }

    companion object {
        val InvalidArea = object: Area(
            "INVALID_AREA_XX",
            intArrayOf(-1, -1, -1, -1), 0, 0, 0,
            false, false, false
        ) { override fun contains(pos: Location): Boolean = false }
    }
}