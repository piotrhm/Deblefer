package com.example.deblefer.Cards;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Pair {

    private Card card1;
    private Card card2;

    public Pair(Card card1, Card card2){
        this.card1 = card1;
        this.card2 = card2;
    }

    public Card getCard1(){ return this.card1; }
    public Card getCard2(){ return this.card2; }

    public boolean equals(Object obj) {
        return obj instanceof Pair && ((Pair) obj).getCard2().equals(card2) && ((Pair) obj).getCard1().equals(card1);
    }

    @Override
    public int hashCode() {
        return card1.hashCode()*61+card2.hashCode();

    }

    @Override
    public String toString() {
        return card1.toString()+" + "+card2.toString();
    }


    public Collection<Card> getCards(){
        Set<Card> set = new HashSet<>();
        set.add(card1); set.add(card2);
        return set;
    }

}

