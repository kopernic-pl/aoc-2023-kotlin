package net.wrony.aoc2023.a14

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class _14KtTest {

    @Test
    fun `should shift one O from end to start`() {
        val input = "........O".toCharArray()
        val expected = "O........".toCharArray()
        assertArrayEquals(expected, shiftTheRow(input))
    }

    @Test
    fun `should shift one O from end to first leaving hash where it was`() {
        val input = "........O#".toCharArray()
        val expected = "O........#".toCharArray()
        assertArrayEquals(expected, shiftTheRow(input))
    }

    @Test
    fun `should shift one O from end to the place after first hash`() {
        val input = "#.......O#.".toCharArray()
        val expected = "#O.......#.".toCharArray()
        assertArrayEquals(expected, shiftTheRow(input))
    }

    @Test
    fun `should shift one O from end to the place after hash on pos 4`() {
        val input = "...#....O#.".toCharArray()
        val expected = "...#O....#.".toCharArray()
        assertArrayEquals(expected, shiftTheRow(input))
    }

    @Test
    fun `should shift two O from end to the place after hash on pos 4`() {
        val input = "...#...OO#.".toCharArray()
        val expected = "...#OO...#.".toCharArray()
        assertArrayEquals(expected, shiftTheRow(input))
    }

    @Test
    fun `should rotate 2x2`() {
        val input = arrayOf(
            "AB".toCharArray(),
            "CD".toCharArray()
        )
        val expected = arrayOf(
            "CA".toCharArray(),
            "DB".toCharArray()
        )
        assertArrayEquals(expected, rotateClockwise(input))
    }

    @Test
    fun `should rotate 3x3`() {
        val input = arrayOf(
            "ABC".toCharArray(),
            "DEF".toCharArray(),
            "GHI".toCharArray()
        )
        val expected = arrayOf(
            "GDA".toCharArray(),
            "HEB".toCharArray(),
            "IFC".toCharArray()
        )
        assertArrayEquals(expected, rotateClockwise(input))
    }

}