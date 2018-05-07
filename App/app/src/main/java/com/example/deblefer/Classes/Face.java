package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

public class Face implements Comparable<Face>{
    protected final Card.Rank rank;
    protected final Card.Suit suit;

    public Card.Rank getRank() { return rank; }
    public Card.Suit getSuit() { return suit; }

    Face(Card.Rank rank, Card.Suit suit){
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public int compareTo(@NonNull Face o) {
        if(this.getRank().getPower().equals(o.getRank().getPower()))
            return this.getSuit().getPower().compareTo(o.getSuit().getPower());
        else return this.getRank().getPower().compareTo(o.getRank().getPower());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Face && rank.equals(((Face) obj).rank) && suit.equals(((Face) obj).suit);
    }
}
