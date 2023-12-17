package net.wrony.aoc2023.a16

import net.wrony.aoc2023.a10.Direction
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.random.Random


typealias Position = Pair<Int, Int>

data class LightThread(var pos: Position, var dir: Direction) {
    var dead = false
}

fun Array<BooleanArray>.countEnergized(): Int {
    return this.sumOf { it.count { it } }
}

fun main() {

    Path("src/main/resources/16.txt").readLines().map { it.toCharArray() }.toTypedArray()
        .let { grid ->
            val threads = mutableListOf(LightThread(Pair(0, 0), Direction.EAST))
            val powerFlow = Array(grid.size) { BooleanArray(grid.size) }
            var newThreads = mutableListOf<LightThread>()

            val splittersUse = mutableSetOf<Position>()

            while(threads.size > 0) {
                threads.removeIf { it.dead }
                threads.addAll(newThreads)
                newThreads = mutableListOf()

                threads.forEach { thread ->
                    if (thread.pos.first < 0 || thread.pos.first >= grid.size || thread.pos.second < 0 || thread.pos.second >= grid.size) {

                        thread.dead = true
                        return@forEach
                    }

                    powerFlow[thread.pos.first][thread.pos.second] = true

                    when (grid[thread.pos.first][thread.pos.second]) {
                        '/' -> {
                            thread.dir = when (thread.dir) {
                                Direction.EAST -> Direction.NORTH
                                Direction.NORTH -> Direction.EAST
                                Direction.WEST -> Direction.SOUTH
                                Direction.SOUTH -> Direction.WEST
                            }
                            thread.pos = thread.dir.transposition(thread.pos)
                        }

                        '\\' -> {
                            thread.dir = when (thread.dir) {
                                Direction.EAST -> Direction.SOUTH
                                Direction.SOUTH -> Direction.EAST
                                Direction.WEST -> Direction.NORTH
                                Direction.NORTH -> Direction.WEST
                            }
                            thread.pos = thread.dir.transposition(thread.pos)
                        }

                        '|' -> {
                            when (thread.dir) {
                                Direction.NORTH, Direction.SOUTH -> {
                                    thread.pos = thread.dir.transposition(thread.pos)
                                }

                                else -> {
                                    thread.dead = true
                                    if (!splittersUse.contains(thread.pos)) {
                                        newThreads.addAll(
                                            listOf(
                                                LightThread(
                                                    Direction.NORTH.transposition(
                                                        thread.pos
                                                    ), Direction.NORTH
                                                ), LightThread(
                                                    Direction.SOUTH.transposition(
                                                        thread.pos
                                                    ), Direction.SOUTH
                                                )
                                            )
                                        )
                                        splittersUse.add(thread.pos)
                                    }
                                }
                            }
                        }

                        '-' -> {
                            when (thread.dir) {
                                Direction.EAST, Direction.WEST -> {
                                    thread.pos = thread.dir.transposition(thread.pos)
                                }

                                else -> {
                                    thread.dead = true
                                    if (!splittersUse.contains(thread.pos)) {
                                        newThreads.addAll(
                                            listOf(
                                                LightThread(
                                                    Direction.EAST.transposition(
                                                        thread.pos
                                                    ), Direction.EAST
                                                ), LightThread(
                                                    Direction.WEST.transposition(
                                                        thread.pos
                                                    ), Direction.WEST
                                                )
                                            )
                                        )
                                        splittersUse.add(thread.pos)
                                    }
                                }
                            }
                        }

                        '.' -> {
                            thread.pos = thread.dir.transposition(thread.pos)
                        }
                    }
                }
                println("Nb of energized: ${powerFlow.countEnergized()}, threads: ${threads.size}")
            }
        }
}