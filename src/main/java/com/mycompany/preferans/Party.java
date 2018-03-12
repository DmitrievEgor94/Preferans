package com.mycompany.preferans;

import com.mycompany.preferans.subjects.Player;
import com.mycompany.preferans.trade_offers.TradeOffer;

import javax.smartcardio.Card;
import java.util.List;
import java.util.Map;

public class Party {
    private List<RecordOfTrading> tradings;
    private List<RecordOfTrick> tricks;

    private Map<Player, StatusInGame> playerAndStatus;
    private Map<Player, Card> palyerAndCardsInBeginning;

    private List<Card> cardsBuyIn;

    class RecordOfTrading{
        Player player;
        TradeOffer tradeOffer;

        public RecordOfTrading(Player player, TradeOffer tradeOffer) {
            this.player = player;
            this.tradeOffer = tradeOffer;
        }
    }

    public class RecordOfTrick{
        String playerName;
        TradeOffer tradeOffer;

    }
}
