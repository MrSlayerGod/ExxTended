package org.dementhium.util.builder

inline fun <T, reified B: GenericBuilder<T>> build(
    block: B.() -> Unit
): T = B::class.constructors.first().call().apply(block).build()