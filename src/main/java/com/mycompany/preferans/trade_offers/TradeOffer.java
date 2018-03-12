package com.mycompany.preferans.trade_offers;

public class TradeOffer implements Comparable<TradeOffer> {
    private Suit suit;
    private Rank rank;
    private SpecialOffer specialOffer;

    public TradeOffer(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public TradeOffer(SpecialOffer specialOffer) {
        this.specialOffer = specialOffer;
    }

    public  enum  Suit {
        Spade , Clubs , Diamonds , Hearts
    }

    public enum Rank {
        SIX, SEVEN, EIGHT, NINE, TEN
    }

    public enum SpecialOffer {
        SKIP, NOTHING
    }

    @Override
    public int compareTo(TradeOffer o) {
        if (o == null) return 1;

        if (specialOffer != null){
            if (o.specialOffer != null){
                return specialOffer.compareTo(o.specialOffer);
            }

            if (specialOffer == SpecialOffer.NOTHING) {
                return -1;
            }
            else {
                return 1;
            }
        }

        if (rank.compareTo(o.rank) == 0) {
            return suit.compareTo(o.suit) ;
        }
        else {
            return rank.compareTo(o.rank);
        }
    }
}
