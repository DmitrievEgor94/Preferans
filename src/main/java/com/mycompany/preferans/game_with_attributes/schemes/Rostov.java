package com.mycompany.preferans.game_with_attributes.schemes;

import com.mycompany.preferans.game_with_attributes.Game;
import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.Scores;
import com.mycompany.preferans.game_with_attributes.StatusInParty;
import com.mycompany.preferans.game_with_attributes.trade_offers_and_trade.TradeOffer;
import com.mycompany.preferans.subjects.Player;

import java.util.Map;

public class Rostov implements Scheme {
    @Override
    public void changeScores(Game game, Party party,  Map<Player, Scores> playersScoresMap) {
        writeScores(game, party, playersScoresMap, 1, 2,1);
    }
}
