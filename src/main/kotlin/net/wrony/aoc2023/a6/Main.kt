package net.wrony.aoc2023.a6

import arrow.core.flatten
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    Path("src/main/resources/6.txt").readLines()
        .map { it.replace("\\s+".toRegex(), " ")  }
        .map { it.split(":").map { it.trim() }.drop(1) }.flatten()
        .map { it.split(" ").map { it.toShort()} }
        .let { (time, dist) -> time.zip(dist) }
        .map { (time, dist) -> (1..<time).map { n -> (time - n) * n}.filter { it > dist }}
        .map { it.size }
        .reduce(Int::times)
        .let { println(it) }

    Path("src/main/resources/6.txt").readLines()
        .map { it.replace("\\s+".toRegex(), " ")  }
        .map { it.split(":").map { it.trim() }.drop(1) }.flatten()
        .map { it.replace(" ", "").toLong() }
        .let { (time, dist) -> (1..<time).map { n -> (time - n) * n}.filter { it > dist }}
        .size
//        .reduce(Int::times)
        .let { println(it) }
}