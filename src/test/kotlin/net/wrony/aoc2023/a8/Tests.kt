package net.wrony.aoc2023.a8

import kotlin.test.Test
import kotlin.test.assertEquals

class Tests {

    @Test
    fun factorize(){
        assertEquals(listOf(3L,5L,823L), factors(12345))
    }
}