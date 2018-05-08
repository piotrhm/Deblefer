package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

public class Card implements Comparable<Card>{

    public enum Rank { DEUCE(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);
        private int power;
        Rank(int power){
            this.power = power;
        }

        public Integer getPower() {
            return power;
        }
    }

    public enum Suit { CLUBS(1), DIAMONDS(2), HEARTS(3), SPADES(4);
        private int power;
        Suit(int power){
            this.power = power;
        }

        public Integer getPower() {
            return power;
        }
    }

    protected final Card.Rank rank;
    protected final Card.Suit suit;

    public Rank getRank() { return rank; }
    public Suit getSuit() { return suit; }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String toString() {
        return rank.toString().toLowerCase()
                + "_of_"
                + suit.toString().toLowerCase(); }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card){
            Card card = (Card)obj;
            return this.rank.equals(card.rank) && this.suit.equals(card.suit);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return rank.hashCode() + 31*suit.hashCode();
    }

    @Override
    public int compareTo(@NonNull Card o) {
        if(this.getRank().getPower().equals(o.getRank().getPower()))
            return this.getSuit().getPower().compareTo(o.getSuit().getPower());
        else return this.getRank().getPower().compareTo(o.getRank().getPower());
    }
}

