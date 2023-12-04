package net.wrony.aoc2023.a2

import kotlin.io.path.Path
import kotlin.io.path.readText

data class Draw(val reds: Int, val greens: Int, val blues: Int) {
    constructor(): this(0,0,0)
    operator fun plus(other: Draw): Draw =
        Draw(reds + other.reds, greens + other.greens, blues + other.blues)

    fun power(): Int = reds * greens * blues

    override fun toString(): String = "|r$reds, g$greens, b$blues|"
}
data class Game(val id: Int, val draws: List<Draw>) {
    fun isPossible(reds: Int, greens: Int, blues: Int): Boolean {
        return draws.all { it.reds<= reds && it.greens <= greens && it.blues <= blues}
    }

    fun fewestCubes(): Draw {
        return Draw(draws.maxOf { it.reds }, draws.maxOf { it.greens }, draws.maxOf { it.blues })
    }
}

fun lineToGameAndDraws(line: String) : Pair<Int, List<List<String>>> {
    return line.split(": ")
        .let { (gamePart, drawsPart) ->
            gamePart.removePrefix("Game ").toInt() to drawsPart.split("; ").map { it.split(", ") }
        }
}

fun parseDraw(draw: List<String>): Draw {
    return draw.map {
        val (count, color) = it.split(" ")
        when (color) {
            "red" -> Draw(count.toInt(), 0, 0)
            "green" -> Draw(0, count.toInt(), 0)
            "blue" -> Draw(0, 0, count.toInt())
            else -> throw Exception("Unknown color $color")
        }
    }.fold(Draw()) { acc, d -> acc + d }
}

fun main() {
    println (
        Path("src/main/resources/2.txt").readText().lineSequence()
        .map { lineToGameAndDraws(it) }
        .map { (gameId, draws) -> Game(gameId, draws.map { parseDraw(it) }) }
        .filter { it.isPossible(12, 13, 14) }
        .sumOf { it.id }
    )

    //tried 279 - X
    //tried 2416 - OK

    println (
        Path("src/main/resources/2.txt").readText().lineSequence()
            .map { lineToGameAndDraws(it) }
            .map { (gameId, draws) -> Game(gameId, draws.map { parseDraw(it) }) }
            .map { it.fewestCubes() }
            .sumOf { it.power() }
    )

    //tried 63307 - OK
}