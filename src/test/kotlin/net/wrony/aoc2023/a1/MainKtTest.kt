package net.wrony.aoc2023.a1

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class MainKtTest {

    @Test
    fun `solution one should work for small input`() {
        val input1 = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent()
        assertEquals(142, solution1a(input1))
    }

    @Test
    fun `solution two should work for small input`() {
        val input1 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent()
        assertEquals(281, solution1b(input1))
    }

}