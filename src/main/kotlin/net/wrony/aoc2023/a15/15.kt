package net.wrony.aoc2023.a15

import kotlin.io.path.Path
import kotlin.io.path.readText


fun String.hASH(): Int {
    return this.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }
}

fun main() {
    Path("src/main/resources/15.txt").readText().split(",").also {
            it.map(String::hASH).sum().let { sum -> println(sum) }
        }

}