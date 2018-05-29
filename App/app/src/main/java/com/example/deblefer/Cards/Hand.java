package com.example.deblefer.Cards;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Hand {
    private Card card1;
    private Card card2;
    private boolean suitIsDifferent;

    public Hand(Card card1, Card card2){
        if(card1.compareTo(card2) > 0){
            this.card1 = card1;
            this.card2 = card2;
        }
        else {
            this.card1 = card2;
            this.card2 = card1;
        }

        suitIsDifferent = !card1.getSuit().equals(card2.getSuit());
        if(card1.equals(card2))
            suitIsDifferent = true;
    }

    public Hand(Card card1, Card card2, boolean suitIsDifferent){
        this.card1 = card1;
        this.card2 = card2;
        this.suitIsDifferent = suitIsDifferent;
    }

    public Card getCard1(){ return this.card1; }
    public Card getCard2(){ return this.card2; }

    public boolean equals(Object obj) {
        return obj instanceof Hand && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        if(suitIsDifferent)
            return card1.getRank().hashCode() + 31* card2.getRank().hashCode();
        return card1.getRank().hashCode() + 61*card2.getRank().hashCode();
    }

    @Override
    public String toString() {
        return card1.getRank().getSimpleName() + card2.getRank().getSimpleName() + (suitIsDifferent ? "o" : "s");
    }


    public Collection<Card> getCards(){
        Set<Card> set = new HashSet<>();
        set.add(card1); set.add(card2);
        return set;
    }
}

