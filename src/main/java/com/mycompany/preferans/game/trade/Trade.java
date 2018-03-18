package com.mycompany.preferans.game.trade;

import com.mycompany.preferans.game.StatusInParty;
import com.mycompany.preferans.game.deck.Card;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.util.*;

public class Trade {
    private static final Logger log = Logger.getLogger(Trade.class);

    private List<RecordOfTrading> records;
    private Map<Player, StatusInParty> playerAndStatus;
    private TradeOffer maxTradeOffer;
    private Player personWhoStartsTrading;

    private boolean gameIsOpened;

    public Trade() {
        records = new ArrayList<>();
        playerAndStatus = new HashMap<>();
        maxTradeOffer = new TradeOffer(TradeOffer.TradeOfferType.SKIP);
    }

    public Player initTrade(Set<Player> players, Player firstPlayer, List<Card> buyIn) {
        personWhoStartsTrading = firstPlayer;

        while (!hasTradeEnded(players)) {
            TradeOffer tradeOffer;

            if ((firstPlayer.getActiveTradeOffer() == null)
                    || (firstPlayer.getActiveTradeOffer().getTradeOfferType() != TradeOffer.TradeOfferType.SKIP)) {

                tradeOffer = firstPlayer.chooseTradeOffer(maxTradeOffer);

                log.info(firstPlayer + " has chosen trade offer " + tradeOffer);

                if (tradeOffer.compareTo(maxTradeOffer) > 0) {
                    maxTradeOffer = tradeOffer;
                    log.info("Max trade offer is " + maxTradeOffer);
                }

                records.add(new RecordOfTrading(firstPlayer, tradeOffer));
            }

            Set<Player> otherPlayers = new HashSet<>(players);
            otherPlayers.remove(firstPlayer);

            for (Player player : otherPlayers) {
                if ((player.getActiveTradeOffer() == null) ||
                        (player.getActiveTradeOffer().getTradeOfferType() != TradeOffer.TradeOfferType.SKIP)) {
                    tradeOffer = player.chooseTradeOffer(maxTradeOffer);

                    log.info(player + " has chosen trade offer " + tradeOffer);
                    if (tradeOffer.compareTo(maxTradeOffer) > 0) {
                        maxTradeOffer = tradeOffer;
                        log.info("Max trade offer is " + maxTradeOffer);
                    }

                    records.add(new RecordOfTrading(player, tradeOffer));
                }
            }
        }

        return chooseStatusAndGetFirstPlayer(players, buyIn);
    }

    private Player chooseStatusAndGetFirstPlayer(Set<Player> players, List<Card> buyIn) {
        Player whoFirstGivesCard = null;

        if (maxTradeOffer.getTradeOfferType() != TradeOffer.TradeOfferType.SKIP) {
            for (Player player : players) {
                if (maxTradeOffer.equals(player.getActiveTradeOffer())) {
                    log.info(player + " has won the trade.");

                    player.setActiveStatus(StatusInParty.PLAYER);
                    playerAndStatus.put(player, StatusInParty.PLAYER);

                    whoFirstGivesCard = player;
                    break;
                }
            }
        } else {
            for (Player player : players) {
                player.setActiveStatus(StatusInParty.SKIPPER);
                playerAndStatus.put(player, StatusInParty.SKIPPER);
            }
        }

        if (whoFirstGivesCard != null) {
            chooseStatusOfPlayers(players, buyIn, whoFirstGivesCard);

        }
        return whoFirstGivesCard;
    }

    private void chooseStatusOfPlayers(Set<Player> players, List<Card> buyIn, Player whoFirstGivesCard) {
        openBuyIn(players, buyIn);

        log.info("Buy-in is opened for all players!");

        if (maxTradeOffer.getTradeOfferType() == TradeOffer.TradeOfferType.GET_NOTHING) {
            for (Player player : players) {
                if (player != whoFirstGivesCard) {
                    playerAndStatus.put(player, StatusInParty.SKIPPER);

                    log.info(player + " has got status " + StatusInParty.SKIPPER + ".");
                }
            }
        } else {
            List<Card> cardsFirstPlayerReturned = whoFirstGivesCard.changeCardsBuyIn();

            maxTradeOffer = whoFirstGivesCard.getActiveTradeOffer();

            log.info(whoFirstGivesCard + " has returned in buy-in " + cardsFirstPlayerReturned);
            log.info(whoFirstGivesCard + " has changed trade offer on " + maxTradeOffer);

            Set<Player> whistAndSkipPlayers = new HashSet<>(players);
            whistAndSkipPlayers.remove(whoFirstGivesCard);

            int numberOfWhistPlayers = 0;
            Player whistPlayer = null;

            for (Player player : whistAndSkipPlayers) {
                StatusInParty status = player.chooseStatus(maxTradeOffer);

                if (status == StatusInParty.WHIST) {
                    numberOfWhistPlayers++;
                    whistPlayer = player;
                }

                playerAndStatus.put(player, status);
                log.info(player + " has got status " + status + ".");
            }

            if (numberOfWhistPlayers == 1) {
                gameIsOpened = whistPlayer.chooseIsGameIsOpened();
                if (gameIsOpened) {
                    log.info(whistPlayer + " has chosen open game.");
                } else {
                    log.info(whistPlayer + " has chosen close game.");
                }
            }
        }
    }

    private void openBuyIn(Set<Player> players, List<Card> buyIn) {
        for (Player player : players) {
            player.setBuyInOfActiveParty(new ArrayList<>(buyIn));
        }
    }


    private boolean hasTradeEnded(Set<Player> players) {
        if (records.isEmpty()) {
            return false;
        }

        int numberOfPlayerStatus = 0;

        for (Player player : players) {
            TradeOffer tradeOffer = player.getActiveTradeOffer();

            if (tradeOffer != null) {
                if ((tradeOffer.getTradeOfferType() == TradeOffer.TradeOfferType.PLAY)
                        || (tradeOffer.getTradeOfferType() == TradeOffer.TradeOfferType.GET_NOTHING)) {
                    numberOfPlayerStatus++;
                }
            }
        }

        return numberOfPlayerStatus <= 1;
    }

    public boolean isGameOpen() {
        return gameIsOpened;
    }

    public List<RecordOfTrading> getRecords() {
        return records;
    }

    public Map<Player, StatusInParty> getPlayerAndStatus() {
        return playerAndStatus;
    }

    public Player getPersonWhoStartsTrading() {
        return personWhoStartsTrading;
    }

    public TradeOffer getMaxTradeOffer() {
        return maxTradeOffer;
    }

    public void setMaxTradeOffer(TradeOffer maxTradeOffer) {
        this.maxTradeOffer = maxTradeOffer;
    }

    public class RecordOfTrading {
        private Player player;
        private TradeOffer tradeOffer;

        RecordOfTrading(Player player, TradeOffer tradeOffer) {
            this.player = player;
            this.tradeOffer = tradeOffer;
        }

        public TradeOffer getTradeOffer() {
            return tradeOffer;
        }

        public Player getPlayer() {
            return player;
        }
    }
}
