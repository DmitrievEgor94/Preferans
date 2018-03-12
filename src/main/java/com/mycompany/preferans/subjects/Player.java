package com.mycompany.preferans.subjects;

import javax.smartcardio.Card;
import java.util.List;

public class Player {
    String name;
    List<Card> cardsOnHand;

    void giveCard(){

    }

    void chooseContract(){

    }

    public void setCardsOnHand(List<Card> cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }
}
