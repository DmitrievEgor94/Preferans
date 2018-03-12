package com.mycompany.preferans.subjects;

import com.mycompany.preferans.Game;
import com.mycompany.preferans.card_and_deck.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Dealer {
    private static final int  NUMBER_OF_CARDS_IN_BEGINNING = 10;

    private Deck deck;

    public Dealer(Deck deck) {
        this.deck = deck;
    }

    void playGame(Set<Player> players, int partiesToPlay ){

       /* if (players.size() != 3)*/

        Game game = new Game(partiesToPlay, players);


    }

    private void giveCardsToPlayers(Set<Player> players){
        Collections.shuffle(deck.getCards());

        for (Player player:players){
           // player.setCardsOnHand();
        }
    }

}
