package net.wrony.aoc2023.a21

import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.roundToLong

fun main() {
    Path("src/main/resources/21.txt").readLines()
        .also {
            it.map { it.toCharArray() }.toTypedArray()
            .also {solve(it, 64)}
        }
        .also { input ->
            val basicSize = input.size

            val stepsNeeded = 26501365L
            val mapRepetitions = (stepsNeeded - 65) / basicSize

            val reps = 5
            val map = input.repeated(reps)

            println("Map size: ${map[0].length}, repetitions: $mapRepetitions")

            val y1 = solve(map.map { it.toCharArray() }.toTypedArray(), 65)
            val y2 = solve(map.map { it.toCharArray() }.toTypedArray(), 65 + 131)
            val y3 = solve(map.map { it.toCharArray() }.toTypedArray(), 65 + 2 * 131)

            val points = WeightedObservedPoints()
            points.add(0.0, y1.toDouble())
            points.add(1.0, y2.toDouble())
            points.add(2.0, y3.toDouble())

            val fitter = PolynomialCurveFitter.create(2)
            val solution = fitter.fit(points.toList())

            val longSol = solution.map { it.roundToLong() }.also { println(it) }

            println(longSol[0] + longSol[1] * mapRepetitions + longSol[2] * mapRepetitions * mapRepetitions)

            // 637039940429529 -
            // 637080865517230 -

            // 012
            // 637087163925555 ok
        }
}

private fun solve(map: Array<CharArray>, stepsToTake: Int): Int {
    val ranges = Array(map.size) { Array(map.size) { mutableSetOf<Int>() } }

    val startPos =
        map.size /2 to map.size /2

    val stack = ArrayDeque<Pair<Pair<Int, Int>, Int>>(1024 * 1024)
    stack.add(startPos to 0)
    ranges[startPos.first][startPos.second].add(0)

    while (stack.isNotEmpty()) {
        val newPosToCheck = wander(stack.removeLast(), map)

        newPosToCheck.shuffled().forEach { (pos, step) ->
            if (step <= stepsToTake && ranges[pos.first][pos.second].add(step)) {
                stack.add(pos to step)
            }
        }
    }
    return ranges.flatten().filter { it.contains(stepsToTake) }.size.also { println(it) }
}

fun List<String>.repeated(i: Int): List<String> {
    return this.map { l -> (1 ..< i).fold(l) { acc, _ -> acc + l.replace('S','.')} }
        .let { lines -> (1..<i).fold(lines) { acc, _ -> acc + lines }
}}

val directions = setOf('N', 'S', 'E', 'W')

fun wander(pos: Pair<Pair<Int, Int>, Int>, map: Array<CharArray>): Set<Pair<Pair<Int, Int>, Int>> {
    val (position, step) = pos

    val (row, col) = position
    val (rows, cols) = map.size to map[0].size

    var possibilities = directions
    //if N impossible, remove N
    if (row == 0 || map[row - 1][col] == '#') possibilities = possibilities - ('N')
    //if S impossible, remove S
    if (row == rows - 1 || map[row + 1][col] == '#') possibilities = possibilities - ('S')
    //if E impossible, remove E
    if (col == 0 || map[row][col - 1] == '#') possibilities = possibilities - ('W')
    //if W impossible, remove W
    if (col == cols - 1 || map[row][col + 1] == '#') possibilities = possibilities - ('E')

    return possibilities.map {
        when (it) {
            'N' -> Pair(row - 1 to col, step + 1)
            'S' -> Pair(row + 1 to col, step + 1)
            'E' -> Pair(row to col + 1, step + 1)
            'W' -> Pair(row to col - 1, step + 1)
            else -> throw Exception("Unknown direction $it")
        }
    }.toSet()
}