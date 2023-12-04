package net.wrony.aoc2023.a3

import kotlin.io.path.Path
import kotlin.io.path.readText

data class PartNumber(val row: Int, val range: IntRange, val value: Int) {
    fun partNeighbourhood(size: Int): Sequence<Pair<Int, Int>> {
        return sequence {
            yield(Pair(row, range.first - 1))
            yield(Pair(row, range.last + 1))
            yieldAll((range.first - 1..range.last + 1).map { Pair(row - 1, it) })
            yieldAll((range.first - 1..range.last + 1).map { Pair(row + 1, it) })
        }.filter { it.first >= 0 && it.second >= 0 && it.first < size && it.second < size }
    }

    fun isInNeighbourhood(size: Int, row: Int, col: Int): Boolean {
        return partNeighbourhood(size).any { it.first == row && it.second == col }
    }
}

fun isFakePart(part: PartNumber, a: Array<CharArray>): Boolean {
    val neighbours = part.partNeighbourhood(a.size)
    return neighbours.all { a[it.first][it.second].isDigit() || a[it.first][it.second] == '.' }
}

fun findGears(a: Array<CharArray>): Sequence<Pair<Int, Int>> {
    return sequence {
        yieldAll(a.indices.flatMap { row ->
            a[row].indices.map { col ->
                Pair(row, col)
            }
        }.filter { a[it.first][it.second] == '*' })
    }
}

fun main() {
    val input = Path("src/main/resources/3.txt").readText().lines()
    val size = input[0].length
    val a = Array(size) { i -> input[i].toCharArray() }

    val allParts = input.flatMapIndexed { lineIdx, line ->
        Regex("\\d+").findAll(line).map { PartNumber(lineIdx, it.range, it.value.toInt()) }
    }

    println("Sum of real parts on the schematic: " + allParts.filter { !isFakePart(it, a) }
        .sumOf { it.value })

    val allPossibleGearPositions = findGears(a)

    fun hasExactlyTwoParts(gearPosition: Pair<Int, Int>): Boolean {
        return allParts.count {
            it.isInNeighbourhood(
                size, gearPosition.first, gearPosition.second
            )
        } == 2
    }

    fun getTwoPartsOfGear(gearPosition: Pair<Int, Int>): List<PartNumber> {
        return allParts.filter {
            it.isInNeighbourhood(
                size, gearPosition.first, gearPosition.second
            )
        }.take(2)
    }

    println("Sum of real parts on the schematic: " + allPossibleGearPositions.filter {
            hasExactlyTwoParts(
                it
            )
        }.map { getTwoPartsOfGear(it) }.map { (first, second) -> first.value * second.value }.sum())
}