package com.bendb.casino.model

import java.util.Collections

enum class Rank(val value: Int, val shortText: String) {
    ACE(1, "A"),
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "T"),
    JACK(11, "J"),
    QUEEN(12, "Q"),
    KING(13, "K");


    infix operator fun plus(other: Rank): Rank? {
        return Rank.fromValue(this.value + other.value)
    }

    companion object {
        fun fromValue(value: Int): Rank? {
            return when (value) {
                1  -> ACE
                2  -> TWO
                3  -> THREE
                4  -> FOUR
                5  -> FIVE
                6  -> SIX
                7  -> SEVEN
                8  -> EIGHT
                9  -> NINE
                10 -> TEN
                11 -> JACK
                12 -> QUEEN
                13 -> KING

                else -> null
            }
        }
    }
}

enum class Suit(val shortText: String) {
    CLUBS("C"),
    DIAMONDS("D"),
    SPADES("S"),
    HEARTS("H")
}

data class Card(val rank: Rank, val suit: Suit) {
    val isFaceCard: Boolean = when (rank) {
            Rank.JACK,
            Rank.QUEEN,
            Rank.KING -> true

            else -> false
        }

    val pointValue: Int = when {
            rank == Rank.ACE -> 1
            rank == Rank.TWO && suit == Suit.SPADES -> 1
            rank == Rank.TEN && suit == Suit.DIAMONDS -> 2

            else -> 0
        }

    override fun toString() = "${rank.shortText}${suit.shortText}"
}

fun computeScoreForRound(cards: List<Card>): Int {
    var total = cards.map(Card::pointValue).filterNotNull().sum()

    if (cards.size > 26) {
        total += 3
    }

    if (cards.filter { it.suit == Suit.SPADES }.size > 6) {
        total += 1
    }

    return total
}

fun createShuffledDeck(): List<Card> {
    val cards = Suit.values().flatMapTo(mutableListOf()) { suit ->
        Rank.values().map { rank ->
            Card(rank, suit)
        }
    }

    Collections.shuffle(cards)

    return cards
}

sealed class Pile {
    class Additive(val cards: List<Card>): Pile() {
        override val rank = cards.first().rank
    }

    class Matching(val piles: List<Pile>): Pile() {
        override val rank = piles.first().rank
    }

    class FaceCards(val cards: List<Card>): Pile() {
        override val rank = cards.first().rank
    }

    abstract val rank: Rank
}

fun Pile.add(card: Card): Pile {
    when (this) {
        is Pile.Additive -> {
            if (this.rank.value > 10 && this.rank == card.rank) {
                return Pile.FaceCards(this.cards + card)
            }

            val combinedRank = this.rank + card.rank
            require(combinedRank != null && combinedRank.value <= 10) {
                "Cannot build with face cards or to a value over 10"
            }

            return Pile.Additive(this.cards + card)
        }

        is Pile.Matching -> error("Cannot add to a matched build")

        is Pile.FaceCards -> {
            require(this.rank == card.rank) {
                "blah blah something about face cards"
            }
            return Pile.FaceCards(this.cards + card)
        }
    }
}

fun Pile.match()