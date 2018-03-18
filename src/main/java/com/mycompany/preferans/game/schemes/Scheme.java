package com.mycompany.preferans.game.schemes;

import com.mycompany.preferans.game.Game;
import com.mycompany.preferans.game.Party;
import com.mycompany.preferans.game.Scores;
import com.mycompany.preferans.game.StatusInParty;
import com.mycompany.preferans.game.trade.TradeOffer;
import com.mycompany.preferans.subjects.Player;

import java.util.Map;

public interface Scheme {
    int MIN_NUMBER_OF_TRICKS = 6;
    int SCORES_FOR_PLAY_NOTHING = 10;

    void changeScores(Game game, Party party, Map<Player, Scores> playersScoresMap);

    default void writeScores(Game game, Party party, Map<Player, Scores> playersScoresMap, int dumpPlayerCoef,
                             int whistCoef, int dumpWhistCoef) {
        Player playerInParty = party.getPlayerInParty();

        if (playerInParty != null) {
            int scoresToWrite = (party.getTrump().getRank().ordinal() + 1) * 2;

            if (party.getTricks().isEmpty()) {
                playersScoresMap.get(playerInParty).setPoolPoints(scoresToWrite);

                for (Player player : game.getPlayers()) {
                    game.getPlayersAndScores().get(player).add(playersScoresMap.get(player));
                }

                return;
            }

            TradeOffer tradeOffer = party.getTrade().getMaxTradeOffer();

            if (tradeOffer.getTradeOfferType() != TradeOffer.TradeOfferType.GET_NOTHING) {
                int difNumberOfScores = party.getTrump().getRank().ordinal() + MIN_NUMBER_OF_TRICKS
                        - party.getPlayerAndNumberOfTricks().get(playerInParty);

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
                        int needTricksToTake;

                        Player skipper = null;

                        if (party.getTrade().isGameOpen()) {

                            for (Player player1 : game.getPlayers()) {
                                if (party.getTrade().getPlayerAndStatus().get(player1) == StatusInParty.SKIPPER) {
                                    skipper = player1;
                                }
                            }

                            needTricksToTake = Math.max(2 - party.getTrump().getRank().ordinal(), 0) * 2 -
                                    party.getPlayerAndNumberOfTricks().get(skipper);
                        } else {
                            needTricksToTake = Math.max(2 - party.getTrump().getRank().ordinal(), 0);
                        }

                        difNumberOfScores = needTricksToTake -
                                party.getPlayerAndNumberOfTricks().get(player);

                        if (difNumberOfScores > 0) {
                            int scoresForDumpWhist = scoresToWrite / 2;

                            playersScoresMap.get(player)
                                    .setDumpPoints(scoresForDumpWhist * difNumberOfScores * dumpWhistCoef);
                        } else {

                            int newScores = playersScoresMap.get(player).getWhistPoints().get(playerInParty);

                            newScores += scoresToWrite * party.getPlayerAndNumberOfTricks().get(player) * whistCoef;

                            playersScoresMap.get(player).getWhistPoints()
                                    .put(playerInParty,
                                            newScores);
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
