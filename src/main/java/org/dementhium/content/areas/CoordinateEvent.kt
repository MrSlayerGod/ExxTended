package org.dementhium.content.areas

import org.dementhium.model.Mob

abstract class CoordinateEvent @JvmOverloads constructor (
    val mob: Mob,
    val x: Int,
    val y: Int,
    val lengthX: Int = 0,
    val lengthY: Int = 0
) {

    var areaObject: Boolean = !(lengthX == 0 || lengthY == 0)

    fun atArea(): Boolean {
        val loc = mob.location
        return loc.x == x && loc.y == y
    }

    fun inArea(): Boolean {
        val loc = mob.location
        return loc.x >= x - lengthX && loc.y >= y - lengthY && loc.x <= x + lengthX && loc.y <= y + lengthY
    }

    abstract fun execute()
}