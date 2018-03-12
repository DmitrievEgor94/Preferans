package com.mycompany.preferans;

import com.mycompany.preferans.trade_offers.TradeOffer;

public class StatusInGame {
    private Status status;
    private TradeOffer tradeOffer;

    public StatusInGame(Status status) {
        this.status = status;
    }

    public StatusInGame(Status status, TradeOffer tradeOffer) {
        this.status = status;
        this.tradeOffer = tradeOffer;
    }

    public enum Status {
        Player, Whist, HalfWhist, Skip
    }
}
