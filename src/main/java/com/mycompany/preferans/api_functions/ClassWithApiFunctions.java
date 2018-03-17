package com.mycompany.preferans.api_functions;

import com.mycompany.preferans.game_with_attributes.Game;
import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.Scores;
import com.mycompany.preferans.game_with_attributes.StatusInParty;
import com.mycompany.preferans.game_with_attributes.card_and_deck.Card;
import com.mycompany.preferans.game_with_attributes.trade_offers_and_trade.Trade;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ClassWithApiFunctions {
    private static final Logger log = Logger.getLogger(ClassWithApiFunctions.class);

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
            for (Player player : playerAndCardsInBeginning.keySet()) {
                printWriter.println(player + " has got " + playerAndCardsInBeginning.get(player) + ".");
            }

            printWriter.println("Buy-in is " + party.getCardsBuyIn() + ".");
            printWriter.println("First trader is " + party.getTrade().getPersonWhoStartsTrading() + ".");

            printWriter.println("Person who first gives card is " + party.getTricks().get(0).getFirstPlayer() + ".");

        } catch (FileNotFoundException e) {
            log.error("File " + fileName + " can't be created.");
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
            log.error("File " + fileName + " can't be created.");
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

            for (Player player : playerAndStatus.keySet()) {
                printWriter.println(player + " has got status " + playerAndStatus.get(player) + ".");
            }

        } catch (FileNotFoundException e) {
            log.error("File " + fileName + " can't be created.");
        }
    }

    public void api4(int numberOfDealing, String fileName, boolean useExistingFile) {
        Party party = game.getParties().get(numberOfDealing - 1);

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            List<Party.RecordOfTrick> records = party.getTricks();

            printWriter.println("Trump is " + party.getTrump() + ".");

            for (Party.RecordOfTrick record : records) {
                Map<Player, Card> trick = record.getTrick();

                printWriter.println("First player is " + record.getFirstPlayer() + ".");

                for (Player player : trick.keySet()) {
                    printWriter.println(player + " gave card " + trick.get(player) + ".");
                }

                printWriter.println(record.getWinner() + " has got trick.\n");
            }

        } catch (FileNotFoundException e) {
            log.error("File " + fileName + " can't be created.");
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
            log.error("File " + fileName + " can't be created.");
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
            log.error("File " + fileName + " can't be created.");
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
            log.error("File " + fileName + " can't be created.");
        }
    }

    public void api8(String fileName, Player player, int numberOfDealings, boolean useExistingFile) {
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {
            printWriter.println(player + " has points: " + game.getAllPoints(player, numberOfDealings));

        } catch (FileNotFoundException e) {
            log.error("File " + fileName + " can't be created.");
        }
    }

    public void api9(String fileName, Player player, int numberOfDealings, boolean useExistingFile) {
        List<Party> playedParties = game.getParties().subList(0, numberOfDealings);

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {


        } catch (FileNotFoundException e) {
            log.error("File " + fileName + " can't be created.");
        }
    }

    public void api10( int numberOfDealings,String fileName, boolean useExistingFile) {

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                new File(fileName),
                useExistingFile))) {

            for (Player player : game.getPlayers()) {
                api8(fileName, player, numberOfDealings, true);
            }

        } catch (FileNotFoundException e) {
            log.error("File " + fileName + " can't be created.");
        }
    }


}
