package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

public class Card extends Face {

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

    private final int id;

    Card(Rank rank, Suit suit, int id) {
        super(rank, suit);
        this.id = id;
    }

    public Face getMyFace(){
        return new Face(this.rank, this.suit);
    }

    public Integer getId(){ return id;}

    public String toString() {
        return rank.toString().toLowerCase()
                + "_of_"
                + suit.toString().toLowerCase(); }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card && ((Card) obj).id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

