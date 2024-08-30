package org.dementhium.content.areas

import org.dementhium.io.XMLHandler

class AreaManager {

    fun getAreaByName(name: String): Area = loadedAreas.firstOrNull { area -> area.name == name } ?: Area.InvalidArea

    var loadedAreas: List<Area> = try {
        println("Loading areas...")
        XMLHandler.fromXML<List<Area>>("data/xml/areas.xml").also {
            println("Loaded ${it.size} areas.")
        }
    } catch (e: Exception) {
        println("Could not load areas (${e.message})")
        listOf()
    }
}