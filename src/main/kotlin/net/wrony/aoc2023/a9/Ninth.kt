package net.wrony.aoc2023.a9

import kotlin.io.path.Path
import kotlin.io.path.readLines


fun diffs(l: List<Int>): List<Int> {
    return l.zipWithNext().map { (a, b) -> b - a }
}

fun listDown(l: List<Int>): List<List<Int>> {
    var d = l
    var acc = listOf(l)
    while (!diffs(d).all { it == 0 }) {
        d = diffs(d)
        acc = acc.plusElement(d)
    }
    return acc
}

fun lastFolded(l: List<Int>): Int {
    return if (listDown(l).last().toSet().size == 1) listDown(l).last()[0] else throw Exception("Nonequal last line")
}

fun main() {
    Path("src/main/resources/9.txt").readLines().map { l -> l.split(" ").map { it.toInt() } }
        .let { inputs: List<List<Int>> ->
            listOf({ acc: Int, l: List<Int> -> l.last() + acc },
                { acc: Int, l: List<Int> -> l.first() - acc }).forEach { algo ->
                inputs.map(::listDown).let { lists ->
                    lists.sumOf { i ->
                        i.reversed().drop(1).fold(lastFolded(i[0]), algo)
                    }
                }.let { println(it) }
            }
        }
}
// 1819126914 too high
// 1819125966

// 1140 ok