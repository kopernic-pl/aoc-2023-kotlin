package net.wrony.aoc2023.a13


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class ThirteenKtTest {

    @Test
    fun `returns null if no symmetry axis in list`() {
        val input = listOf("1221","2343")
        assertNull(findSymmetryAxis(input, 0))
    }

    @Test
    fun `returns null if no symmetry axis in list 2`() {
        val input = listOf("abba","bccc", "bccb", "abba")
        assertNull(findSymmetryAxis(input, 0))
    }

    @Test
    fun `returns value if symmetry axis in list`() {
        val input = listOf("abba","bccb", "bccb", "abba")
        assertEquals(1, findSymmetryAxis(input, 0))
    }


    @Test
    fun `returns value if symmetry axis in list but offset`() {
        val input = listOf("abcd","qwer","qwer","abcd","zzzz")

        assertEquals(1, findSymmetryAxis(input, 0))
    }

    @Test
    fun `returns value if symmetry axis in list but offset far`() {
        val input = listOf("aaa","bbb","ccc","ddd","111","111","ddd")
        assertEquals(4, findSymmetryAxis(input, 0))
    }

    @Test
    fun `returns value if symmetry axis at the beginning`() {
        val input = listOf("aaa", "aaa", "ccc", "ddd", "eee")
        assertEquals(0, findSymmetryAxis(input, 0))
    }

    @Test
    fun `returns value if symmetry axis at the end`() {
        val input = listOf("aaa", "bbb", "ccc", "ddd", "ddd")
        assertEquals(3, findSymmetryAxis(input, 0))
    }

    @Test
    fun `returns value if fake symmetry axis inside`() {
        val input = listOf("aaa","bbb","bbb","ccc","ccc","bbb","bbb","aaa")
        assertEquals(3, findSymmetryAxis(input, 0))
    }
}