package com.mycompany.preferans.game_with_attributes.trade_offers_and_trade;

import com.mycompany.preferans.game_with_attributes.card_and_deck.Card;

public class TradeOffer implements Comparable<TradeOffer> {
    private Card card;
    private TradeOfferType tradeOfferType;

    public TradeOffer(Card card) {
       this.card = card;
        tradeOfferType = TradeOfferType.PLAY;
    }

    public TradeOffer(TradeOfferType tradeOfferType) {
        this.tradeOfferType = tradeOfferType;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setTradeOfferType(TradeOfferType tradeOfferType) {
        this.tradeOfferType = tradeOfferType;
    }

    @Override
    public int compareTo(TradeOffer o) {
        if (o == null) return 1;

        if ((o.tradeOfferType == TradeOfferType.PLAY) && (tradeOfferType == TradeOfferType.PLAY)) {
           return card.compareTo(o.card);
        }

        if ((tradeOfferType == TradeOfferType.GET_NOTHING) && (o.tradeOfferType == TradeOfferType.PLAY)){
            if (o.card.getRank().compareTo(Card.Rank.SEVEN) > 0)
                return -1;
            else {
                return  1;
            }
        }

        if ((tradeOfferType == TradeOfferType.PLAY) && (o.tradeOfferType == TradeOfferType.GET_NOTHING)){
            if (card.getRank().compareTo(Card.Rank.SEVEN) > 0)
                return 1;
            else {
                return -1;
            }
        }

        return tradeOfferType.compareTo(o.tradeOfferType);
    }

    public Card getCard() {
        return card;
    }

    public TradeOfferType getTradeOfferType() {
        return tradeOfferType;
    }

    public enum TradeOfferType {
        SKIP, PLAY, GET_NOTHING
    }

    @Override
    public String toString() {
        if (tradeOfferType == TradeOfferType.PLAY){
            return tradeOfferType + " [" + card+"]";
        }
        return tradeOfferType.toString();
    }
}
