package net.wrony.aoc2023.a5

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class MappingRangeTest {

    @Test
    fun mapsOneToOneIfNotInRange() {
        val mappingRange = MappingRange(10, 20, 10)
        assertEquals(1, mappingRange.mapSourceToDestVal(1))
        assertEquals(10, mappingRange.mapSourceToDestVal(10))
        assertEquals(11, mappingRange.mapSourceToDestVal(11))
        assertEquals(19, mappingRange.mapSourceToDestVal(19))
        assertEquals(10, mappingRange.mapSourceToDestVal(20))
        assertEquals(11, mappingRange.mapSourceToDestVal(21))
        assertEquals(20, mappingRange.mapSourceToDestVal(30))
        assertEquals(31, mappingRange.mapSourceToDestVal(31))
    }
}