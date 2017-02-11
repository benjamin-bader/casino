package com.bendb.casino.model

import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test

class CardTests {
    @Test fun `createShuffledDeck returns a list of 52 cards`() {
        assertThat(createShuffledDeck(), hasSize(52))
    }

    @Test fun `createShuffledDeck does not contain duplicates`() {
        val set = mutableSetOf<Card>()
        var allUnique = true
        createShuffledDeck().forEach { allUnique = allUnique && set.add(it) }
        assertThat(allUnique, `is`(true))
    }

    @Test fun `aces are worth one point`() {
        assertThat(Card(Rank.ACE, Suit.CLUBS).pointValue, equalTo(1))
        assertThat(Card(Rank.ACE, Suit.DIAMONDS).pointValue, equalTo(1))
        assertThat(Card(Rank.ACE, Suit.SPADES).pointValue, equalTo(1))
        assertThat(Card(Rank.ACE, Suit.HEARTS).pointValue, equalTo(1))
    }

    @Test fun `two of spades is worth one point`() {
        assertThat(Card(Rank.TWO, Suit.SPADES).pointValue, equalTo(1))
    }

    @Test fun `ten of diamonds is worth two points`() {
        assertThat(Card(Rank.TEN, Suit.DIAMONDS).pointValue, equalTo(2))
    }

    @Test fun `non-point cards are worth zero points`() {
        fun isPointCard(card: Card): Boolean {
            return when {
                card.rank == Rank.ACE -> true
                card.rank == Rank.TWO && card.suit == Suit.SPADES -> true
                card.rank == Rank.TEN && card.suit == Suit.DIAMONDS -> true
                else -> false
            }
        }

        createShuffledDeck().forEach { card ->
            if (! isPointCard(card) ) {
                assertThat(card.pointValue, equalTo(0))
            }
        }
    }
}

private fun parseCardList(text: String): List<Card> {
    val parts = text.split(" ")

}