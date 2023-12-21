package net.wrony.aoc2023.a12

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class RiddleKtTest {

    @Test
    fun shouldValidate(){
        assertTrue(isValid("######" to listOf(6)))
        assertTrue(isValid("######." to listOf(6)))
        assertTrue(isValid(".######" to listOf(6)))
    }
}