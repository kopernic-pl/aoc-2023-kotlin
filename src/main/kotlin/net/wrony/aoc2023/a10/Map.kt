package net.wrony.aoc2023.a10

import net.wrony.aoc2023.a10.MapElements.Companion.fromChar
import kotlin.io.path.Path
import kotlin.io.path.readText


enum class Direction(val transposition: (Pair<Int, Int>) -> Pair<Int, Int>) {
    NORTH({(r,c)->Pair(r-1,c)}),
    EAST({(r,c)->Pair(r,c+1)}),
    SOUTH({(r,c)->Pair(r+1,c)}),
    WEST({(r,c)->Pair(r,c-1)});

    fun opposite(): Direction {
        return when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }
}

enum class MapElements(val track: (Direction) -> Direction) {
    EMPTY({ _ -> throw IllegalArgumentException("Cannot move in empty space") }), START({ _ ->
        throw IllegalArgumentException(
            "Should not move through start"
        )
    }),
    PIPE_VERTICAL({ d ->
        when (d) {
            Direction.NORTH -> Direction.SOUTH
            Direction.SOUTH -> Direction.NORTH
            else -> throw IllegalArgumentException("Impossible move in vertical pipe")
        }
    }),
    PIPE_HORIZONTAL({ d ->
        when (d) {
            Direction.EAST -> Direction.WEST
            Direction.WEST -> Direction.EAST
            else -> throw IllegalArgumentException("Impossible move in horizontal pipe")
        }
    }),
    PIPE_NORTH_EAST({ d ->
        when (d) {
            Direction.NORTH -> Direction.EAST
            Direction.EAST -> Direction.NORTH
            else -> throw IllegalArgumentException("Impossible move in NE pipe")
        }
    }),
    PIPE_NORTH_WEST({ d ->
        when (d) {
            Direction.NORTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
            else -> throw IllegalArgumentException("Impossible move in NW pipe")
        }
    }),
    PIPE_SOUTH_WEST({ d ->
        when (d) {
            Direction.WEST -> Direction.SOUTH
            Direction.SOUTH -> Direction.WEST
            else -> throw IllegalArgumentException("Impossible move in SW pipe")
        }
    }),
    PIPE_SOUTH_EAST({ d ->
        when (d) {
            Direction.EAST -> Direction.SOUTH
            Direction.SOUTH -> Direction.EAST
            else -> throw IllegalArgumentException("Impossible move in SE pipe")
        }
    });

    companion object {
        fun fromChar(char: Char): MapElements {
            return when (char) {
                '.' -> EMPTY
                'S' -> START
                '|' -> PIPE_VERTICAL
                '-' -> PIPE_HORIZONTAL
                'L' -> PIPE_NORTH_EAST
                'J' -> PIPE_NORTH_WEST
                '7' -> PIPE_SOUTH_WEST
                'F' -> PIPE_SOUTH_EAST
                else -> throw IllegalArgumentException("Unknown char $char")
            }
        }
    }
}

fun main() {
    fun findStartLetterS(arr: Array<Array<MapElements>>): Pair<Int, Int> {
        return arr.indices.flatMap { row ->
            arr[row].indices.map { col ->
                Pair(row, col)
            }
        }.first { coord: Pair<Int, Int> -> arr[coord.first][coord.second] == MapElements.START }
    }

    Path("src/main/resources/10.txt").readText().lines().let {
        val size = it[0].length
        size to Array(size) { i -> it[i].toCharArray() }
    }.let { (size, arr) ->
        size to arr.map { row -> row.map(::fromChar).toTypedArray() }.toTypedArray()
    }.let { (_, emap) ->
        val start = findStartLetterS(emap)
        println(start)
        //start going east, from West
        var currPos = start
        var goingTo = Direction.EAST
        var stepCount = 0
        val visited = mutableSetOf<Pair<Int, Int>>(currPos)
        while (true) {
            currPos = goingTo.transposition(currPos)
            stepCount++
            visited.add(currPos)

            goingTo = emap[currPos.first][currPos.second].track(goingTo.opposite())
            if (start == goingTo.transposition(currPos)) {
                println("found start again, after $stepCount steps")
                break
            }
        }
        println("Part 1: ${(stepCount + 1) / 2}")
        emap to visited
    }.let { (emap, visited) ->
        var cnt = 0
        for (r in emap.indices) {
            var det = 0
            for (c in emap[r].indices) {
                if (!visited.contains(r to c)) {
                    if (det > 0 && det%2==1) {
                        cnt++
                    }
                } else {
                    if(isVert(emap[r][c])) {
                        det++
                    }
                }
            }
        }
        cnt
    }.let { println("Part 2: $it")}
}

fun isVert(mapElements: MapElements): Boolean {
    return setOf(MapElements.PIPE_VERTICAL, MapElements.PIPE_SOUTH_EAST, MapElements.PIPE_SOUTH_WEST).contains(mapElements)
}
