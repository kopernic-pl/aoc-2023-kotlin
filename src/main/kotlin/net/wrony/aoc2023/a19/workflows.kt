package net.wrony.aoc2023.a19

import arrow.core.Tuple4
import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Rule(val name: String, val rules: List<String>) {

    val default = rules.last()

    fun match(p: Part): String {
        return rules.dropLast(1).firstOrNull { r ->
            val toCompare = when {
                r.startsWith("x") -> p.x
                r.startsWith("m") -> p.m
                r.startsWith("a") -> p.a
                r.startsWith("s") -> p.s
                else -> throw IllegalArgumentException("Unknown rule $r")
            }
            val comparator: (Int, Int) -> Boolean = when {
                r.contains("<") -> { a, b -> a < b }
                r.contains(">") -> { a, b -> a > b }
                else -> throw IllegalArgumentException("Unknown rule $r")
            }
            val value = r.split(":").first().drop(2).toInt()
            comparator(toCompare, value)
        }?.split(':')?.last() ?: default
    }

    companion object {
        fun parse(str: String): Rule {
            str.dropLast(1).split('{').let { (name, rules) ->
                return Rule(name, rules.split(','))
            }
        }
    }
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {

    fun rating(): Int {
        return x + m + a + s
    }

    companion object {
        fun parse(str: String): Part {
            val (x, m, a, s) = str.drop(1).dropLast(1).split(",")
                .map { it.split("=").last().toInt() }
            return Part(x, m, a, s)
        }
    }
}

typealias Range4d = Tuple4<IntRange, IntRange, IntRange, IntRange>

fun Range4d.power(): ULong {
    val (x, m, a, s) = this
    return x.count().toULong() * m.count().toULong() * a.count().toULong() * s.count().toULong()
}

fun IntRange.remove(other: IntRange): IntRange {
    return when {
        this.first < other.first -> {
            this.first..<other.first
        }

        this.last > other.last -> {
            other.last + 1..this.last
        }

        else -> {
            throw IllegalArgumentException("Cannot remove $other from $this")
        }
    }
}

fun IntRange.overlap(other: IntRange): IntRange {
    return when {
        this.first < other.first -> {
            other.first..this.last
        }

        this.last > other.last -> {
            this.first..other.last
        }

        else -> {
            throw IllegalArgumentException("Cannot remove $other from $this")
        }
    }
}

fun narrowDown(rule: Rule, inRange: Range4d, maxSize: Int): Set<Pair<String, Range4d>> {
    val out = mutableSetOf<Pair<String, Range4d>>()
    val rules = rule.rules

    rules.fold(inRange) { range, r ->
        when {
            r.contains(':') -> {
                val (matcher, target) = r.split(':')
                val reference = matcher.drop(2).toInt()

                val matchingRange = when {
                    matcher.contains("<") -> {
                        0..<reference
                    }

                    matcher.contains(">") -> {
                        reference + 1..maxSize
                    }

                    else -> throw IllegalArgumentException("Unknown rule $r")
                }

                val (x, m, a, s) = range

                val (narrowedRangeForCurrentTarget, narrowedRangeForNextStep) = when (matcher.first()) {
                    'x' -> {
                        range.copy(first = x.overlap(matchingRange)) to range.copy(first = x.remove(matchingRange))
                    }

                    'm' -> {
                        range.copy(second = m.overlap(matchingRange)) to range.copy(second = m.remove(matchingRange))
                    }

                    'a' -> {
                        range.copy(third = a.overlap(matchingRange)) to range.copy(third = a.remove(matchingRange))
                    }

                    's' -> {
                        range.copy(fourth = s.overlap(matchingRange)) to range.copy(fourth = s.remove(matchingRange))
                    }

                    else -> throw IllegalArgumentException("Unknown rule $r")
                }
                out.add(target to narrowedRangeForCurrentTarget)

                narrowedRangeForNextStep
            }

            else -> {
                out.add(r to range)
                range
            }
        }
    }
    return out
}

fun main() {
    Path("src/main/resources/19.txt").readLines().let { list ->
        // find index of line that is empty
        list.indexOfFirst { it.isBlank() }.let { idx ->
            // split into rules and elements
            list.subList(0, idx).map(Rule::parse).associateBy { it.name } to list.subList(
                idx + 1, list.size
            ).map(Part::parse)
        }
    }.also { (rules, parts) ->
        // load elements from rules list to map by rule name

        parts.groupBy { el ->
            var currentRule = "in"

            while (true) {
                val rule = rules[currentRule]!!
                val res = rule.match(el)
                if (res == "A" || res == "R") {
                    return@groupBy res
                }
                currentRule = res
            }
        }["A"]!!.sumOf { it.rating() }.let { println("Part 1: $it") }
    }.let { (rules, _) ->
        val maxSize = 4000
        val r = Range4d(1..maxSize, 1..maxSize, 1..maxSize, 1..maxSize)

        val state = narrowDown(rules["in"]!!, r, maxSize).toMutableSet()
        val accepted = mutableSetOf<Range4d>()

        while (state.isNotEmpty()) {
            val e = state.first()
            state.remove(e)
            val newRules = narrowDown(rules[e.first]!!, e.second, maxSize)

            accepted.addAll(newRules.filter { (target, _) -> target == "A" }.map { it.second })

            state.addAll(newRules.filter { (target, _) -> target != "R" && target != "A" })
        }
        println("Part 2: ${accepted.sumOf { range -> range.power() }}")


    }.let { println(it) }
}