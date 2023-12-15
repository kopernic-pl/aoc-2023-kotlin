package net.wrony.aoc2023.a14

import java.util.Arrays
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    Path("src/main/resources/14.txt").readLines().map { it.toCharArray() }.toTypedArray()
        .also {
            val size = it.size
            transpose(it).map { r -> shiftTheRow(r) }.toTypedArray().let { arr ->
                transpose(arr)
            }
            .let { arr -> calcLoad(arr) }
            .let { println(it) }
        }.let lit@{
            var arr = it
            val hashToIdxAndWeight = mutableMapOf<Int, Pair<Int, Int>>()
            (0..10000).forEach {i ->
                arr.hash().let { h -> if (hashToIdxAndWeight.containsKey(h)) {println("cycle detected: ${hashToIdxAndWeight[h]} -> $i"); return@lit Triple(hashToIdxAndWeight, hashToIdxAndWeight[h]!!.first, i)} }
                hashToIdxAndWeight[arr.hash()] = i to calcLoad(arr)
                arr = cycle(arr)
            }
            Triple(hashToIdxAndWeight, 0, 0)
        }.let { (hashToIdxAndWeight, first , second) ->
            val nbofcycles : Long = 1000000000
            (first..second).forEach {
                if ((nbofcycles - it) % (second-first) == 0L) {
                    println("cycle index at mark: $it")
                    return@let hashToIdxAndWeight.values.first { (idx, _) -> idx == it }.second
                }
            }
        }.let { println(
            "answer 2: $it"
        ) }
}

fun Array<CharArray>.hash(): Int {
    var hash = 0
    for (r in this.indices) {
        for (c in this[r].indices) {
            hash = hash * 31 + this[r][c].hashCode()
        }
    }
    return hash
}

fun cycle(arr: Array<CharArray>): Array<CharArray> {
    var out = arr
    repeat(4) {
        out = transpose(out).map { r -> shiftTheRow(r) }.toTypedArray().let { transpose(it) }.let { rotateClockwise(it) }
    }
    return out
}

private fun calcLoad(arr: Array<CharArray>) = arr.map { r -> r.count { c -> c == 'O' } }
    .foldIndexed(0) { idx: Int, acc: Int, v: Int -> acc + v * (arr.size - idx) }

fun shiftTheRow(r: CharArray): CharArray {
    val out = CharArray(r.size) { '.' }
    var lastFree = 0
    for (i in r.indices) {
        when (r[i]) {
            'O' -> {
                out[lastFree] = 'O'; lastFree++
            }

            '#' -> {
                out[i] = '#'; lastFree = i + 1
            }
        }
    }
    return out
}

private fun transpose(arr: Array<CharArray>): Array<CharArray> {
    val transpose = Array(arr.size) { CharArray(arr.size) }
    for (c in arr.indices) {
        for (r in arr[c].indices) {
            transpose[c][r] = arr[r][c]
        }
    }
    return transpose
}

fun rotateClockwise(arr: Array<CharArray>): Array<CharArray> {
    val rotated = Array(arr.size) { CharArray(arr.size) }
    for (r in arr.indices) {
        for (c in arr[r].indices) {
            rotated[c][arr.size - 1 - r] = arr[r][c]
        }
    }
    return rotated
}