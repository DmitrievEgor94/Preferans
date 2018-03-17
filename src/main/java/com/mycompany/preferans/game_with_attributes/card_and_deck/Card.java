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
        SPADES('\u2660'),
        CLUBS('\u2663'),
        DIAMONDS('\u2666'),
        HEARTS('\u2665'),
        NO_SUITS('N');

        private char symbolInUnicode;

        Suit(char symbolInUnicode) {
            this.symbolInUnicode = symbolInUnicode;
        }

        @Override
        public String toString() {
            return String.valueOf(symbolInUnicode);
        }
    }

    public enum Rank {
        SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

        private String name;

        Rank(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public String toString() {
        return rank.toString() + suit;
    }

}
