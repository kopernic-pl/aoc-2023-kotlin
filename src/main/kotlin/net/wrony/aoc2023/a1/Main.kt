package net.wrony.aoc2023.a1

import arrow.core.flatten
import kotlin.io.path.Path
import kotlin.io.path.readText


fun solution1a(input: String): Int {
    val lines = arrayListOf<Int>()

    fun findFirstDigit(s: String): Int = s.first { it.isDigit() }.digitToInt()
    fun findLastDigit(s: String): Int = s.last { it.isDigit() }.digitToInt()

    input.lines().forEach { lines.add(findFirstDigit(it) * 10 + findLastDigit(it)) }

    return lines.sum()
}

fun solution1b(input: String): Int {
    val wordsanddigits2digits: Map<String, Int> = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9
    )

    fun wordPositions(s: String): List<Pair<Int, Int>> {
        return wordsanddigits2digits.map { (word, digit) ->
            Regex(word).findAll(s).map { it.range.first to digit }.toList()
        }.flatten().sortedBy { it.first }
    }

    return input.lines().map { s -> s to wordPositions(s) }
        .map { (s, positions) -> s to positions.map { it.second } }
        .map { (s, digits) -> s to 10 * digits.first() + digits.last() }.sumOf { it.second }
}

fun main() {
    println(solution1a(Path("src/main/resources/1.txt").readText()))
    println(solution1b(Path("src/main/resources/1.txt").readText()))
}


