package com.mycompany.preferans.game.trade;

import com.mycompany.preferans.game.deck.Card;

import java.util.Objects;

public class TradeOffer implements Comparable<TradeOffer> {
    private Card card;
    private TradeOfferType tradeOfferType;

    TradeOffer(Card card) {
        this.card = card;
        tradeOfferType = TradeOfferType.PLAY;
    }

    public TradeOffer(TradeOffer tradeOffer) {
        if (tradeOffer.getCard() != null) {
            card = new Card(tradeOffer.getCard().getSuit(), tradeOffer.getCard().getRank());
        }
        tradeOfferType = tradeOffer.getTradeOfferType();
    }

    public TradeOffer(TradeOfferType tradeOfferType) {
        this.tradeOfferType = tradeOfferType;
    }

    @Override
    public int compareTo(TradeOffer o) {
        if (o == null) return 1;

        if ((o.tradeOfferType == TradeOfferType.PLAY) && (tradeOfferType == TradeOfferType.PLAY)) {
            return card.compareTo(o.card);
        }

        if ((tradeOfferType == TradeOfferType.GET_NOTHING) && (o.tradeOfferType == TradeOfferType.PLAY)) {
            if (o.card.getRank().compareTo(Card.Rank.SEVEN) > 0)
                return -1;
            else {
                return 1;
            }
        }

        if ((tradeOfferType == TradeOfferType.PLAY) && (o.tradeOfferType == TradeOfferType.GET_NOTHING)) {
            if (card.getRank().compareTo(Card.Rank.SEVEN) > 0)
                return 1;
            else {
                return -1;
            }
        }

        return tradeOfferType.compareTo(o.tradeOfferType);
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

        TradeOffer tradeOffer = (TradeOffer) obj;

        return Objects.equals(card, tradeOffer.card) &&
                Objects.equals(tradeOfferType, tradeOffer.tradeOfferType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, tradeOfferType);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public TradeOfferType getTradeOfferType() {
        return tradeOfferType;
    }

    public void setTradeOfferType(TradeOfferType tradeOfferType) {
        this.tradeOfferType = tradeOfferType;
    }

    @Override
    public String toString() {
        if (tradeOfferType == TradeOfferType.PLAY) {
            return tradeOfferType + " [" + card + "]";
        }
        return tradeOfferType.toString();
    }

    public enum TradeOfferType {
        SKIP, PLAY, GET_NOTHING
    }
}
