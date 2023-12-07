package net.wrony.aoc2023.a7

import net.wrony.aoc2023.a7.Hand.Companion.fromTextLine
import kotlin.io.path.Path
import kotlin.io.path.readLines


//Five of a kind, where all five cards have the same label: AAAAA
//Four of a kind, where four cards have the same label and one card has a different label: AA8AA
//Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
//Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
//Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
//One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
//High card, where all cards' labels are distinct: 23456

enum class HandType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind
}


data class Hand(val cards: String, val bid: Int, val cardValMapper: (Char)->Int) : Comparable<Hand> {

    private val handMap by lazy { cards.groupingBy { it }.eachCount() }

    private val cardsAsNumbers by lazy {
        cards.map(cardValMapper)
    }

    companion object {
        fun fromTextLine(line: String, cardValMapper: (Char) -> Int): Hand {
            val (cards, bid) = line.split(" ")
            return Hand(cards, bid.toInt(), cardValMapper)
        }
    }

    val handType: HandType by lazy {
        val vals = handMap.values.sortedDescending()
        when {
            handMap.size == 1 -> HandType.FiveOfAKind
            vals.max() == 4 -> HandType.FourOfAKind
            vals.containsAll(listOf(3, 2)) -> HandType.FullHouse
            vals == listOf(3, 1, 1) -> HandType.ThreeOfAKind
            vals == listOf(2, 2, 1) -> HandType.TwoPair
            vals == listOf(2, 1, 1, 1) -> HandType.OnePair
            handMap.size == 5 -> HandType.HighCard
            else -> throw Exception("Unknown hand type should not happen $this, map: $handMap")
        }
    }

    val handTypeJokerized: HandType by lazy {
        when {
            !cards.contains('J') -> return@lazy handType
            else -> {
                return@lazy listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').map { r ->
                    Hand(cards.replace('J', r), bid, cardValMapper).handType
                }.maxOf { it }
            }
        }
    }

    override fun compareTo(other: Hand): Int {
        return when (this.handType.ordinal - other.handType.ordinal) {
            0 -> compareByOrder(other)
            else -> this.handType.ordinal - other.handType.ordinal
        }
    }

    fun compareJokerized(otherHand: Hand): Int {
        return when (this.handTypeJokerized.ordinal - otherHand.handTypeJokerized.ordinal) {
            0 -> compareByOrder(otherHand)
            else -> this.handTypeJokerized.ordinal - otherHand.handTypeJokerized.ordinal
        }
    }

    private fun compareByOrder(other: Hand): Int {
        this.cardsAsNumbers.forEachIndexed { index, i ->
            when {
                i > other.cardsAsNumbers[index] -> return 1
                i < other.cardsAsNumbers[index] -> return -1
            }
        }
        return 0
    }
}

val valMapper = { c: Char ->
    when (c) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> c.toString().toInt()
    }
}

val jokerizedValMapper = { c: Char ->
    when (c) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 1
        'T' -> 10
        else -> c.toString().toInt()
    }
}

fun main() {


    Path("src/main/resources/7.txt").readLines()
        .map { fromTextLine(it, valMapper) }
        .sorted()
        .mapIndexed { idx, hand -> idx to ((idx+1) * hand.bid).toLong() }
        .sumOf { it.second }
        .let { println(it) }

    //255103572 -
    //254024898 OK

    val c: Comparator<Hand> = Comparator { o1, o2 ->
        o1.compareJokerized(o2)
    }
    Path("src/main/resources/7.txt").readLines()
        .map { fromTextLine(it, jokerizedValMapper) }
        .sortedWith(c)
        .mapIndexed { idx, hand -> idx to ((idx+1) * hand.bid).toLong() }
        .sumOf { it.second }
        .let { println(it)}
}