package net.wrony.aoc2023.a11

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.absoluteValue


fun discreteDistance(p: Pair<Position, Position>): Long {
    return discreteDistance(p.first, p.second)
}

fun discreteDistance(first: Position, second: Position): Long {
    return discreteDistance(first.first.toLong(), first.second.toLong(), second.first.toLong(), second.second.toLong())
}

fun discreteDistance(x1: Long, y1: Long, x2: Long, y2: Long): Long {
    return (y2 - y1).absoluteValue + (x2 - x1).absoluteValue
}

fun calcSumOfAllDistances(stars: List<Position>): Long {
    return stars.flatMapIndexed { idx, star ->
        stars.drop(idx + 1).map { otherStar ->
            star to otherStar
        }
    }
        .sumOf { starsPair -> discreteDistance(starsPair) }
        .also { println("sumOf distances: $it") }
}

typealias Position = Pair<Int, Int>

fun main() {
    Path("src/main/resources/11.txt").readLines().let { rows ->
            val emptyRows = rows.foldIndexed(mutableSetOf<Int>()) { idx, acc, row ->
                    if (row.all { c -> c == '.' }) {
                        acc.add(idx)
                    }
                    acc
                }.sorted()
            val emptyCols = (0..<rows[0].length - 1).fold(mutableSetOf<Int>()) { acc, col ->
                    if (rows.all { row -> row[col] == '.' }) {
                        acc.add(col)
                    }
                    acc
                }.sorted()
            println("emptyRows: $emptyRows, emptyCols: $emptyCols")

            val originalStars = sequence {
                rows.indices.forEach { r ->
                    (0..<rows[r].length).forEach { c ->
                        if (rows[r][c] == '#') yield(Position(r, c))
                    }
                }
            }

            val expansionMultiplier = 1000000

            val expandedStars = originalStars.map {
                (row, col) ->
                (emptyRows.count { it < row } * (expansionMultiplier-1) + row) to (emptyCols.count { it < col } * (expansionMultiplier-1) + col)
            }


            calcSumOfAllDistances(expandedStars.toList())
        }
}