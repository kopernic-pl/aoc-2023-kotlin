package net.wrony.aoc2023.a19

import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Rule(val name: String, val rules: List<String>) {

    fun match(p: Part): String {
        val default = rules.last()
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
            val (x, m, a, s) = str.drop(1).dropLast(1).split(",").map { it.split("=").last().toInt() }
            return Part(x, m, a, s)
        }
    }
}
fun main() {
    Path("src/main/resources/19.txt").readLines().let {list ->
        // find index of line that is empty
        list.indexOfFirst { it.isBlank() }.let { idx ->
            // split into rules and elements
            list.subList(0, idx).map(Rule::parse) to list.subList(idx + 1, list.size).map(Part::parse)
        }
    }.also {
        (rules, parts) ->
        // load elements from rules list to map by rule name
        val rulesMap = rules.associateBy { it.name }

        parts.groupBy {el ->
            var currentRule = "in"

            while (true) {
                val rule = rulesMap[currentRule]!!
                val res = rule.match(el)
                if (res == "A" || res == "R") {
                    println("Element $el accepted or rejected with $res")
                    return@groupBy res
                }
                currentRule = res
            }
        }["A"]!!.sumOf { it.rating() }.let { println("Part 1: $it") }
    }
        //.also { () }
}