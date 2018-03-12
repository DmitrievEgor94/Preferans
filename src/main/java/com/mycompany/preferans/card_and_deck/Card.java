package com.mycompany.preferans.card_and_deck;

class Card implements Comparable<Card> {
    private Suit suit;
    private Rank rank;

    public  enum  Suit {
        Spade , Clubs , Diamonds , Hearts
    }

    public enum Rank{
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

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

        if (suit.compareTo(o.suit) == 0) {
            return rank.compareTo(o.rank) ;
        }
        else {
            return suit.compareTo(o.suit);
        }
    }
}
