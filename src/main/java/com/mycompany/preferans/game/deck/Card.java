package com.mycompany.preferans.game.deck;

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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Card card = (Card) obj;

        return Objects.equals(card.rank, rank) &&
                Objects.equals(card.suit, suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return rank.toString() + suit;
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

}
