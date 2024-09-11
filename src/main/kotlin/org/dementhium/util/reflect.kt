package org.dementhium.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType

fun <T: Any> findConstructorThatMatches(
    thisClass: KClass<out T>,
    paramTypesInOrder: List<KType>
): KFunction<T> {
    val constructors: Collection<KFunction<T>> = thisClass.constructors
    val firstCorrectConstructor = constructors.first {
        val params = it.parameters
        if (params.size != paramTypesInOrder.size) return@first false
        (params.zip(paramTypesInOrder)).forEach { (param, type) ->
            if (param.type != type) return@first false
        }
        true
    }
    return firstCorrectConstructor
}

inline fun <reified T: Any> findConstructorThatMatches(
    vararg paramTypesInOrder: KType
): KFunction<T> = findConstructorThatMatches(T::class, paramTypesInOrder.toList())

inline fun <T: Any> findConstructorThatMatches(
    thisClass: KClass<out T>,
    args: Map<KParameter, Any?>
): KFunction<T> {
    val constructors = thisClass.constructors
    val firstCorrectConstructor = constructors.first {
        val params = it.parameters
        if (params.size != args.size) return@first false
        val namesMatch = params.all { paramName -> args.any { (arg, argValue) -> arg.name == paramName.name }}
        val typesMatch = params.all { paramName -> args.any { (arg, argValue) -> arg.type == paramName.type }}
        namesMatch && typesMatch
    }
    return firstCorrectConstructor
}