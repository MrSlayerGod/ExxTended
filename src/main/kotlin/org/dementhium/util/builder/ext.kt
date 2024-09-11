package org.dementhium.util.builder

import org.dementhium.util.findConstructorThatMatches
import kotlin.reflect.KParameter

inline fun <T, reified B: GenericBuilder<T>> build(
    byArgs: Map<KParameter, Any?> = mapOf(),
    block: B.() -> Unit,
): T {
    val builderConstructor = findConstructorThatMatches(B::class, byArgs)
    val builder = builderConstructor.callBy(byArgs)
    return builder.apply(block).build()
}
