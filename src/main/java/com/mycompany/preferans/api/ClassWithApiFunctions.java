package com.mycompany.preferans.api;

import com.mycompany.preferans.game.Game;
import com.mycompany.preferans.game.Party;
import com.mycompany.preferans.game.Scores;
import com.mycompany.preferans.game.StatusInParty;
import com.mycompany.preferans.game.deck.Card;
import com.mycompany.preferans.game.trade.Trade;
import com.mycompany.preferans.game.trade.TradeOffer;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ClassWithApiFunctions {
    private static final Logger log = Logger.getLogger(ClassWithApiFunctions.class);

    private static final String CREATING_FILE_IS_IMPOSSIBLE = "Can't create file";

    private Game game;

    public ClassWithApiFunctions(Game game) {
        this.game = game;
    }

    public void api1(int numberOfDealing, String fileName, boolean useExistingFile) {
        Party party = game.getParties().get(numberOfDealing - 1);

        Map<Player, List<Card>> playerAndCardsInBeginning = party.getPlayerAndCardsInBeginning();

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {
            for (Map.Entry<Player, List<Card>> playerListEntry : playerAndCardsInBeginning.entrySet()) {
                printWriter.println(playerListEntry.getKey() + " has got " + playerListEntry.getValue() + ".");
            }

            printWriter.println("Buy-in is " + party.getCardsBuyIn() + ".");
            printWriter.println("First trader is " + party.getTrade().getPersonWhoStartsTrading() + ".");

            printWriter.println("Person who first gives card is " + party.getPlayerWhoFirstMakesMove() + ".");

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api2(int numberOfDealing, String fileName, boolean useExistingFile) {
        Party party = game.getParties().get(numberOfDealing - 1);

        Trade trade = party.getTrade();

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            List<Trade.RecordOfTrading> records = trade.getRecords();

            for (Trade.RecordOfTrading record : records) {
                printWriter.println(record.getPlayer() + " has chosen " + record.getTradeOffer() + ".");
            }

            printWriter.println("Buy-in is " + party.getCardsBuyIn() + ".");

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api3(int numberOfDealing, String fileName, boolean useExistingFile) {
        Party party = game.getParties().get(numberOfDealing - 1);

        Trade trade = party.getTrade();

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            List<Trade.RecordOfTrading> records = trade.getRecords();

            for (Trade.RecordOfTrading record : records) {
                printWriter.println(record.getPlayer() + " has chosen " + record.getTradeOffer() + ".");
            }

            Map<Player, StatusInParty> playerAndStatus = trade.getPlayerAndStatus();

            for (Map.Entry<Player, StatusInParty> playerStatusInPartyEntry : playerAndStatus.entrySet()) {
                printWriter.println(playerStatusInPartyEntry.getKey() + " has got status "
                        + playerStatusInPartyEntry.getValue() + ".");
            }

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api4(int numberOfDealing, String fileName, boolean useExistingFile) {
        Party party = game.getParties().get(numberOfDealing - 1);

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            List<Party.RecordOfTrick> records = party.getTricks();

            printWriter.println("Trump is " + party.getTrump() + ".");

            if (records.isEmpty()){
                printWriter.println(party.getPlayerInParty()+" has won this game without play!");
            }

            for (Party.RecordOfTrick record : records) {
                Map<Player, Card> trick = record.getTrick();

                printWriter.println("First player is " + record.getFirstPlayer() + ".");

                for (Map.Entry<Player, Card> playerCardEntry : trick.entrySet()) {
                    printWriter.println(playerCardEntry.getKey() + " gave deck " + playerCardEntry.getValue() + ".");
                }

                printWriter.println(record.getWinner() + " has got trick.\n");
            }

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api5(int numberOfDealing, String fileName, boolean useExistingFile) {
        Party party = game.getParties().get(numberOfDealing - 1);

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            Map<Player, Integer> playerAndNumberOfTricks = party.getPlayerAndNumberOfTricks();

            Set<Player> players = game.getPlayers();

            Map<Player, List<Scores>> playersAndScores = game.getPlayersAndScores();

            for (Player player : players) {
                printWriter.println(player + " has got " + playerAndNumberOfTricks.get(player) + " tricks.");

                printWriter.println(player + " has got scores:\n" +
                        playersAndScores.get(player).get(numberOfDealing - 1));
            }

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api6(int numberOfDealing, String fileName, boolean useExistingFile) {

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            api1(numberOfDealing, fileName, true);
            api3(numberOfDealing, fileName, true);
            api4(numberOfDealing, fileName, true);
            api5(numberOfDealing, fileName, true);

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api7(int numberOfDealings, String fileName, Player player, boolean useExistingFile) {
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            printWriter.println("Scores for " + player + " after " + numberOfDealings + " dealings:");

            printWriter.println("pool points:" + game.getPoolPoints(player, numberOfDealings));

            for (Player player1 : game.getPlayers()) {
                if (player1 != player) {
                    printWriter.println("whist points on " + player1 + ": " + game.getWhistPoints(player, player1, numberOfDealings));
                }
            }

            printWriter.println("dump points: " + game.getDumpPoints(player, numberOfDealings));

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api8(int numberOfDealings, String fileName, Player player, boolean useExistingFile) {
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {
            printWriter.println(player + " has points: " + game.getAllPoints(player, numberOfDealings));

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    public void api9(int numberOfDealings, String fileName, Player player, boolean useExistingFile) {
        List<Party> playedParties = game.getParties().subList(0, numberOfDealings);

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            int numberOfSkipParties = 0;
            List<Integer> numberOfTricksOnSkipParties = new ArrayList<>();

            int numberOfNothingGames = 0;
            List<Integer> numberOfTricksNothingGames = new ArrayList<>();

            int numberOfPlayerGames = 0;
            List<TradeOffer> tradeOffersForPlayersGames = new ArrayList<>();
            List<Integer> numberOfTricksForPlayersGames = new ArrayList<>();

            int numberOfWhistParties = 0;
            List<String> openCloseGames = new ArrayList<>();
            List<TradeOffer> tradeOffersForWhistGames = new ArrayList<>();
            List<Integer> numberOfTricksForWhistGames = new ArrayList<>();

            for (Party playedParty : playedParties) {

                numberOfSkipParties = getNumberOfSkipParties(player, numberOfSkipParties,
                        numberOfTricksOnSkipParties, playedParty);

                if (playedParty.getTrade().getPlayerAndStatus().get(player) == StatusInParty.PLAYER) {
                    if (playedParty.getTrade().getMaxTradeOffer().getTradeOfferType()
                            == TradeOffer.TradeOfferType.GET_NOTHING) {
                        numberOfNothingGames++;
                        numberOfTricksNothingGames.add(playedParty.getPlayerAndNumberOfTricks().get(player));
                    } else {
                        numberOfPlayerGames++;
                        tradeOffersForPlayersGames.add(playedParty.getTrade().getMaxTradeOffer());
                        numberOfTricksForPlayersGames.add(playedParty.getPlayerAndNumberOfTricks().get(player));

                    }

                }

                numberOfWhistParties = getNumberOfWhistParties(player, numberOfWhistParties,
                        openCloseGames, tradeOffersForWhistGames, numberOfTricksForWhistGames, playedParty);
            }

            printWriter.println("Skipper parties:");
            printWriter.println("Number of skip parties: " + numberOfSkipParties);
            printWriter.println("Player take tricks: " + numberOfTricksOnSkipParties + "\n");

            printWriter.println("Get-nothing parties:");
            printWriter.println("Number of get-nothing parties: " + numberOfNothingGames);
            printWriter.println("Player take tricks: " + numberOfTricksNothingGames + "\n");

            printWriter.println("Player parties:");
            printWriter.println("Number of player parties: " + numberOfPlayerGames);
            printWriter.println("Trade offers of parties: " + tradeOffersForPlayersGames);
            printWriter.println("Taken tricks: " + numberOfTricksForPlayersGames + "\n");

            printWriter.println("Whist parties:");
            printWriter.println("Number of whist parties: " + numberOfWhistParties);
            printWriter.println("Open-closed parties: " + openCloseGames);
            printWriter.println("Trade offers of parties: " + tradeOffersForWhistGames);
            printWriter.println("Taken tricks: " + numberOfTricksForWhistGames + "\n");
        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }

    private int getNumberOfSkipParties(Player player, int numberOfSkipParties,
                                       List<Integer> numberOfTricksOnSkipParties, Party playedParty) {
        boolean skipParty = true;

        for (Player player1 : game.
                getPlayers()) {

            if (playedParty.getTrade().getPlayerAndStatus().get(player1) != StatusInParty.SKIPPER) {
                skipParty = false;
            }
        }

        if (skipParty) {
            numberOfSkipParties++;
            numberOfTricksOnSkipParties.add(playedParty.getPlayerAndNumberOfTricks().get(player));
        }
        return numberOfSkipParties;
    }

    private int getNumberOfWhistParties(Player player, int numberOfWhistParties, List<String> openCloseGames,
                                        List<TradeOffer> tradeOffersForWhistGames,
                                        List<Integer> numberOfTricksForWhistGames, Party playedParty) {
        if (playedParty.getTrade().getPlayerAndStatus().get(player) == StatusInParty.WHIST) {
            numberOfWhistParties++;

            if (playedParty.getTrade().isGameOpen()) {
                openCloseGames.add("Open");
            } else {
                openCloseGames.add("Close");
            }

            tradeOffersForWhistGames.add(playedParty.getTrade().getMaxTradeOffer());
            numberOfTricksForWhistGames.add(playedParty.getPlayerAndNumberOfTricks().get(player));
        }
        return numberOfWhistParties;
    }

    public void api10(int numberOfDealings, String fileName, boolean useExistingFile) {

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            for (Player player : game.getPlayers()) {
                api8(numberOfDealings, fileName, player, true);
            }

        } catch (FileNotFoundException e) {
            log.error(CREATING_FILE_IS_IMPOSSIBLE + fileName);
        }
    }


}
