package com.example.deblefer.Classes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Deck {
    private static Collection<Card> cards = new HashSet<>();
    static {
        int id = 1;
        for (Card.Rank rank : Card.Rank.values())
            for (Card.Suit suit : Card.Suit.values())
                cards.add(new Card(rank, suit, id++));
    }
    public static Collection<Card> getUnmodifableDeck(){
        return Collections.unmodifiableCollection(cards);
    }

    public static Collection<Card> getModifableDeck(){
        return new HashSet<>(cards);
    }

    /*
    * moze sie przydac
    * */

    public Card getCard(Card.Rank rank, Card.Suit suit){
        return new Card(rank, suit, getCardId(rank, suit));
    }

    public int getCardId(Card.Rank rank, Card.Suit suit){
        for (Card card : cards){
            if(card.getRank() == rank && card.getSuit() == suit)
                return card.getId();
        }
        return 0;
    }

}
