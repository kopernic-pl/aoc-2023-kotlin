package net.wrony.aoc2023.a8

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun main() {
    Path("src/main/resources/8.txt").readLines().let {
        it[0].toCharArray() to it.drop(2).map { m ->
            m.split(" = ")
                .let { sl -> sl[0] to (sl[1].trim().drop(1).dropLast(1).split(", ").take(2)) }
        }
    }.let { (plan, map) ->
        plan.asSequence().repeat() to map.toMap()
    }.also { (plan, map) ->
        plan.foldIndexed("AAA") { idx, acc, dir ->
            if (acc == "ZZZ") {
                println(" - $dir: $acc $idx")
                return@also
            }
            map[acc]?.let { (left, right) ->
                when (dir) {
                    'L' -> left
                    'R' -> right
                    else -> throw Exception("Unknown direction $dir")
                }
            } ?: throw Exception("Unknown key $acc")
        }
    }.also { (plan, map) ->
        map.keys.filter { it.endsWith("A") }.map { k ->
            plan.foldIndexed(k to 0) { idx, acc, dir ->
                if (acc.first.endsWith("Z")) {
                    println("$acc $idx")
                    return@map acc to idx
                }
                map[acc.first]?.let { (left, right) ->
                    when (dir) {
                        'L' -> left to idx
                        'R' -> right to idx
                        else -> throw Exception("Unknown direction $dir")
                    }
                } ?: throw Exception("Unknown key $acc")
            }
        }.map { (_, v) -> factors(v.toLong()) }.reduce { acc, f -> acc + f }.toSet()
            .fold(1L) { acc, n -> acc * n }.let { println(it) }
    }
    // 7159165833283004735 -
    // 50530847183 -
    // 13289612809129
}

fun factors(value: Long): List<Long> {
    val factors = mutableListOf<Long>()
    var n = value
    var i = 2L
    while (i * i <= n) {
        while (n % i == 0L) {
            factors.add(i)
            n /= i
        }
        i++
    }
    if (n > 1) {
        factors.add(n)
    }
    return factors
}