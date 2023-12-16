package net.wrony.aoc2023.a15

import kotlin.io.path.Path
import kotlin.io.path.readText


fun String.hASH(): Int {
    return this.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }
}

sealed class Operation(val element: String) {
    class INSERT(element: String, val focal: Int) : Operation(element) {
        fun toLens() = Lens(element, focal)
    }

    class REMOVE(element: String) : Operation(element)

    fun hASH() = element.hASH()
}

typealias Lens = Pair<String, Int>

fun initMap(cap: Int): Map<Int, MutableList<Lens>> {
    return buildMap(cap) {
        (0..255).forEach {
            put(it, mutableListOf())
        }
    }
}

fun main() {
    Path("src/main/resources/15.txt").readText().split(",").also {
        println(it.sumOf(String::hASH))
    }.let { commands ->
        commands.map { c ->
            when {
                c.contains('-') -> Operation.REMOVE(c.dropLast(1))
                c.contains('=') -> c.split("=")
                    .let { (element, focal) -> Operation.INSERT(element, focal.toInt()) }

                else -> throw Exception("Unknown command $c")
            }
        }.fold(initMap(commands.size)) { acc, operation ->
            when (operation) {
                is Operation.REMOVE -> acc[operation.hASH()]!!.removeIf { l -> l.first == operation.element }
                    .let { acc }

                is Operation.INSERT -> acc[operation.hASH()]!!.let { lenses ->
                    if (lenses.any { l -> l.first == operation.element }) {
                        lenses.replaceAll { l ->
                            if (l.first == operation.element) {
                                operation.toLens()
                            } else {
                                l
                            }
                        }
                    } else {
                        lenses.add(operation.toLens())
                    }
                    acc
                }
            }
        }.filter { (_, lenses) -> lenses.isNotEmpty() }
        .let {
            m -> m.keys.fold(0) { acc, boxKey ->
                acc + m[boxKey]!!.foldIndexed(0) {
                    idx, boxAcc, lens -> boxAcc + (boxKey+1) * (idx+1) * lens.second
                }
            }
        }.let { println("Second answer: $it") }
    }
}