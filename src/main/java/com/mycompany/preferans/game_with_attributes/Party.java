package com.mycompany.preferans.game_with_attributes;

import com.mycompany.preferans.game_with_attributes.card_and_deck.Card;
import com.mycompany.preferans.game_with_attributes.trade_offers_and_trade.Trade;
import com.mycompany.preferans.subjects.Dealer;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.util.*;

public class Party {
    private static final Logger log = Logger.getLogger(Party.class);

    private Map<Player, Integer> playerAndNumberOfTricks;
    private Trade trade;
    private List<RecordOfTrick> tricks;
    private Card trump;
    private Player playerInParty;

    private Map<Player, List<Card>> playerAndCardsInBeginning;

    private List<Card> cardsBuyIn;
    private List<Card> cardsFirstPlayerReturned;

    public Party(Set<Player> players, List<Card> cardsBuyIn, Trade trade, Card trump) {
        playerAndCardsInBeginning = new HashMap<>();
        tricks = new ArrayList<>();

        this.cardsBuyIn = new ArrayList<>(cardsBuyIn);
        this.trade = trade;
        this.trump = trump;
        this.playerAndNumberOfTricks = new HashMap<>();

        for (Player player : players) {
            playerAndNumberOfTricks.put(player, 0);
            playerAndCardsInBeginning.put(player, new ArrayList<>(player.getCardsOnHand()));
        }
    }

    public Card getTrump() {
        return trump;
    }

    public void setTrump(Card trump) {
        this.trump = trump;
    }

    public Player getPlayerInParty() {
        return playerInParty;
    }

    public void initParty(Set<Player> players) {
        Player firstPlayer = null;

        for (Player player : players) {
            if (player.getActiveStatus() == StatusInParty.PLAYER) {
                firstPlayer = player;
                playerInParty = player;
                break;
            }

            firstPlayer = player;
        }

        cardsFirstPlayerReturned = firstPlayer.changeCardsBuyIn(cardsBuyIn, this);
        log.info(firstPlayer + " has returned in buy-in " +cardsFirstPlayerReturned);

        while (firstPlayer.getCardsOnHand().size() != 0) {
            RecordOfTrick recordOfTrick = new RecordOfTrick();

            List<Card> cardsInTrick = new ArrayList<>();

            Card card = firstPlayer.giveCard(trump, cardsInTrick, this);

            log.info(firstPlayer + " gave card " + card+".");

            recordOfTrick.trick.put(firstPlayer, card);
            cardsInTrick.add(card);

            for (Player player : players) {
                if (player != firstPlayer) {
                    card = player.giveCard(trump, cardsInTrick, this);

                    log.info(player + " gave card " + card+".");

                    recordOfTrick.trick.put(player, card);
                    cardsInTrick.add(card);
                }
            }

            Player playerWithBiggestCard = recordOfTrick.findPlayerWithBiggestCard(trump, cardsInTrick.get(0));
            log.info(playerWithBiggestCard + " has got " + "trick.");

            if (!playerAndNumberOfTricks.keySet().contains(playerWithBiggestCard)) {
                playerAndNumberOfTricks.put(playerWithBiggestCard, 1);
            } else {
                Integer numberOfTricks = playerAndNumberOfTricks.get(playerWithBiggestCard) + 1;
                playerAndNumberOfTricks.put(playerWithBiggestCard, numberOfTricks);
            }

            tricks.add(recordOfTrick);

            firstPlayer = playerWithBiggestCard;
        }

        for (Player player : players) {
            log.info(player + " has got " + playerAndNumberOfTricks.get(player) +" tricks.");
        }

    }

    public Trade getTrade() {
        return trade;
    }

    public Map<Player, Integer> getPlayerAndNumberOfTricks() {
        return playerAndNumberOfTricks;
    }

    public class RecordOfTrick {

        Map<Player, Card> trick;

        public RecordOfTrick() {
            trick = new HashMap<>();
        }

        public Map<Player, Card> getTrick() {
            return trick;
        }

        Player findPlayerWithBiggestCard(Card trump, Card firstCard) {
            Map<Player, Card> playersWithTrump = new HashMap<>();
            Map<Player, Card> playersWithFirstCardSuit = new HashMap<>();

            for (Player player : trick.keySet()) {
                if (trump != null) {
                    if (trick.get(player).getSuit() == trump.getSuit()) {
                        playersWithTrump.put(player, trick.get(player));
                    }
                }

                if (trick.get(player).getSuit() == firstCard.getSuit()) {
                    playersWithFirstCardSuit.put(player, trick.get(player));
                }
            }

            if (playersWithTrump.size() != 0) {
                return findPlayerWithBigCard(playersWithTrump);

            } else {
                return findPlayerWithBigCard(playersWithFirstCardSuit);
            }
        }

        Player findPlayerWithBigCard(Map<Player, Card> playerAndCard) {
            return playerAndCard.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .get()
                    .getKey();
        }
    }
}