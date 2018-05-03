package com.example.deblefer.Classes;

public class Card {;

    public enum Rank { DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

    private final int id;
    private  final Rank rank;
    private  final Suit suit;

    Card(Rank rank, Suit suit, int id) {
        this.rank = rank;
        this.suit = suit;
        this.id = id;
    }

    public Rank getRank() { return rank; }
    public Suit getSuit() { return suit; }
    public int getId(){ return id;}

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

