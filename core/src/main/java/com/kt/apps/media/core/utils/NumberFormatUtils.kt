package com.kt.apps.media.core.utils

import java.util.NavigableMap
import java.util.TreeMap
import kotlin.math.roundToLong


val suffixes: NavigableMap<Long, String> by lazy {
    TreeMap<Long, String>().apply {
        this[1_000L] = "k"
        this[1_000_000L] = "M"
        this[1_000_000_000L] = "G"
        this[1_000_000_000_000L] = "T"
        this[1_000_000_000_000_000L] = "P"
        this[1_000_000_000_000_000_000L] = "E"
    }
}

fun Long.format(): String {
    val value = this
    if (this == Long.MIN_VALUE) return (value + 1).format()
    if (value < 0) return "-" + (-1 * value).format()
    if (value < 1000) return value.toString()
    val e = suffixes.floorEntry(value)
    val divideBy = e.key
    val suffix = e.value
    val truncated = (value / (divideBy / 10.0)).roundToLong()
    val hasDecimal = truncated < 1000 && (truncated / 100.0) != (truncated / 100).toDouble()
    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
}