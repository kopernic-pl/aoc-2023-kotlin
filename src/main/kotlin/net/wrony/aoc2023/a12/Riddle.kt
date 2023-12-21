package net.wrony.aoc2023.a12

import kotlin.io.path.Path
import kotlin.io.path.readLines


fun txtToSprings(s: String): Pair<String, List<Int>> {
    return s.split(" ").let { (inp, desc) -> inp to desc.split(",").map { it.toInt() } }
}

fun isPossible(s: Pair<String, List<Int>>): Boolean {
    val (inp, desc) = s
    val potential = inp.replace("?", ".").split(".").filter { it.isNotEmpty() }.map { it.length }
    return inp.count { it == '#' } <= desc.sum() &&
            potential.size <= desc.size && potential.zip(desc).all { (pot, desc) -> pot <= desc }
}

fun isValid(s: Pair<String, List<Int>>): Boolean {
    val (inp, desc) = s
    return !inp.contains('?') && inp.split(".").filter { it.isNotEmpty() }.map { it.length } == desc
}

fun scanput(str: String, counts: List<Int>, n: Int): Int {
    // kill branch early if it's NOK

    if(!isPossible(str to counts)) return 0

    if (n == str.length) return if (isValid(str to counts)) 1 else 0

    return if (str[n] == '?') {
        scanput(str.replaceRange(n, n + 1, "#"), counts, n + 1) +
        scanput(str.replaceRange(n, n + 1, "."), counts, n + 1)
    } else {
        scanput(str, counts, n + 1)
    }
}

fun main() {
    Path("src/main/resources/12.txt").readLines().map { txtToSprings(it) }.also {
//            it.sumOf { (inp, desc) -> scanput(inp, desc, 0) }.also { println("Part 1: $it") }
        }.also {
            it.map { (inp, blocks) -> inp.repeat(5) to List(5) { blocks }.flatten() }
                .minBy { (inp, blocks) -> inp.length }
                .also { println(it) }
                .let { (inp, blocks) -> scanput(inp, blocks, 0) }
                .also { println(it) }
        }

}

