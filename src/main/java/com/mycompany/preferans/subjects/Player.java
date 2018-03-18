package com.mycompany.preferans.subjects;


import com.mycompany.preferans.game.StatusInParty;
import com.mycompany.preferans.game.deck.Card;
import com.mycompany.preferans.game.trade.TradeOffer;

import java.util.*;
import java.util.stream.Collectors;


public class Player {
    private static final int MIN_NUMBER_OF_TRICKS_FOR_OFFER_PLAYER = 6;
    private static final int MAX_NUMBER_OF_TRICKS_FOR_OFFER_NOTHING = 2;
    private static final int MIN_NUMBER_OF_TRICKS_FOR_WHIST_PLAY = 4;

    private String name;
    private List<Card> cardsOnHand;
    private StatusInParty activeStatus;
    private TradeOffer activeTradeOffer;

    private List<Card> buyInOfActiveParty;

    public Player(String name) {
        activeTradeOffer = null;
        this.name = name;
    }

    public Card giveCard(Card trump, List<Card> cardsInTrick) {
        Card card;

        if (trump != null) {
            card = chooseMaxCardForTrick(trump, cardsInTrick);
        } else {
            card = chooseMinCardForTrick(cardsInTrick);
        }

        cardsOnHand.remove(card);

        return card;
    }

    public Map<Player, Card> giveCardOpenGame(Card trump, List<Card> cardsInTrick, Player playerWithStatusSkip) {
        Map<Player, Card> playerCardMap = new HashMap<>();

        getMaxCard(trump, cardsInTrick, playerWithStatusSkip, playerCardMap);

        Player player = playerCardMap.keySet().iterator().next();

        player.getCardsOnHand().remove(playerCardMap.get(player));

        if (player == this) {
            Card minCard = playerWithStatusSkip.chooseMinCardForTrick(cardsInTrick);

            playerCardMap.put(playerWithStatusSkip, minCard);

            playerWithStatusSkip.cardsOnHand.remove(minCard);
        } else {
            Card minCard = chooseMinCardForTrick(cardsInTrick);

            playerCardMap.put(this, minCard);

            cardsOnHand.remove(minCard);
        }

        return playerCardMap;
    }

    private void getMaxCard(Card trump, List<Card> cardsInTrick,
                            Player playerWithStatusSkip, Map<Player, Card> playerAndCard) {
        Card maxCard = chooseMaxCardForTrick(trump, cardsInTrick);
        Card maxCardSkipPlayer = playerWithStatusSkip.chooseMaxCardForTrick(trump, cardsInTrick);

        if (maxCard.getSuit() == trump.getSuit() && (maxCardSkipPlayer.getSuit() != trump.getSuit())) {
            playerAndCard.put(this, maxCard);
            return;
        }

        if (maxCard.getSuit() != trump.getSuit() && (maxCardSkipPlayer.getSuit() == trump.getSuit())) {
            playerAndCard.put(playerWithStatusSkip, maxCardSkipPlayer);
            return;
        }

        if (maxCard.getSuit() == maxCardSkipPlayer.getSuit()) {
            if (maxCard.compareTo(maxCardSkipPlayer) > 0) {
                playerAndCard.put(this, maxCard);
            } else {
                playerAndCard.put(playerWithStatusSkip, maxCardSkipPlayer);
            }

        } else {
            if (maxCard.compareTo(maxCardSkipPlayer) > 0) {
                playerAndCard.put(this, maxCard);
            } else {
                playerAndCard.put(playerWithStatusSkip, maxCardSkipPlayer);
            }
        }

    }

    private Card chooseMinCardForTrick(List<Card> cardsInTrick) {
        Card minCard = cardsOnHand.stream()
                .min(Comparator.comparing(Card::getRank)).get();

        if (cardsInTrick.isEmpty()) {
            return minCard;
        } else {
            Card firstCard = cardsInTrick.get(0);

            Card minCardOfFirstSuit = cardsOnHand.stream()
                    .filter(a -> (a.getRank().equals(firstCard.getRank())))
                    .min(Card::compareTo)
                    .orElse(null);

            if (minCardOfFirstSuit == null) {
                return minCard;
            }

            return minCardOfFirstSuit;
        }
    }

    private Card chooseMaxCardForTrick(Card trump, List<Card> cardsInTrick) {
        Card maxCard = cardsOnHand.stream()
                .max(Comparator.comparing(Card::getRank))
                .get();

        Card minCard = cardsOnHand.stream()
                .min(Comparator.comparing(Card::getRank)).get();

        if (cardsInTrick.isEmpty()) {
            return maxCard;
        } else {
            Card firstCard = cardsInTrick.get(0);

            Card maxCardOfFirstSuit = cardsOnHand.stream()
                    .filter(a -> (a.getRank().equals(firstCard.getRank())))
                    .max(Card::compareTo)
                    .orElse(null);

            if (maxCardOfFirstSuit == null) {
                Card maxCardOfTrumpSuit = cardsOnHand.stream()
                        .filter(a -> (a.getRank().equals(trump.getRank())))
                        .max(Card::compareTo)
                        .orElse(null);

                if (maxCardOfTrumpSuit == null) {
                    return minCard;
                }
                return maxCardOfTrumpSuit;
            }
            return maxCardOfFirstSuit;
        }
    }

