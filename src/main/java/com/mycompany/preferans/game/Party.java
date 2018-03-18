package com.mycompany.preferans.game;

import com.mycompany.preferans.game.deck.Card;
import com.mycompany.preferans.game.trade.Trade;
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
    private Player playerWhoFirstMakesMove;

    private Map<Player, List<Card>> playerAndCardsInBeginning;

    private List<Card> cardsBuyIn;

    public Party(Set<Player> players, List<Card> cardsBuyIn, Trade trade, Player playerInParty) {
        playerAndCardsInBeginning = new HashMap<>();
        tricks = new ArrayList<>();

        this.cardsBuyIn = new ArrayList<>(cardsBuyIn);
        this.trade = trade;
        this.playerAndNumberOfTricks = new HashMap<>();
        this.playerInParty = playerInParty;

        if (playerInParty != null) {
            this.trump = playerInParty.getActiveTradeOffer().getCard();
        }

        for (Player player : players) {
            playerAndNumberOfTricks.put(player, 0);
            playerAndCardsInBeginning.put(player, new ArrayList<>(player.getCardsOnHand()));
        }
    }

    public void initParty(Set<Player> players) {
        Player firstPlayer = playerInParty;

        if (firstPlayer == null) {
            firstPlayer = players.iterator().next();
        }

        playerWhoFirstMakesMove = firstPlayer;

        while (!firstPlayer.getCardsOnHand().isEmpty()) {
            RecordOfTrick recordOfTrick = new RecordOfTrick();

            recordOfTrick.setFirstPlayer(firstPlayer);

            List<Card> cardsInTrick = new ArrayList<>();

            if (!trade.isGameOpen()) {
                playCloseGame(players, firstPlayer, recordOfTrick, cardsInTrick);
            } else {
                playOpenGame(players, firstPlayer, recordOfTrick, cardsInTrick);
            }

            log.info(firstPlayer + " gave deck " + recordOfTrick.getTrick().get(firstPlayer) + ".");

            for (Map.Entry<Player, Card> playerCardEntry : recordOfTrick.getTrick().entrySet()) {
                if (firstPlayer != playerCardEntry.getKey()) {
                    log.info(playerCardEntry.getKey() + " gave deck " + playerCardEntry.getValue() + ".");
                }
            }

            Player playerWithBiggestCard = recordOfTrick.findPlayerWithBiggestCard(trump, cardsInTrick.get(0));
            log.info(playerWithBiggestCard + " has got " + "trick.\n");

            if (!playerAndNumberOfTricks.keySet().contains(playerWithBiggestCard)) {
                playerAndNumberOfTricks.put(playerWithBiggestCard, 1);
            } else {
                Integer numberOfTricks = playerAndNumberOfTricks.get(playerWithBiggestCard) + 1;
                playerAndNumberOfTricks.put(playerWithBiggestCard, numberOfTricks);
            }

            recordOfTrick.setWinner(playerWithBiggestCard);

            tricks.add(recordOfTrick);

            firstPlayer = playerWithBiggestCard;
        }

        for (Player player : players) {
            log.info(player + " has got " + playerAndNumberOfTricks.get(player) + " tricks.");
        }
    }

    private void playOpenGame(Set<Player> players, Player firstPlayer, RecordOfTrick recordOfTrick,
                              List<Card> cardsInTrick) {
        Player whistPlayer = null;
        Player skipPlayer = null;

        for (Player player : players) {
            if (player.getActiveStatus() == StatusInParty.WHIST) {
                whistPlayer = player;
            }

            if (player.getActiveStatus() == StatusInParty.SKIPPER) {
                skipPlayer = player;
            }
        }

        if (firstPlayer == playerInParty) {
            Card cardPlayerInPartyGave = firstPlayer.giveCard(trump, cardsInTrick);
            cardsInTrick.add(cardPlayerInPartyGave);

            Map<Player, Card> notPlayersCards = whistPlayer.giveCardOpenGame(trump, cardsInTrick, skipPlayer);

            List<Card> cardsGivenByWhistPlayer =
                    new ArrayList<>(notPlayersCards.values());

            recordOfTrick.getTrick().putAll(notPlayersCards);
            recordOfTrick.getTrick().put(playerInParty, cardPlayerInPartyGave);

            cardsInTrick.addAll(cardsGivenByWhistPlayer);

        } else {
            Map<Player, Card> notPlayersCards = whistPlayer.giveCardOpenGame(trump, cardsInTrick, skipPlayer);

            List<Card> cardsGivenByWhistPlayer =
                    new ArrayList<>(notPlayersCards.values());

            cardsInTrick.addAll(cardsGivenByWhistPlayer);

            Card cardPlayerInPartyGave = playerInParty.giveCard(trump, cardsInTrick);

            recordOfTrick.getTrick().put(playerInParty, cardPlayerInPartyGave);
            recordOfTrick.getTrick().putAll(notPlayersCards);

            cardsInTrick.add(cardPlayerInPartyGave);
        }
    }

    private void playCloseGame(Set<Player> players, Player firstPlayer, RecordOfTrick recordOfTrick, List<Card> cardsInTrick) {
        Card card = firstPlayer.giveCard(trump, cardsInTrick);

        recordOfTrick.trick.put(firstPlayer, card);
        cardsInTrick.add(card);

        for (Player player : players) {
            if (player != firstPlayer) {
                card = player.giveCard(trump, cardsInTrick);

                recordOfTrick.trick.put(player, card);
                cardsInTrick.add(card);
            }
        }
    }

    public Player getPlayerWhoFirstMakesMove() {
        return playerWhoFirstMakesMove;
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

    public List<RecordOfTrick> getTricks() {
        return tricks;
    }

    public List<Card> getCardsBuyIn() {
        return cardsBuyIn;
    }

    public Map<Player, List<Card>> getPlayerAndCardsInBeginning() {
        return playerAndCardsInBeginning;
    }

    public Trade getTrade() {
        return trade;
    }

    public Map<Player, Integer> getPlayerAndNumberOfTricks() {
        return playerAndNumberOfTricks;
    }

    public class RecordOfTrick {

        private Map<Player, Card> trick;
        private Player winner;
        private Player firstPlayer;

        RecordOfTrick() {
            trick = new HashMap<>();
        }

        public Map<Player, Card> getTrick() {
            return trick;
        }

        public Player getWinner() {

            return winner;
        }

        void setWinner(Player winner) {
            this.winner = winner;
        }

        public Player getFirstPlayer() {
            return firstPlayer;
        }

        void setFirstPlayer(Player firstPlayer) {
            this.firstPlayer = firstPlayer;
        }

        Player findPlayerWithBiggestCard(Card trump, Card firstCard) {
            Map<Player, Card> playersWithTrump = new HashMap<>();
            Map<Player, Card> playersWithFirstCardSuit = new HashMap<>();


            for (Map.Entry<Player, Card> playerCardEntry : trick.entrySet()) {
                if ((trump != null) && (playerCardEntry.getValue().getSuit() == trump.getSuit())) {
                    playersWithTrump.put(playerCardEntry.getKey(), playerCardEntry.getValue());
                }

                if (playerCardEntry.getValue().getSuit() == firstCard.getSuit()) {
                    playersWithFirstCardSuit.put(playerCardEntry.getKey(), trick.get(playerCardEntry.getKey()));
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
