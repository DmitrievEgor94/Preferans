package com.mycompany.preferans.subjects;


import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.StatusInParty;
import com.mycompany.preferans.game_with_attributes.card_and_deck.Card;
import com.mycompany.preferans.game_with_attributes.trade_offers_and_trade.TradeOffer;
import org.apache.log4j.Logger;

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

    public Player(String name) {
        activeTradeOffer = null;
        this.name = name;
    }

    public Card giveCard(Card trump, List<Card> cardsInTrick, Party party) {
        Card maxCard = cardsOnHand.stream()
                .max(Comparator.comparing(Card::getRank)).get();

        Card minCard = cardsOnHand.stream()
                .min(Comparator.comparing(Card::getRank)).get();

        if (trump != null) {
            if (cardsInTrick.isEmpty()) {
                cardsOnHand.remove(maxCard);
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
                        cardsOnHand.remove(minCard);
                        return minCard;
                    }
                    cardsOnHand.remove(maxCardOfTrumpSuit);
                    return maxCardOfTrumpSuit;
                }
                cardsOnHand.remove(maxCardOfFirstSuit);
                return maxCardOfFirstSuit;
            }
        } else {
            if (cardsInTrick.isEmpty()) {
                cardsOnHand.remove(minCard);
                return minCard;
            } else {
                Card firstCard = cardsInTrick.get(0);

                Card minCardOfFirstSuit = cardsOnHand.stream()
                        .filter(a -> (a.getRank().equals(firstCard.getRank())))
                        .min(Card::compareTo)
                        .orElse(null);

                if (minCardOfFirstSuit == null) {
                    cardsOnHand.remove(minCard);
                    return minCard;
                }

                cardsOnHand.remove(minCardOfFirstSuit);
                return minCardOfFirstSuit;
            }

        }
    }

    public List<Card> changeCardsBuyIn(List<Card> buyIn, Party party) {
        if (party.getTrump() != null) {
            cardsOnHand.addAll(buyIn);

            TradeOffer newTradeOffer = chooseTradeOffer(new TradeOffer(TradeOffer.TradeOfferType.SKIP));

            if (newTradeOffer.getCard() != null) {
                party.setTrump(newTradeOffer.getCard());
                party.getTrade().setMaxTradeOffer(newTradeOffer);

                List<Card> leastCards = chooseTheLeastCards(newTradeOffer.getCard());

                cardsOnHand = cardsOnHand.stream()
                        .filter(a -> !leastCards.contains(a))
                        .collect(Collectors.toList());

                return leastCards;
            } else {
                cardsOnHand = cardsOnHand.stream()
                        .filter(a -> !buyIn.contains(a))
                        .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
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
        Map<Card.Suit, List<Card>> cardsOfDifSuits = getCardsOfSuits(cardsOnHand);

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

        if (maxTradeOffer.compareTo(activeTradeOffer) == 0) {
            return maxTradeOffer;
        }

        if (tradeOffer.compareTo(maxTradeOffer) > 0) {
            activeTradeOffer = new TradeOffer(tradeOffer);
            return new TradeOffer(tradeOffer);
        }

        return new TradeOffer(TradeOffer.TradeOfferType.SKIP);
    }

    private Map<Card.Suit, List<Card>> getCardsOfSuits(List<Card> givenCards) {
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
        } else {
            activeStatus = StatusInParty.SKIPPER;
            return StatusInParty.SKIPPER;
        }
    }

    public void setActiveTradeOffer(TradeOffer activeTradeOffer) {
        this.activeTradeOffer = activeTradeOffer;
    }

    public TradeOffer getActiveTradeOffer() {
        return activeTradeOffer;
    }

    public List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public void setCardsOnHand(List<Card> cardsOnHand) {
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
