package net.wrony.aoc2023.a12

import kotlin.io.path.Path
import kotlin.io.path.readLines


fun txtToSprings2(s: String): Pair<String, List<Int>> {
    return s.split(" ").let { (inp, desc) -> inp to desc.split(",").map { it.toInt() } }
}

val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

fun scanput2(str: String, counts: List<Int>): Long {
    // if str is empty - end of story
    if (str.isEmpty()) return if (counts.isEmpty()) 1 else 0

    if (counts.isEmpty()) return if (str.contains('#')) 0 else 1

    println("Cache check for $str to $counts")

    return cache.getOrPut(str to counts) {

        println(str)

        var res = 0L

        if (str[0] in ".?") {
            res += scanput2(str.drop(1), counts)
        }

        if (str[0] in "#?") {
            if (counts[0] <= str.length && str.take(counts[0])
                    .all { it != '.' } && (counts[0] == str.length || str[counts[0]] != '#')
            ) {
                res += scanput2(str.drop(counts[0] + 1), counts.drop(1))
            }
        }
        res
    }

}

fun main() {
    Path("src/main/resources/12.txt").readLines().map { txtToSprings2(it) }.also {
        it.sumOf { (inp, desc) -> scanput2(inp, desc) }.also { println("Part 1: $it") }
    }
    .also {
        it.map { (inp, blocks) ->
            generateSequence { inp }.take(5).toList()
                .joinToString("?") to List(5) { blocks }.flatten()
        }
        .sumOf { (inp, blocks) ->
            scanput2(inp, blocks)
        }.let { p2res -> println("Part 2: $p2res") }

    }
}

