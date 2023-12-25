package net.wrony.aoc2023.a19

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test


class RuleTest {

    @Test
    fun shouldMatchDefault() {
        val part = Part(3, 2, 1, 0)
        val rule = Rule("test", listOf("x>4:A", "m>4:B", "a>4:C", "s>5:D", "default"))
        assertEquals("default", rule.match(part))
    }

    @Test
    fun shouldMatchFirst() {
        val part = Part(3, 2, 1, 0)
        val rule = Rule("test", listOf("x<4:A", "m>4:B", "a>4:C", "s>5:D", "default"))
        assertEquals("A", rule.match(part))
    }

    @Test
    fun shouldMatchSecond() {
        val part = Part(99, 6, 1, 0)
        val rule = Rule("test", listOf("x<4:A", "m>4:B", "a>4:C", "s>5:D", "default"))
        assertEquals("B", rule.match(part))
    }

    @Test
    fun shouldMatchLast() {
        val part = Part(99, 3, 1, 6)
        val rule = Rule("test", listOf("x<4:A", "m>4:B", "a>4:C", "s>5:D", "default"))
        assertEquals("D", rule.match(part))
    }
}