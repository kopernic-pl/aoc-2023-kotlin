package net.wrony.aoc2023.a4

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.pow

data class Card(val number: Int, val wNumbers: Set<Int>, val tries: Set<Int>) {
    fun worthPoints(): Long {
        wNumbers.intersect(tries).count().let {
            return when (it) {
                0 -> 0
                else -> 2.toDouble().pow(it - 1).toLong()
            }
        }
    }

    fun worthCards(): Int {
        wNumbers.intersect(tries).count().let { return it }
    }
}

fun lineToCard(line: String): Card {
    return line
        .replace("   ", " ")
        .replace("  ", " ")
        .split(": ").let { (card, numbers) ->
        val (wNumbers, tries) = numbers.split(" | ").let { (winningStr, tryStr) ->
            winningStr.split(" ").map { it.toInt() } to tryStr.split(" ").map { it.toInt() }
        }
        Card(
            Regex("Card (\\d+)").find(card)?.groupValues?.get(1)!!.toInt(),
            wNumbers.toHashSet(),
            tries.toHashSet()
        )
    }
}

fun main() {
    val lines = Path("src/main/resources/4.txt").readLines()
    lines.asSequence()
        .map { lineToCard(it) }
        .filter { it.worthPoints() > 0 }
        .sumOf { c -> c.worthPoints() }
        .let { println(it) }


    val countsOfCards = IntArray(lines.size + 1) { 1 }
    countsOfCards[0] = 0
    lines
        .map { lineToCard(it) }
        .forEach { c ->
            if (c.worthCards() > 0)
                (c.number + 1..c.number + c.worthCards()).forEach {
                    countsOfCards[it] = countsOfCards[it] + countsOfCards[c.number]
                }
        }
    println(countsOfCards.sum())
}