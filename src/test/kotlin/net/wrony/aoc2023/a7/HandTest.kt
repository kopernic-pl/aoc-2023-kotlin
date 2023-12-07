package net.wrony.aoc2023.a7

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class HandTest {

    @Test
    fun `hand of five aces is greater than HighCard`() {
        val hand1 = Hand.fromTextLine("AAAAA 1",)
        val hand2 = Hand.fromTextLine("23456 2",)
        assertTrue(hand1 > hand2)
    }

    @Test
    fun `hand of five aces is greater than hand of five kings`() {
        val hand1 = Hand.fromTextLine("AAAAA 1",)
        val hand2 = Hand.fromTextLine("KKKKK 2",)
        assertTrue(hand1 > hand2)
    }

    @Test
    fun `HighCard starting with ace is greater than HighCard starting with king`() {
        val hand1 = Hand.fromTextLine("A2345 1",)
        val hand2 = Hand.fromTextLine("K5678 2",)
        assertTrue(hand1 > hand2)
    }

    @Test
    fun `HighCard starting with king is smaller than HighCard starting with king`() {
        val hand1 = Hand.fromTextLine("K5678 2",)
        val hand2 = Hand.fromTextLine("A2345 1",)

        assertTrue(hand1 < hand2)
    }

    @Test
    fun `T2T8J is one pair`() {
        val hand = Hand.fromTextLine("T2T8J 1",)
        assertEquals(HandType.OnePair, hand.handType)
    }

    @Test
    fun `T2T8J with joker is triplet`() {
        val hand = Hand.fromTextLine("T2T8J 1",)
        assertEquals(HandType.ThreeOfAKind, hand.handTypeJokerized)
    }

    @Test
    fun `TJT8J with joker is four`() {
        val hand = Hand.fromTextLine("TJT8J 1",)
        assertEquals(HandType.FourOfAKind, hand.handTypeJokerized)
    }

}