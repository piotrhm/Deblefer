package com.example.deblefer.Cards;

import java.util.ArrayList;
import java.util.List;

public class CardInDialog {
    private Card card;
    private boolean chosen;
    private boolean used;

    private CardInDialog(Card card){
        this.card = card;
        chosen = false;
        used = false;
    }

    public Card getCard() {
        return card;
    }
    public boolean isChosen() {
        return chosen;
    }
    public boolean isUsed() {
        return used;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public static List<List<CardInDialog>> getDeckCardInDialog(){
        List<List<CardInDialog>> fourCardsList = new ArrayList<>();
        for(Card.Rank rank : Card.Rank.values()){
            List<CardInDialog> cards1 = new ArrayList<>();
            for(Card.Suit suit : Card.Suit.values())
                cards1.add(new CardInDialog(new Card(rank, suit)));
            fourCardsList.add(cards1);
        }
        return fourCardsList;
    }
}
