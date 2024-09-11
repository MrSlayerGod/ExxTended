package org.dementhium.util.builder

import org.dementhium.util.findConstructorThatMatches
import kotlin.reflect.KParameter

inline fun <T, reified B: GenericBuilder<T>> build(
    block: B.() -> Unit,
    byArgs: Map<KParameter, Any?> = mapOf(),
): T {
    val builderConstructor = findConstructorThatMatches(B::class, byArgs)
    val builder = builderConstructor.callBy(byArgs)
    return builder.apply(block).build()
}
