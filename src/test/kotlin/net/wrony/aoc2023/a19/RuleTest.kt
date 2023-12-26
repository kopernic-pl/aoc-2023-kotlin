package net.wrony.aoc2023.a19

import kotlin.test.*


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


    @Test
    fun shouldSubtractRanges() {
        val inRange = 1..4000
        assertEquals(1..332, inRange.remove(333..4000))
    }

    @Test
    fun shouldSubtractRangesPart2() {
        val inRange = 1..4000
        assertEquals(457..4000, inRange.remove(1..456))
    }

    @Test
    fun shouldSubtractRangesPart3() {
        val inRange = 234..1234
        assertEquals(457..1234, inRange.remove(1..456))
    }

    @Test
    fun shouldCalcOverlap(){
        val inRange = 1..4000
        assertEquals(1..456, inRange.overlap(1..456))
    }

    @Test
    fun shouldCalcOverlap2(){
        val inRange = 1..3000
        assertEquals(2500..3000, inRange.overlap(2500..4000))
    }

    @Test
    fun shouldCalcOverlap3(){
        val inRange = 200..500
        assertEquals(200..299, inRange.overlap(1..299))
    }

    @Test
    fun testingNarrowDown() {
        val maxSize = 4000
        narrowDown(
            Rule("name", listOf("x<1000:A", "m<333:B", "a>400:C", "s>2000:D", "default")),
            Range4d(1..maxSize, 1..maxSize, 1..maxSize, 1..maxSize), maxSize
        ).let {
            assertEquals("A", it.first().first)
            assertEquals(Range4d(1..999, 1..maxSize, 1..maxSize, 1..maxSize), it.first().second)

            assertEquals("default", it.last().first)
            assertEquals(Range4d(1000..maxSize, 333..maxSize, 1..400, 1..2000), it.last().second)
        }
    }
}