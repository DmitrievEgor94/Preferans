package com.mycompany.preferans.subjects;

import com.mycompany.preferans.Main;
import com.mycompany.preferans.game_with_attributes.Game;
import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.Scores;
import com.mycompany.preferans.game_with_attributes.card_and_deck.Card;
import com.mycompany.preferans.game_with_attributes.card_and_deck.Deck;
import com.mycompany.preferans.game_with_attributes.schemes.Scheme;
import com.mycompany.preferans.game_with_attributes.trade_offers_and_trade.Trade;
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

    public void playGame(Set<Player> players, int partiesToPlay) {
        Game game = new Game(partiesToPlay, players);
        log.info("\n\nGame has started!");

        int positionOfFirstPlayer = 0;
        int numberOfParty = 0;
        List<Player> listOfPlayers = new ArrayList<>(players);

        while (game.getPartiesToPlay() != game.getPlayedParties()) {
            giveCardsToPlayers(players);

            numberOfParty++;

            List<Card> cardsBuyIn = deck.getCards()
                    .subList(players.size() * NUMBER_OF_CARDS_IN_BEGINNING, deck.getCards().size());
            log.info("Buy-in is " + cardsBuyIn);

            Trade trade = new Trade();

            Card trump = null;

            log.info("Trade has started.");
            Trade.RecordOfTrading playerWithBiggestOffer =
                    trade.initTrade(players, listOfPlayers.get(positionOfFirstPlayer));

            if (playerWithBiggestOffer != null) {
                trump = playerWithBiggestOffer.getTradeOffer().getCard();
            }

            positionOfFirstPlayer++;
            positionOfFirstPlayer %= NUMBER_OF_PLAYERS;

            Party party = new Party(players, cardsBuyIn, trade, trump);
            log.info("Party " + numberOfParty +" has started.");
            party.initParty(players);

            game.addParty(party);
            game.setPlayedParties(game.getPlayedParties() + 1);

            Map<Player, Scores> playerScoresMap = new HashMap<>();

            for (Player player : players) {
                playerScoresMap.put(player, createScores(players, player));
            }

            schemeOfGame.changeScores(game, party, playerScoresMap);

            log.info(playerScoresMap);
        }

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
