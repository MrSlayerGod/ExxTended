package org.dementhium.util.map

fun <K, V> Map<K, V>.reverse(): Map<V, K> = buildMap {
    for ((k, v) in this@reverse) {
        put(v, k)
    }
}

fun <K, V> Map<K, V>.codeprint(separateEntries: Boolean = true): String = buildString {
    fun nl() { if (separateEntries) append("\n") }
    append("mutableMapOf(")
    this@codeprint.forEach { (key, value) ->
        nl()
        append("\t")
        append(key)
        append(" to ")
        append(value)
        append(",")
    }
    nl()
    append(")")
}
