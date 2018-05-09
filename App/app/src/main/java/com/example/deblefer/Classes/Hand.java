package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

public class Hand{
    private Card card1;
    private Card card2;
    private boolean suitIsDifferent;

    Hand(Card card1, Card card2){
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

    @Override
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
        return card1.getRank().getSimpleName() + card2.getRank().getSimpleName() + (suitIsDifferent ? "s" : "o");
    }
}
