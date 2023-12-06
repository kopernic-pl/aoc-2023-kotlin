package net.wrony.aoc2023.a5

import kotlin.io.path.Path
import kotlin.io.path.readText

data class MappingRange(val destStart: Long, val sourceStart: Long, val length: Long) {
    fun mapsValue(l: Long): Boolean = l in sourceStart..<sourceStart + length
    fun mapSourceToDestVal(l: Long): Long {
        return destStart + (l - sourceStart)
    }
}

fun main() {
    fun listMappings(groups: List<List<String>>): List<List<MappingRange>> =
        groups.drop(1).map { map ->
            map.drop(1).map { mapping -> mapping.split(" ").map { it.toLong() } }
                .map { (a, b, c) -> MappingRange(a, b, c) }
        }

    Path("src/main/resources/5.txt").readText().let { input ->
        input.lines()[0]
        input.lines().mapIndexed { idx, l -> idx to l.isEmpty() }.filter { (idx, empty) -> !empty }
            .map { (idx, _) -> idx }

        var counter = 0
        input.lines().groupBy {
            if (it.isBlank()) counter++
            counter
        }.values.map { group -> group.filter { it.isNotBlank() && it.isNotEmpty() } }
            .also { groups ->
                val seeds = groups[0][0].removePrefix("seeds: ").split(" ").map { it.toLong() }
                val maps = listMappings(groups)
                seeds.minOfOrNull { seed ->
                    maps.fold(seed) { acc, ranges ->
                        ranges.find { it.mapsValue(acc) }?.mapSourceToDestVal(acc) ?: acc
                    }
                }.let { println(it) }
            }.also { groups ->
                val seedRanges =
                    groups[0][0].removePrefix("seeds: ").split(" ").map { it.toLong() }.asSequence()
                        .chunked(2).map { (nb, size) -> nb..<(nb + size) }.sortedBy { r -> r.last - r.first }.toList()

                val maps = listMappings(groups)
                var minLoc = Long.MAX_VALUE
                println()
                println(seedRanges.toList())
                seedRanges
                    .asSequence()
                    .forEachIndexed { rangeIdx, seedRange ->
                        println("Range $seedRange")
                        seedRange.forEach { seed ->
                            when (rangeIdx) {
                                0 -> {
                                    val loc = maps.fold(seed) { acc, ranges ->
                                        ranges.find { it.mapsValue(acc) }?.mapSourceToDestVal(acc)
                                            ?: acc
                                    }
                                    if (loc < minLoc) {
                                        minLoc = loc; println("Min seed $seed")
                                    }
                                }

                                else -> {
                                    if (!(seedRanges.subList(0, rangeIdx - 1)
                                            .any { it.contains(seed) })
                                    ) {
                                        val loc = maps.fold(seed) { acc, ranges ->
                                            ranges.find { it.mapsValue(acc) }
                                                ?.mapSourceToDestVal(acc) ?: acc
                                        }
                                        if (loc < minLoc) {
                                            minLoc = loc; println("Min seed $seed, loc $loc")
                                        }
                                    }
                                }
                            }

                        }
                    }
                println(minLoc)
            }
//194601276 -- too high
//108956227 OK
    }
}