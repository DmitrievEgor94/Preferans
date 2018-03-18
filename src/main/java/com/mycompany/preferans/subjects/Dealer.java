package com.mycompany.preferans.subjects;

import com.mycompany.preferans.game.Game;
import com.mycompany.preferans.game.Party;
import com.mycompany.preferans.game.Scores;
import com.mycompany.preferans.game.StatusInParty;
import com.mycompany.preferans.game.deck.Card;
import com.mycompany.preferans.game.deck.Deck;
import com.mycompany.preferans.game.schemes.Scheme;
import com.mycompany.preferans.game.trade.Trade;
import org.apache.log4j.Logger;

import java.util.*;

public class Dealer {
    private static final Logger log = Logger.getLogger(Dealer.class);

    private static final int NUMBER_OF_CARDS_IN_BEGINNING = 10;
    private static final int NUMBER_OF_PLAYERS = 3;

    private Scheme schemeOfGame;
    private Deck deck;

    public Dealer(Scheme schemeOfGame, Deck deck) {
        this.schemeOfGame = schemeOfGame;
        this.deck = deck;
    }

    public Game playGame(Set<Player> players, int partiesToPlay) {
        Game game = new Game(partiesToPlay, players);

        log.info("\n\nGame has started!");

        int positionOfFirstPlayer = 0;
        int numberOfDealing = 0;
        List<Player> listOfPlayers = new ArrayList<>(players);

        while (game.getPartiesToPlay() != game.getPlayedParties()) {
            numberOfDealing++;

            log.info("\n\nDealing " + numberOfDealing + " has started.");
            giveCardsToPlayers(players);

            List<Card> cardsBuyIn = deck.getCards()
                    .subList(players.size() * NUMBER_OF_CARDS_IN_BEGINNING, deck.getCards().size());
            log.info("Buy-in is " + cardsBuyIn);

            Trade trade = new Trade();

            log.info("Trade has started.");
            Player playerWithBiggestOffer =
                    trade.initTrade(players, listOfPlayers.get(positionOfFirstPlayer), cardsBuyIn);

            positionOfFirstPlayer++;
            positionOfFirstPlayer %= NUMBER_OF_PLAYERS;

            Party party = new Party(players, cardsBuyIn, trade, playerWithBiggestOffer);

            if (!hasPlayerWon(players)) {
                party.initParty(players);
            }

            for (Player player : players) {
                player.setActiveTradeOffer(null);
            }

            Map<Player, Scores> playerScoresMap = new HashMap<>();

            for (Player player : players) {
                playerScoresMap.put(player, createScores(players, player));
            }

            schemeOfGame.changeScores(game, party, playerScoresMap);

            log.info("Scores:");

            for (Player player : players) {
                log.info(player + " has got scores:\n" + playerScoresMap.get(player));
            }

            game.addParty(party);
            game.setPlayedParties(game.getPlayedParties() + 1);
        }

        return game;
    }

    private boolean hasPlayerWon(Set<Player> players) {
        int numberOfSkipPlayers = 0;

        for (Player player : players) {
            if (player.getActiveStatus() == StatusInParty.SKIPPER) {
                numberOfSkipPlayers++;
            }
        }

        return numberOfSkipPlayers == 2;
    }

    private Scores createScores(Set<Player> players, Player playerWhoNeedsScores) {
        Scores scores = new Scores();

        for (Player player : players) {
            Map<Player, Integer> whistPoints = scores.getWhistPoints();

            if (player != playerWhoNeedsScores)
                whistPoints.put(player, 0);
        }

        return scores;
    }

    private void giveCardsToPlayers(Set<Player> players) {
        Collections.shuffle(deck.getCards());

        int i = 0;

        for (Player player : players) {
            List<Card> cards = new ArrayList<>(deck.getCards().subList(i, i + NUMBER_OF_CARDS_IN_BEGINNING));

            player.setCardsOnHand(cards);
            log.info(player + " got " + cards);

            i += NUMBER_OF_CARDS_IN_BEGINNING;
        }
    }
}