    public List<Card> changeCardsBuyIn() {
        cardsOnHand.addAll(buyInOfActiveParty);

        chooseTradeOffer(activeTradeOffer);

        List<Card> leastCards = chooseTheLeastCards(activeTradeOffer.getCard());

        cardsOnHand = cardsOnHand.stream()
                .filter(a -> !leastCards.contains(a))
                .collect(Collectors.toList());

        return leastCards;
    }

    private List<Card> chooseTheLeastCards(Card trump) {
        List<Card> cardsNotTrump = cardsOnHand.stream()
                .filter(a -> a.getSuit() != trump.getSuit())
                .collect(Collectors.toList());

        cardsNotTrump.sort((a, b) -> -b.getRank().compareTo(a.getRank()));

        return cardsNotTrump.subList(0, 2);
    }

    public TradeOffer chooseTradeOffer(TradeOffer maxTradeOffer) {
        Set<Card> aces = new HashSet<>();
        Set<Card> kings = new HashSet<>();
        Map<Card.Suit, List<Card>> cardsOfDifSuits = getCardsOfSuits();

        for (Card card : cardsOnHand) {
            if (card.getRank() == Card.Rank.ACE) {
                aces.add(card);
            }

            if (card.getRank() == Card.Rank.KING) {
                kings.add(card);
            }
        }

        int possibleNumberOfTakenTricks = 0;

        Card.Suit suitWithMaxNumberOfCards = cardsOfDifSuits.entrySet().stream()
                .max(Comparator.comparingInt(a -> a.getValue().size()))
                .get()
                .getKey();

        for (Card card : cardsOfDifSuits.get(suitWithMaxNumberOfCards)) {
            if (!aces.contains(card) && !kings.contains(card)) {
                possibleNumberOfTakenTricks++;
            }
        }

        possibleNumberOfTakenTricks += aces.size() + kings.size();

        TradeOffer tradeOffer = new TradeOffer(TradeOffer.TradeOfferType.SKIP);

        if (possibleNumberOfTakenTricks >= MIN_NUMBER_OF_TRICKS_FOR_OFFER_PLAYER) {
            Card card = new Card(suitWithMaxNumberOfCards, Card.Rank.values()[possibleNumberOfTakenTricks -
                    MIN_NUMBER_OF_TRICKS_FOR_OFFER_PLAYER]);

            tradeOffer.setCard(card);
            tradeOffer.setTradeOfferType(TradeOffer.TradeOfferType.PLAY);
        }

        if (possibleNumberOfTakenTricks < MAX_NUMBER_OF_TRICKS_FOR_OFFER_NOTHING) {
            tradeOffer.setTradeOfferType(TradeOffer.TradeOfferType.GET_NOTHING);
        }

        if (tradeOffer.compareTo(maxTradeOffer) > 0) {
            activeTradeOffer = new TradeOffer(tradeOffer);
            return new TradeOffer(tradeOffer);
        }

        if (maxTradeOffer.compareTo(activeTradeOffer) == 0) {
            return maxTradeOffer;
        }

        activeTradeOffer = new TradeOffer(TradeOffer.TradeOfferType.SKIP);
        return activeTradeOffer;
    }

    private Map<Card.Suit, List<Card>> getCardsOfSuits() {
        Map<Card.Suit, List<Card>> cardsOfDifSuits = new HashMap<>();

        for (Card card : cardsOnHand) {
            List<Card> cards = cardsOfDifSuits.get(card.getSuit());

            if (cards == null) {
                cards = new ArrayList<>();
            }

            cards.add(card);

            cardsOfDifSuits.put(card.getSuit(), cards);
        }

        return cardsOfDifSuits;
    }

    public StatusInParty chooseStatus(TradeOffer tradeOffer) {
        Card trumpCard = tradeOffer.getCard();

        if (trumpCard == null) {
            activeStatus = StatusInParty.SKIPPER;
            return StatusInParty.SKIPPER;
        }

        Set<Card> aces = new HashSet<>();
        Set<Card> kings = new HashSet<>();

        List<Card> cardsOfTrump = new ArrayList<>();

        for (Card card : cardsOnHand) {
            if (card.getRank() == Card.Rank.ACE) {
                aces.add(card);
            }

            if (card.getRank() == Card.Rank.KING) {
                kings.add(card);
            }

            if (card.getSuit() == trumpCard.getSuit()) {
                cardsOfTrump.add(card);
            }
        }

        int possibleNumberOfTricks = 0;

        for (Card card : cardsOfTrump) {
            if (!aces.contains(card) && !kings.contains(card)) {
                possibleNumberOfTricks++;
            }
        }

        possibleNumberOfTricks += aces.size() + kings.size();

        if (possibleNumberOfTricks > MIN_NUMBER_OF_TRICKS_FOR_WHIST_PLAY) {
            activeStatus = StatusInParty.WHIST;
            return StatusInParty.WHIST;
        }

        activeStatus = StatusInParty.SKIPPER;
        return StatusInParty.SKIPPER;

    }

    public boolean chooseIsGameIsOpened() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public void setBuyInOfActiveParty(List<Card> buyInOfActiveParty) {
        this.buyInOfActiveParty = buyInOfActiveParty;
    }

    public TradeOffer getActiveTradeOffer() {
        return activeTradeOffer;
    }

    void setActiveTradeOffer(TradeOffer activeTradeOffer) {
        this.activeTradeOffer = activeTradeOffer;
    }

    public List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    void setCardsOnHand(List<Card> cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }

    public String getName() {
        return name;
    }

    public StatusInParty getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(StatusInParty activeStatus) {
        this.activeStatus = activeStatus;
    }

    @Override
    public String toString() {
        return name;
    }

}
