package net.wrony.aoc2023.a11

import kotlin.test.Test
import kotlin.test.assertEquals

class _11KtTest {

    @Test
    fun `side neighbour is range 1`() {
        val sideNeighbours = (0 to 0) to (0 to 1)
        assertEquals(1, discreteDistance(sideNeighbours))
    }

    @Test
    fun `diagonal neighbour is range 2`() {
        val sideNeighbours = (0 to 0) to (1 to 1)
        assertEquals(2, discreteDistance(sideNeighbours))
    }

    @Test
    fun `long range neighbor`() {
        val sideNeighbours = (0 to 0) to (12 to 0)
        assertEquals(12, discreteDistance(sideNeighbours))
    }

    @Test
    fun `row neighbor`() {
        val sideNeighbours = (11 to 0) to (11 to 5)
        assertEquals(5, discreteDistance(sideNeighbours))
    }

    @Test
    fun `some far neighbor range`() {
        val sideNeighbours = (0 to 4) to (10 to 9)
        assertEquals(15, discreteDistance(sideNeighbours))
    }

    @Test
    fun `some other far neighbor range`() {
        val sideNeighbours = (2 to 0) to (7 to 12)
        assertEquals(17, discreteDistance(sideNeighbours))
    }
}

