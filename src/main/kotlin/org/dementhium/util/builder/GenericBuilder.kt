package org.dementhium.util.builder

abstract class GenericBuilder<T>(val builderId: String) {

    fun builderError(desc: () -> String) {
        println("ERROR IN [$builderId]: ${desc()}")
        println("PLEASE CONTACT STAFF IMMEDIATELY")
    }

    companion object;

    abstract fun build(): T
}
