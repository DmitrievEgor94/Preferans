package com.mycompany.preferans.game_with_attributes.schemes;

import com.mycompany.preferans.game_with_attributes.Game;
import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.Scores;
import com.mycompany.preferans.game_with_attributes.StatusInParty;
import com.mycompany.preferans.game_with_attributes.trade_offers_and_trade.TradeOffer;
import com.mycompany.preferans.subjects.Player;

import java.util.Map;

public interface Scheme {
    int MIN_NUMBER_OF_TRICKS = 6;
    int SCORES_FOR_PLAY_NOTHING = 10;

    void changeScores(Game game, Party party, Map<Player, Scores> playersScoresMap);

    default void writeScores(Game game, Party party, Map<Player, Scores> playersScoresMap, int dumpPlayerCoef,
                             int whistCoeff, int dumpWhistCoef) {
        Player playerInParty = party.getPlayerInParty();

        if (playerInParty != null) {
            TradeOffer tradeOffer = party.getTrade().getMaxTradeOffer();

            if (tradeOffer.getTradeOfferType() != TradeOffer.TradeOfferType.GET_NOTHING) {
                int difNumberOfScores = party.getTrump().getRank().ordinal() + MIN_NUMBER_OF_TRICKS
                        - party.getPlayerAndNumberOfTricks().get(playerInParty);

                int scoresToWrite = (party.getTrump().getRank().ordinal() + 1) * 2;

                if (difNumberOfScores > 0) {
                    int scoresForNotTakenTricks = difNumberOfScores * scoresToWrite * dumpPlayerCoef;

                    playersScoresMap.get(playerInParty).setDumpPoints(scoresForNotTakenTricks);

                    for (Player player : game.getPlayers()) {
                        if (player != playerInParty) {
                            playersScoresMap.get(player).getWhistPoints().put(playerInParty, scoresForNotTakenTricks);
                        }
                    }
                } else {
                    playersScoresMap.get(playerInParty).setPoolPoints(scoresToWrite);
                }

                for (Player player : game.getPlayers()) {
                    if (player.getActiveStatus() == StatusInParty.WHIST) {
                        difNumberOfScores = Math.max(2 - party.getTrump().getRank().ordinal(), 0) -
                                party.getPlayerAndNumberOfTricks().get(player);

                        if (difNumberOfScores > 0) {
                            int scoresForDumpWhist = scoresToWrite/2;

                            playersScoresMap.get(player)
                                    .setDumpPoints(scoresForDumpWhist * difNumberOfScores * dumpWhistCoef);
                        } else {
                            playersScoresMap.get(player).getWhistPoints()
                                    .put(playerInParty,
                                            scoresToWrite * party.getPlayerAndNumberOfTricks().get(player) * whistCoeff);
                        }
                    }
                }

            } else {
                int takenTricks = party.getPlayerAndNumberOfTricks().get(playerInParty);

                if (takenTricks != 0) {
                    playersScoresMap.get(playerInParty).setDumpPoints(takenTricks * SCORES_FOR_PLAY_NOTHING * dumpPlayerCoef);
                } else {
                    playersScoresMap.get(playerInParty).setPoolPoints(takenTricks * SCORES_FOR_PLAY_NOTHING);
                }
            }

        } else {
            for (Player player : game.getPlayers()) {
                int takenTricks = party.getPlayerAndNumberOfTricks().get(player);
                if (takenTricks != 0) {
                    playersScoresMap.get(player).setDumpPoints(takenTricks * 2);
                }
            }
        }

        for (Player player : game.getPlayers()) {
            game.getPlayersAndScores().get(player).add(playersScoresMap.get(player));
        }
    }
}
