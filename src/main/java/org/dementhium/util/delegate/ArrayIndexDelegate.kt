package org.dementhium.util.delegate

fun ByteArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
fun ShortArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
fun CharArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
fun IntArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
fun LongArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
fun FloatArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
fun DoubleArray.delegate(index: Int) = GetSetDelegate(
    get = { get(index) },
    set = {value -> set(index, value) }
)
