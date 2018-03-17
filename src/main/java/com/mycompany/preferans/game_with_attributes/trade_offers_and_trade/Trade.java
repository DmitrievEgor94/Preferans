package com.mycompany.preferans.game_with_attributes.trade_offers_and_trade;

import com.mycompany.preferans.game_with_attributes.StatusInParty;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.util.*;

public class Trade {
    private static final Logger log = Logger.getLogger(Trade.class);

    private static final int NUMBER_OF_PLAYERS = 3;

    private List<RecordOfTrading> records;
    private Map<Player, StatusInParty> playerAndStatus;
    private TradeOffer maxTradeOffer;
    private Player personWhoStartsTrading;

    public Trade() {
        records = new ArrayList<>();
        playerAndStatus = new HashMap<>();
        maxTradeOffer = new TradeOffer(TradeOffer.TradeOfferType.SKIP);
    }

    public RecordOfTrading initTrade(Set<Player> players, Player firstPlayer) {
        personWhoStartsTrading = firstPlayer;

        while (!hasTradeEnded()) {
            TradeOffer tradeOffer = firstPlayer.chooseTradeOffer(maxTradeOffer);

            log.info(firstPlayer + " has chosen trade offer " + tradeOffer);

            if (tradeOffer.compareTo(maxTradeOffer) > 0) {
                maxTradeOffer = tradeOffer;
                log.info("Max trade offer is " + maxTradeOffer);
            }

            records.add(new RecordOfTrading(firstPlayer, tradeOffer));

            for (Player player : players) {
                if (player != firstPlayer) {
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

        List<RecordOfTrading> lastRecords = records.subList(records.size() - NUMBER_OF_PLAYERS, records.size());

        RecordOfTrading recordOfBiggestOffer = getRecordOfBiggestOffer(lastRecords, maxTradeOffer);

        if (recordOfBiggestOffer != null) {
            maxTradeOffer = recordOfBiggestOffer.tradeOffer;
            log.info(recordOfBiggestOffer.player + " has won the trade.");
        }

        return recordOfBiggestOffer;
    }

    private RecordOfTrading getRecordOfBiggestOffer(List<RecordOfTrading> lastRecords, TradeOffer maxTradeOffer) {
        RecordOfTrading biggestRecordWithTradeOffer = null;

        for (RecordOfTrading record : lastRecords) {
            if ((record.tradeOffer.getTradeOfferType() != TradeOffer.TradeOfferType.PLAY)
                    && (record.tradeOffer.getTradeOfferType() != TradeOffer.TradeOfferType.GET_NOTHING)) {

                if (maxTradeOffer.getTradeOfferType() == TradeOffer.TradeOfferType.GET_NOTHING) {
                    record.player.setActiveStatus(StatusInParty.SKIPPER);

                    log.info(record.player + " has got status " + StatusInParty.SKIPPER + ".");

                    playerAndStatus.put(record.player, StatusInParty.SKIPPER);
                } else {
                    StatusInParty status = record.player.chooseStatus(maxTradeOffer);

                    log.info(record.player + " has got status " + status + ".");

                    playerAndStatus.put(record.player, status);
                }
            } else {
                playerAndStatus.put(record.player, StatusInParty.PLAYER);
                record.player.setActiveStatus(StatusInParty.PLAYER);

                log.info(record.player + " has got status " + StatusInParty.PLAYER + ".");

                biggestRecordWithTradeOffer = record;
            }
        }
        return biggestRecordWithTradeOffer;
    }

    private boolean hasTradeEnded() {
        if (records.isEmpty()) {
            return false;
        }

        List<RecordOfTrading> lastRecords = records.subList(records.size() - NUMBER_OF_PLAYERS, records.size());

        int numberOfPlayerStatus = 0;

        for (RecordOfTrading record : lastRecords) {
            if (record.tradeOffer.getTradeOfferType() == TradeOffer.TradeOfferType.PLAY) {
                numberOfPlayerStatus++;
            }
        }

        return numberOfPlayerStatus <= 1;
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
