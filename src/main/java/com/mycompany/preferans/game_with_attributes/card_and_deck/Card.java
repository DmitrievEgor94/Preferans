package com.mycompany.preferans.game_with_attributes.card_and_deck;

import java.util.Objects;

public class Card implements Comparable<Card> {
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public int compareTo(Card o) {
        if (o == null) return 1;

        if (rank.compareTo(o.rank) == 0) {
            return suit.compareTo(o.suit);
        } else {
            return rank.compareTo(o.rank);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Card card = (Card) o;

        return Objects.equals(card.rank, rank) &&
                Objects.equals(card.suit, suit);
    }

    public enum Suit {
        SPADE, CLUBS, DIAMONDS, HEARTS, NO_SUITS
    }

    public enum Rank {
        SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    @Override
    public String toString() {
        return suit + " " + rank;
    }
}
