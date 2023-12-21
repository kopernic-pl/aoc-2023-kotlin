package net.wrony.aoc2023.a13

import kotlin.io.path.Path
import kotlin.io.path.readLines


fun gradeSymmetry(r: List<String>, pos: Int, len: Int): Int {
    return r.subList(pos - len + 1, pos + 1).zip(r.subList(pos + 1, pos + len + 1).reversed())
        .sumOf { (str1, str2) -> str1.zip(str2).count { (c1, c2) -> c1 != c2 } }
}

fun findSymmetryAxis(lines: List<String>, accuracyNeeded: Int): Int? {
    return (0..<lines.size - 1).map { pos ->
        val lenToCompare = minOf(pos + 1, lines.size - pos - 1)
        pos to gradeSymmetry(lines, pos, lenToCompare)
    }.firstOrNull { (_, diff) -> diff == accuracyNeeded }
        ?.let { (pos, _) -> pos }
}

fun main() {
    Path("src/main/resources/13.txt").readLines()
        .fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(line)
            }
            acc
        }.map { it.toList() }.toList().map { pattern ->
            pattern to pattern[0].indices.fold(
                listOf<String>()
            ) { acc, idx ->
                acc + pattern.map { row -> row[idx] }.joinToString("")
            }
        }.also {
            it.sumOf { (rows, cols) ->
                val r = findSymmetryAxis(rows, 0)
                val c = findSymmetryAxis(cols, 0)


                (c?.let { c + 1 } ?: 0) + (r?.let { (r + 1) * 100 } ?: 0)
            }.let { r -> println("Part 1: $r") }
        }.also {
            it.sumOf { (rows, cols) ->
                val r = findSymmetryAxis(rows, 1)
                val c = findSymmetryAxis(cols, 1)


                (c?.let { c + 1 } ?: 0) + (r?.let { (r + 1) * 100 } ?: 0)
            }.let { r -> println("Part 2: $r") }
        }
}